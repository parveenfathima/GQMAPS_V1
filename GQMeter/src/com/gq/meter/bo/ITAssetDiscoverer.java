package com.gq.meter.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.gq.meter.ComputerMeter;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterData;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;

import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.SpeedTestSnpsht;
import com.gq.meter.object.SpeedTestSnpshtId;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;
import com.gq.meter.util.SpeedTestHelper;
import com.gq.meter.util.StringCompression;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 * @author chandru.p
 * @change parveen , rathish , sriram
 * 
 */
public final class ITAssetDiscoverer {

    private String gqmid = null;
    public String localIPCommunityString = null;
    private static Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private Gson gson = new GsonBuilder().create();
    private GQMeterResponse gqmResponse = null;
    private String meterProtocol;

    private LinkedList<String> snmpKnownIPList = new LinkedList<String>();
    private LinkedList<String> snmpUnknownIPList = new LinkedList<String>();
    List<GQErrorInformation> gqErrorInfoList = new LinkedList<GQErrorInformation>();

    private HashMap<MeterProtocols, LinkedList<String>> switches = new HashMap<MeterProtocols, LinkedList<String>>(4);

    Client client;
    WebResource service;

    public ITAssetDiscoverer() {
        super();

        client = Client.create(new DefaultClientConfig());
        service = client.resource(MeterConstants.restURL);
    }

    /**
     * This method used to find the asset type
     * 
     * @param communityIPMap
     * @return
     */
    private List<ProtocolData> findAssets(HashMap<String, String> communityIPMap) {

        String snmpVersion = null;
        String communityString = null;
        String ipAddress = null;

        MeterProtocols assetProtocol = null;
        HashMap<String, String> snmpDetails = null;

        
        List<ProtocolData> pdList = new LinkedList<ProtocolData>();

        ExecutorService es = Executors.newCachedThreadPool();

        try {
            // Iterating the community ip map
            for (Entry<String, String> entry : communityIPMap.entrySet()) {
                ipAddress = entry.getKey();
                communityString = entry.getValue();

                List<String> errorList = new LinkedList<String>();
                snmpDetails = MeterUtils.isSnmpConfigured(communityString, ipAddress);

                if (snmpDetails.get("snmpUnKnownIp") != null) {
                	
                    snmpUnknownIPList.add(ipAddress);
                    errorList.add(communityString + " - " + ipAddress + " -  : Unable to reach the asset");
                    gqErrorInfoList.add(new GQErrorInformation(null, errorList));
                    continue;
                }

                snmpVersion = snmpDetails.get("snmpVersion");
                
                if (snmpDetails.get("snmpKnownIp") != null) {
                    snmpKnownIPList.add(ipAddress);

                    assetProtocol = MeterUtils.getAssetType(communityString, ipAddress, snmpVersion);
                    if (assetProtocol == null) {
                        errorList.add(communityString + " - " + ipAddress
                                + " -  : Unable to reach the asset may be due to slow network connection");
                        continue;
                    }
                    // ********** here is where the meter object is being made *************
                    if ((meterProtocol.equals(MeterConstants.PRINTER_PROTOCOL) && assetProtocol
                            .equals(MeterProtocols.PRINTER))
                            || (meterProtocol.equals(MeterConstants.COMPUTER_PROTOCOL) && assetProtocol
                                    .equals(MeterProtocols.COMPUTER))
                            || (meterProtocol.equals(MeterConstants.NSRG_PROTOCOL) && assetProtocol
                                    .equals(MeterProtocols.NSRG))
                            || (meterProtocol.equals(MeterConstants.STORAGE_PROTOCOL) && assetProtocol
                                    .equals(MeterProtocols.STORAGE))
                            || meterProtocol.equals(MeterConstants.IT_PROTOCOL)) {

                        Thread t = new Thread(new AssetDiscoverythread(snmpVersion, switches.get(assetProtocol),
                                communityString, ipAddress, assetProtocol, pdList, gqErrorInfoList));
                        es.execute(t);

                        // i hope the last 2 params of the adt constructor will have them set so they are available here
                        // very rusty stupid code from ss - sep 11 , 2013
                        // it works !! - sep 18 , 2013
                    }
                }

            }// for loop ends

            es.shutdown();
            // wait for all threads to complete or we shouldnt execute nothing beyond here...
            boolean finished = es.awaitTermination(1, TimeUnit.MINUTES); // this is a blocking call

            System.out.println(" [GQMETER] all threads complete ");

        }
        catch (Exception e) {
            System.out.println(" [GQMETER] Exception occured while processing the communityIpMap ");
            e.printStackTrace();
        }
        finally {
            gqmResponse.setErrorInformationList(gqErrorInfoList); // Added the errors to the GQMResponse
            
        }

        return pdList;
    }

    /**
     * This method used to return the community and ip addresses read from the asset input file
     * 
     * @param inputFilePath
     * @return
     */
    private void readInput(String inputFilePath,HashMap<String, String> communityIPMap,HashMap<String, String> speedTestIPMap) {

        File assetsInputFile = null;
        //HashMap<String, String> communityIPMap = new HashMap<String, String>();

        if (inputFilePath == null || inputFilePath.trim().length() == 0) {
            System.out.println(" [GQMETER] Not a valid input file argument");
            System.exit(1);
        }

        try {
            assetsInputFile = new File(inputFilePath);

            if (!assetsInputFile.exists() || !assetsInputFile.isFile()) {
                System.out.println(" [GQMETER] Unable to locate the input file");
                System.exit(1);
            }

            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));

            String line = null;
            boolean headerFound = false;
            int headerLinesCount = 0;
            Map<String, Integer> switchOcc = new HashMap<String, Integer>(); // integer value has no significance. just
                                                                             // using it for map features

            // process the header section
            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("#") || line.length() == 0) {
                    continue;
                }

                if (line.equals("$$")) { // header line found
                    headerFound = true;
                    break;
                }
                // do the header processing
                // now we expect only 5 lines with key value pairs , nothing if count is more than 5 we complain and get
                // out
                ++headerLinesCount;

                // split the line to key value
                String headerToken[] = line.split("\\s+");
                
                String keyy = headerToken[0].toLowerCase();
                
                // Token count validation for all meters,meter id except speedtest meter.
                
                if(keyy.equals(MeterConstants.METER_ID) ||keyy.equals(MeterConstants.COMPUTER_SWITCH)||keyy.equals(MeterConstants.STORAGE_SWITCH)
                		||keyy.equals(MeterConstants.PRINTER_SWITCH)||keyy.equals(MeterConstants.NSRG_SWITCH)) {
                	// lines must contain only 2 tokens
                    if (headerToken.length != 2) {
                        System.out.println(" [GQMETER] Invalid header line ; wrong # of tokens ; line : " + line);
                        System.exit(0);
                    }
                }
                
                // key can be one among the 5 keys only
                if (keyy.equals(MeterConstants.METER_ID)) {
                    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
                    
                    if (p.matcher(headerToken[1]).find()) {
                        System.out.println(" [GQMETER] Meter has special char , Process Terminating Now ..");
                        System.exit(0);
                    }
                    gqmid = headerToken[1];
                }
                else if (keyy.equals(MeterConstants.COMPUTER_SWITCH) || keyy.equals(MeterConstants.STORAGE_SWITCH)) {
                    Map<String, Integer> tokenOcc = new HashMap<String, Integer>();
                    
                    // complain if the line starts or ends with a | symbol
                    if ((headerToken[1].charAt(0) == '|')
                            || (headerToken[1].charAt(headerToken[1].length() - 1) == '|')) {
                        System.out
                                .println(" [GQMETER] Switches cannot start or end with | chars , Process Terminating Now ..");
                        System.exit(0);
                    }

                    String switchToken[] = headerToken[1].split("\\|");

                    LinkedList<String> switchList = new LinkedList<String>();

                    for (String token : switchToken) {
                        token = token.toLowerCase();

                        if (token.equals(MeterConstants.INSTALLED_SOFTWARE)
                                || token.equals(MeterConstants.CONNECTED_DEVICES)
                                || token.equals(MeterConstants.PROCESS) || token.equals(MeterConstants.SNAPSHOT)) {
                            // good token
                            if (tokenOcc.containsKey(token)) {
                                System.out
                                        .println(" [GQMETER] Computer/Storage switch duplicates found , Process Terminating Now ..");
                                System.exit(0);
                            }
                            tokenOcc.put(token, 0); // value doesnt matter , may be a list is enough for this - ss sep 4
                                                    // , 2013
                            // put it in switch map for findassets method to use
                            switchList.add(token);
                        }
                        else {
                            System.out
                                    .println(" [GQMETER] Computer/Storage switch has invalid entry , Process Terminating Now ..");
                            System.exit(0);
                        }

                    }
                    if (keyy.equals(MeterConstants.COMPUTER_SWITCH)) {
                        switches.put(MeterProtocols.COMPUTER, switchList);
                    }
                    else {
                        switches.put(MeterProtocols.STORAGE, switchList);
                    }
                } // comp switch proc ends
                else if (keyy.equals(MeterConstants.PRINTER_SWITCH) || keyy.equals(MeterConstants.NSRG_SWITCH)) {
                	
                    // complain if the line starts or ends with a | symbol
                    if ((headerToken[1].charAt(0) == '|')
                            || (headerToken[1].charAt(headerToken[1].length() - 1) == '|')) {
                        System.out
                                .println(" [GQMETER] Switches cannot start or end with | chars , Process Terminating Now ..");
                        System.exit(0);
                    }

                    Map<String, Integer> tokenOcc = new HashMap<String, Integer>();
                    String switchToken[] = headerToken[1].split("\\|");
                    LinkedList<String> switchList = new LinkedList<String>();

                    for (String token : switchToken) {
                        token = token.toLowerCase();
                        if (token.equals(MeterConstants.CONNECTED_DEVICES) || token.equals(MeterConstants.SNAPSHOT)) {
                            // good token
                            if (tokenOcc.containsKey(token)) {
                                System.out
                                        .println(" [GQMETER] Printer/NSRG switch duplicates found , Process Terminating Now ..");
                                System.exit(0);
                            }
                            tokenOcc.put(token, 0); // value doesnt matter , may be a list is enough for this - ss sep 4
                                                    // , 2013
                            // put it in switch map for findassets method to use
                            switchList.add(token);
                        }
                        else {
                            System.out
                                    .println(" [GQMETER] Printer/NSRG switch has invalid entry , Process Terminating Now ..");
                            System.exit(0);
                        }
                    }
                    if (keyy.equals(MeterConstants.PRINTER_SWITCH)) {
                        switches.put(MeterProtocols.PRINTER, switchList);
                    }
                    else {
                        switches.put(MeterProtocols.NSRG, switchList);
                    }
                }
                //Validation for speed test meter input            
                else if ( keyy.equals ( MeterConstants.SPEEDTEST_SWITCH )) {
                	// line must contains 4 tokens.
                	if ( headerToken.length != 5 ) {
                		System.out.println(" [GQMETER] Invalid header line ; wrong # of tokens ; line : " + line);
                        System.exit(0);
                	}
                	// complain if the line starts or ends with a | symbol
                    if ( ( headerToken[1].charAt(0) == '|')
                            || ( headerToken[1].charAt( headerToken[1].length() - 1 ) == '|') ) {
                        System.out
                                .println(" [GQMETER] Switches cannot start or end with | chars , Process Terminating Now ..");
                        System.exit(0);
                    }
                    //to check the speedtest status condition here.
                    if ( headerToken[1].equals ( MeterConstants.SPEEDTEST_STATUS ) ) {
                    	if ( !isIPAddressValid ( headerToken[2] ) ) {
                            System.out.println(" [GQMETER] Invalid ip address ; continuing process ; line :" + line);
                            System.exit(0);
                        }
                        else {
                        	//here to add ipaddress and communitystring in Map
                        	localIPCommunityString=headerToken[4];
                            speedTestIPMap.put(headerToken[2], headerToken[3]);
                            
                        }
                    }
                }
                
                else {
                    System.out.println(" [GQMETER] Invalid Header Section entry , Process Terminating Now ..");
                    System.exit(0);
                }

                // put the meterid or switch thing in the map and check for duplicates
                if (switchOcc.containsKey(keyy)) {
                    System.out.println(" [GQMETER] Switch duplicates found , Process Terminating Now ..");
                    System.exit(0);
                }
                switchOcc.put(keyy, 0);

            } // while file read ends

            if (!headerFound) {
                System.out.println(" [GQMETER] No valid header delimeter found on Input Assets File , check manual..");
                System.exit(0);
            }

            if (switchOcc.size() != 6) {
                System.out.println(" [GQMETER] All switches need to be present on Input Assets File , check manual..");
                System.exit(0);
            }

            if (!(headerLinesCount == 6)) {
                System.out.println(" [GQMETER] Invalid Header Section , 5 lines are reqd , Process Terminating Now ..");
                System.exit(0);
            }

            // send it to gate keeper and check if it is good.
            meterProtocol = isValid(gqmid);

            // *************************
            // process the data section
            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("#") || line.length() == 0) {
                    continue;
                }

                // split the line to key value
                String dataToken[] = line.split("\\s+");

                // key can be one among the 5 keys only
                if ((dataToken.length == 1) || (dataToken.length > 3)) {
                    System.out.println(" [GQMETER] Invalid data line ; wrong # of tokens , continuing process .."
                            + line);
                    continue;
                }

                if (dataToken.length == 2) {
                    if (!isIPAddressValid(dataToken[1])) {
                        System.out.println(" [GQMETER] Invalid ip address ; continuing process ; line :" + line);
                        continue;
                    }
                    else {
                        communityIPMap.put(dataToken[1], dataToken[0]);
                    }
                }
                else if (dataToken.length == 3) {
                    if (isIPAddressValid(dataToken[1]) && isIPAddressValid(dataToken[2])) {
                        // to check the lowerboundip is greater than the upperboundip
                        if (MeterUtils.ipComparator.compare(dataToken[1], dataToken[2]) == -1) {
                            communityIPMap.put(dataToken[1], dataToken[0]);
                            // Here we iterate the ip range and find & add those intermediate ips to map
                            while (!(dataToken[1] = MeterUtils.nextIpAddress(dataToken[1])).equals(dataToken[2])) {
                                communityIPMap.put(dataToken[1], dataToken[0]);
                            }
                            communityIPMap.put(dataToken[2], dataToken[0]);
                        }
                        // to check the lowerboundip and ipupperboundip as same
                        else if (MeterUtils.ipComparator.compare(dataToken[1], dataToken[2]) == 0) {
                            communityIPMap.put(dataToken[1], dataToken[0]);
                        }
                        // else part for Invalid IP range
                        else {
                            System.out.println(" [GQMETER] : Invalid ip range in line :" + line);
                            System.exit(0);
                        }
                    }
                    else {
                        System.out.println(" [GQMETER] Invalid ip address ; continuing process ; line :" + line);
                        continue;
                    }
                }

            } // while file read ends

            br.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(" [GQMETER] Exception occured : " + e);
        }

    }
    
    private String isValid(String gqmid) {
        String protocolId = "";
        try {
            System.out.println(" [GQMETER] Validating the expiry date for the meter " + gqmid);
            ClientResponse response = service.path("metercheck").queryParam("meterId", gqmid)
                    .post(ClientResponse.class);

            String resp = response.getEntity(String.class).trim();
            String resp1 = resp.substring(1, 6);
            protocolId = resp.substring(6, (resp.length() - 1));

            if (resp1.equals("valid")) {
                System.out.println(" [GQMETER] The MeterId: " + gqmid + " is valid");
            }
            else {
                System.out.println(" [GQMETER] Your Meter: " + gqmid + " is expired/Invalid");
                System.exit(0);
            }
        }// try ends
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return protocolId;
    }

    private boolean isIPAddressValid(String ipAddress) {

        return pattern.matcher(ipAddress).matches();
    }
    
  //We are here to calculate download speed of given asset
    public List<ProtocolData> speedCalculation(HashMap<String,String> speedTestIPMap,String localIPCommunityString) {
    	 
    	 Snmp snmp = null;
		 CommunityTarget target = null;
		 String oidString = null;
		 OID rootOID = null;
		 CPNId id = null;
		 GQMeterData assetObject=null;
		 
         List<VariableBinding> result = null;
         List<ProtocolData> pdList = new LinkedList<ProtocolData>();
         List<String> errorList = new LinkedList<String>();
         //List<GQErrorInformation> gqErrorInfoList = new LinkedList<GQErrorInformation>();
         
         String assetId=null;
         Asset assetObj = null;         
         HashMap<String, String> snmpDetails = null;
         String snmpVersion = null;
         MeterProtocols assetProtocol = null;
         HashMap<String, String> networkBytes = null;
         
         String remoteIPAddress=null;
         String localIPAddress=null;
         String remoteIPCommunityString=null;
         Long runId = 0L;
         double uploadSpeed=0.0f;

         boolean isWindows = false;
         boolean isLinux=false;
         String sysDescription = null;
        
         try {
        	  
        	 snmp = new Snmp(new DefaultUdpTransportMapping());
        	 snmp.listen();
        	 
        	 localIPAddress = MeterConstants.localIPAddress;
        	 
        	 //To walk the snmp for current working machine here. reason for that calculate the asset id.
        	 snmpDetails = MeterUtils.isSnmpConfigured(localIPCommunityString, localIPAddress);
        	 
        	 if ( snmpDetails.get("snmpUnKnownIp") != null ) {
            	 snmpUnknownIPList.add(localIPAddress);
            	 errorList.add(localIPCommunityString + " - " + localIPAddress + " -  : Unable to reach the asset");
            	 gqErrorInfoList.add( new GQErrorInformation( null, errorList ) );
            	 return null;
             }
             
             snmpVersion = snmpDetails.get("snmpVersion");
             
             if(snmpVersion == null) {
            	 return null;
             }
             // snmop version is not null
        	 target = MeterUtils.makeTarget(localIPAddress,localIPCommunityString, snmpVersion);
		 
        	 assetObj = MeterUtils.sysBasicInfo(localIPCommunityString, localIPAddress, snmpVersion, errorList);
        	 assetObj.setProtocolId(MeterConstants.SPEEDTEST_PROTOCOL);
		 
        	 sysDescription = assetObj.getDescr();
  
        	 if (null != sysDescription) { // 1st if starts
        		 if (sysDescription.contains("windows")) {
        			 isWindows = true;
        		 }
        		 else if (sysDescription.contains("linux")) {
        			 isLinux = true;
        		 }
        		 else {
        			 
        		 }
        	 }// 1st if ends
	 	
        	 SpeedTestHelper speedTestHelperObj = new SpeedTestHelper();
        	 // the following oid's is used to get the asset id for windows.
        	 if (isWindows) {
        		 oidString = ".1.3.6.1.2.1.2.2.1";
        		 rootOID = new OID(oidString);
        		 result = MeterUtils.walk(rootOID, target);
        		 if (result != null && !result.isEmpty()) {
	 					HashMap<String, String> winNetworkMap = new HashMap<String, String>();
	 					networkBytes =speedTestHelperObj.winAssetIdCalc(result, rootOID, winNetworkMap);
	 					assetId = "C-" + networkBytes.get("macWinNetworkValue");
	 					assetObj.setAssetId(assetId);
	 				}// 2nd if ends
	 			}// 1st if ends
	 			// the following oid's is used to get the asset id for Linux.
	 			else {
	 				oidString = ".1.3.6.1.2.1.2.2.1";
	 				rootOID = new OID(oidString);
	 				result = MeterUtils.walk(rootOID, target);
	 				if (result != null && !result.isEmpty()) {
	 					String[] ethernet = new String[] { "eth0", "eth1", "eth2", "en1", "en2", "en3", "em1", "em2", "em3", "wlan" };
	 					HashMap<String, List<Long>> networkMap = new HashMap<String, List<Long>>();
	 					networkBytes = speedTestHelperObj.linuxAssetIdCalc(result, rootOID, ethernet, networkMap, assetId, sysDescription);
	 					assetId = "C-" + networkBytes.get("assetId");
	 					assetObj.setAssetId(assetId);
	 				}
	 				else {
	 					errorList.add(assetId + " Root OID : 1.3.6.1.2.1.2.2.1" + " "
	 						+ "Unable to get network bandwidth details and unable to collate asset ID");
	 				}// 2nd else ends
	 			}// 1st else ends
                         
        	 	//To walk the remote machine for calculate the speed.
    	 		for ( Entry<String, String> entry : speedTestIPMap.entrySet() ) {
    	 			
    	 			remoteIPAddress = entry.getKey();
    	 			remoteIPCommunityString = entry.getValue();
                 
    	 			snmpDetails = MeterUtils.isSnmpConfigured(remoteIPCommunityString, remoteIPAddress);
                 
    	 			if ( snmpDetails.get("snmpUnKnownIp") != null ) {
    	 				snmpUnknownIPList.add(remoteIPAddress);
    	 				errorList.add(remoteIPCommunityString + " - " + remoteIPAddress + " -  : Unable to reach the asset");
    	 				gqErrorInfoList.add( new GQErrorInformation( null, errorList ) );
    	 				return null;
    	 			}
                 
    	 			snmpVersion = snmpDetails.get("snmpVersion");
                 
    	 			if (snmpDetails.get("snmpKnownIp") != null ) {
    	 				snmpKnownIPList.add(remoteIPAddress);
    	 			}
    	 		}
        	 
        	 	// ASSET ID , RUN ID STARTS HERE.
        	 	id = new CPNId(runId, assetId);
        	 		
        	 	if( snmpVersion == null ) {
        	 		return null;
        	 	}
        	 	
    	 		target = MeterUtils.makeTarget(remoteIPAddress,remoteIPCommunityString, snmpVersion);
    	
    	 		oidString = ".";
   	 	 		rootOID = new OID(oidString);
   	 	 		
   	 	 		//we are here to perform speed test functionality based on speedtest tries.
   	 	 		double netDownloadSpeed = 0.0;
   	 	 		long startTime = 0;
   	 	 		long endTime = 0;
   	 	 		long timeTaken = 0;
				double bytesInMB = 0.0;
				double timeInSec = 0.0;
				double downloadSpeed=0.0;
				
				System.out.println(" Number of Bytes	TimeTaken");
   	 	 		for( int speedTestTriesCount = 1; speedTestTriesCount <= MeterConstants.SPEEDTEST_TRIES; speedTestTriesCount++) {
   	 	 			
   	 	 			startTime = System.currentTimeMillis();
   	 	 			result = MeterUtils.walk(rootOID, target);
   	 	 			endTime = System.currentTimeMillis();
   	 	 			timeTaken = endTime-startTime;
   	 	 			
   	 	 			long noOfBytes = 0L;
   	 	 			for(int resultCount = 0; resultCount < result.size(); resultCount++) {
   	 	 				noOfBytes = noOfBytes + result.get(resultCount).toString().length();
   	 	 			}
   	 	 			
   	 	 			System.out.println(" "+noOfBytes +"			"+ timeTaken);
   	 	 			
   	 	 			bytesInMB = (double) noOfBytes / (1024*1024)  ;
   	 	 			timeInSec = (double) timeTaken / 1000 ;
   	 	 			downloadSpeed = (bytesInMB / timeInSec);
   	 	 			netDownloadSpeed += downloadSpeed;
	 
   	 	 		}
   	 	 		
   	 	 		double avgDownloadSpeed = netDownloadSpeed / (MeterConstants.SPEEDTEST_TRIES);
   	 	 		
   	 	 		//To bulid a speed test snapshot object.
   	 	 		SpeedTestSnpshtId speedTestSnapshotIdObj = new SpeedTestSnpshtId(assetId, runId);
   	 	 		SpeedTestSnpsht speedTestSnapshotObj = new SpeedTestSnpsht(speedTestSnapshotIdObj, uploadSpeed, avgDownloadSpeed);

   	 	 		GQErrorInformation gqErrorInfo = null;
   	 	 		if (errorList != null && !errorList.isEmpty()) {
   	 	 			gqErrorInfo = new GQErrorInformation(sysDescription, errorList);
   	 	 		}
   	 	
   	 	 		assetObject = new GQMeterData(gqErrorInfo, speedTestSnapshotObj);
   	 	 		assetProtocol = MeterProtocols.SPEEDTEST;
   	 	 		pdList.add(new ProtocolData(assetProtocol, gson.toJson(assetObject.getMeterData())));
         } 
         catch (Exception e) {
        	e.printStackTrace();  
         }
         finally {
        		 gqmResponse.setErrorInformationList(gqErrorInfoList); // Added the errors to the GQMResponse
         }
         //To return a speedtest meter protcol list.
         return pdList;
     }

    public void discover(String inputFilePath) throws IOException {
    	
    	HashMap<String, String> communityIPMap = new HashMap<String, String>();// to process computer,printer and switches ip's.
    	HashMap<String, String> speedTestIPMap = new HashMap<String, String>();// to process speed test meter ip.
    	
        
    	System.out.println(" [GQMETER] started .......");
        gqmResponse = new GQMeterResponse();
        gqmResponse.setRecDttm(new Date());

        // The start time of the meter execution
        long startTime = System.currentTimeMillis();

        List<ProtocolData> assetsList = null;
        List<ProtocolData> speedAssetsList = null;

        //we are here to read the input file, to store the ip for required map
        readInput(inputFilePath,communityIPMap,speedTestIPMap);
        
        gqmResponse.setAssetScanned((short) ((communityIPMap.size())+speedTestIPMap.size()));

        if (communityIPMap.size() > 0) {
            assetsList = findAssets(communityIPMap);
            gqmResponse.addToAssetInformationList(assetsList);
        }
        
        if ( speedTestIPMap.size() > 0 && speedTestIPMap != null ) {    	
        	speedAssetsList=speedCalculation(speedTestIPMap,localIPCommunityString);
        	for(int i=0;i<speedAssetsList.size();i++) {
        		System.out.println(speedAssetsList.get(i).getData());
        	}
        	gqmResponse.addToAssetInformationList(speedAssetsList);
        }
    
        long endTime = System.currentTimeMillis();
        gqmResponse.setRunTimeMiliSeconds((endTime - startTime));
        gqmResponse.setStatus("pass");
        gqmResponse.setVersion("1");
        gqmResponse.setGqmid(gqmid);
        gqmResponse.setAssetDiscovered((short) snmpKnownIPList.size());

        // System.out.println(" [GQMETER] json : " + gson.toJson(gqmResponse));
        System.out.println(" [GQMETER] Total number of assets(ip address) in input file : "
                + gqmResponse.getAssetScanned());
        System.out.println(" [GQMETER] SNMP configured on : " + this.snmpKnownIPList.toString());
        System.out.println(" [GQMETER] SNMP not configured on : " + this.snmpUnknownIPList.toString());
        System.out.println(" [GQMETER] SNMP walk succeeded on : " + this.snmpKnownIPList.size());
        System.out.println(" [GQMETER] TOTAL time taken for meter execution : " + (endTime - startTime));

        // Sending the generated json output to the server
        Form form = new Form();
        form.add("gqMeterResponse", StringCompression.compress(gson.toJson(gqmResponse)));
        form.add("summary", "Sending data from GQMeter to GQGatekeeper");

        Builder builder = service.path("gatekeeper").type(MediaType.APPLICATION_JSON);
        ClientResponse response = builder.post(ClientResponse.class, form);

        System.out.println(" [GQMETER] ended successfully ...." + response.getStatus());

    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println(" [GQMETER] Usage : ASSETDETAILS_FILE_PATH");
            System.exit(1);
        }
        ITAssetDiscoverer itad = new ITAssetDiscoverer();
        String inputFilePath = args[0].trim();
        itad.discover(inputFilePath);
    }
    
}// class ends
