package com.gq.meter.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterData;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;

import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 * @author chandru.p
 * @change	parveen , rathish , sriram
 * 
 */
public final class ITAssetDiscoverer {

    private List<String> errorList = null;
    private GQErrorInformation gqErrInfo = null;
    private List<GQErrorInformation> gqerrorInfoList = null;
    private String gqmid = null;
    private Pattern pattern;
    private Matcher matcher;
    private Gson gson = new GsonBuilder().create();
    private GQMeterResponse gqmResponse = null;

    private LinkedList<String> snmpKnownIPList = null;
    private LinkedList<String> snmpUnknownIPList = null;
    private HashMap<MeterProtocols, LinkedList<String>> switches = new HashMap<MeterProtocols, LinkedList<String>>(4);

    Client client;
    WebResource service;

    public ITAssetDiscoverer() {
		super();

	    client = Client.create(new DefaultClientConfig());
	    service = client.resource(MeterUtils.restURL);
	}

	/**
     * This method used to find the asset type
     * 
     * @param communityIPMap
     * @return
     */
    private List<ProtocolData> findAssets(HashMap<String, String> communityIPMap) {

        String snmpVersion = null;
        String assetDesc = null;
        String communityString = null;
        String ipAddress = null;
        String snmpKnownIp = null;

        MeterProtocols mProtocol = null;
        GQMeterData assetObject = null;
        HashMap<String, String> snmpDetails = null;

        gqerrorInfoList = new LinkedList<GQErrorInformation>();
        List<ProtocolData> pdList = new LinkedList<ProtocolData>();
        snmpKnownIPList = new LinkedList<String>();
        snmpUnknownIPList = new LinkedList<String>();

        try {
            // Iterating the communityipmap
            for (Entry<String, String> entry : communityIPMap.entrySet()) {
                ipAddress = entry.getKey();
                communityString = entry.getValue();
                snmpDetails = new MeterUtils().isSnmpConfigured(communityString, ipAddress);
                if (snmpDetails != null && snmpDetails.size() != 0) {
                    snmpVersion = snmpDetails.get("snmpVersion");
                    assetDesc = snmpDetails.get("assetDesc");
                    snmpKnownIp = snmpDetails.get("snmpKnownIp");

                    if (snmpKnownIp != null) {
                        snmpKnownIPList.add(snmpKnownIp);
                        this.setSnmpKnownIPList(snmpKnownIPList);
                        mProtocol = MeterUtils.getAssetType(communityString, ipAddress, snmpVersion);
                        if (!mProtocol.equals(MeterProtocols.UNKNOWN)) {
                            assetObject = MeterUtils.getAssetObject(mProtocol, communityString, ipAddress, snmpVersion,
                                    switches.get(mProtocol));

                            if (assetObject == null) {
                                errorList = new LinkedList<String>();
                                errorList.add(ipAddress + " - " + mProtocol.name()
                                        + " : Cannot fetch the meter details");
                                gqerrorInfoList.add(new GQErrorInformation(assetDesc, errorList));
                            }
                            else {
                                pdList.add(new ProtocolData(mProtocol, gson.toJson(assetObject.getMeterData())));
                                if (assetObject.getErrorInformation() != null) {
                                    gqerrorInfoList.add(assetObject.getErrorInformation());
                                }
                            }
                        }// if ends
                        else {
                            errorList = new LinkedList<String>();
                            errorList.add(ipAddress + " - " + mProtocol.name() + " : Cannot find the asset type");
                            gqErrInfo = new GQErrorInformation(assetDesc, errorList);
                            gqerrorInfoList.add(gqErrInfo);
                        }// else ends
                    }
                    else {
                        snmpUnknownIPList.add(ipAddress);
                        this.setSnmpUnknownIPList(snmpUnknownIPList);
                        errorList = new LinkedList<String>();
                        errorList.add(communityString + " - " + ipAddress + " -  : Cannot reach the asset");
                        gqErrInfo = new GQErrorInformation(null, errorList);
                        gqerrorInfoList.add(gqErrInfo);
                    }
                }// if ends
            }// for loop ends
        }
        catch (Exception e) {
            System.out.println(" [GQMETER] Exception occured while processing the communityIpMap ");
            e.printStackTrace();
        }
        finally {
            gqmResponse.setAssetDiscovered((short) snmpKnownIPList.size());
            gqmResponse.setErrorInformationList(gqerrorInfoList); // Added the errors to the GQMResponse
        }

        return pdList;
    }

    /**
     * This method used to return the community and ip addresses read from the asset input file
     * 
     * @param inputFilePath
     * @return
     */
    private HashMap<String, String> readInput(String inputFilePath) {

        File assetsInputFile = null;
        HashMap<String, String> communityIPMap = new HashMap<String, String>();
        gqerrorInfoList = new LinkedList<GQErrorInformation>();
       // gqmResponse.setGqmid("GQMeterResponse");

        if ( inputFilePath == null || inputFilePath.trim().length() == 0) {
            System.out.println(" [GQMETER] Not a valid input file argument");
            System.exit(1);
        }

        try {
            assetsInputFile = new File(inputFilePath);

            if ( ! assetsInputFile.exists() || ! assetsInputFile.isFile()) {
                System.out.println(" [GQMETER] Unable to locate the input file");
                System.exit(1);
            }

            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            
            String line = null;
            boolean headerFound = false;
            int headerLinesCount = 0;
    		Map<String,Integer> switchOcc = new HashMap<String,Integer>(); // integer value has no significance. just using it for map features

            // process the header section
            while ( (line=br.readLine()) != null ) {
            	 
            	line = line.trim();
            	
                if (line.startsWith("#") || line.length() == 0) {
                    continue;
                }

            	if ( line.equals("$$")) {	// header line found
            		headerFound = true;	
            		break;
            	} 
            	// do the header processing
            	// now we expect only 5 lines with key value pairs , nothing if count is more than 5 we complain and get out
            	++headerLinesCount;
            	
            	// split the line to key value
            	String headerToken[] = line.split("\\s+");
            	
            	// lines must contain only 2 tokens
            	if ( headerToken.length != 2  ) {
                    System.out.println(" [GQMETER] Invalid header line ; wrong # of tokens ; line : "+ line);
                    System.exit(0);
            	}
            	
            	String keyy = headerToken[0].toLowerCase();
            	
            	// key can be one among the 5 keys only 
            	if ( keyy.equals(MeterConstants.METER_ID ) ) {
            		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            		if ( p.matcher(headerToken[1]).find() ) {
                        System.out.println(" [GQMETER] Meter has special char , Process Terminating Now ..");
                        System.exit(0);
            		}
            		gqmid =  headerToken[1];
           		}
            	else if ( keyy.equals(MeterConstants.COMPUTER_SWITCH)  || keyy.equals(MeterConstants.STORAGE_SWITCH) ) {
            		Map<String,Integer> tokenOcc = new HashMap<String,Integer>();
            		
            		// complain if the line starts or ends with a | symbol
            		if ( (headerToken[1].charAt(0) == '|') || ( headerToken[1].charAt(headerToken[1].length()-1) == '|') ) {
                        System.out.println(" [GQMETER] Switches cannot start or end with | chars , Process Terminating Now ..");
                        System.exit(0);
            		}

            		String switchToken[] = headerToken[1].split("\\|");
                    
                    LinkedList<String> switchList = new LinkedList<String>();
                    
                    for (String token : switchToken) {
                    	token = token.toLowerCase();

                    	if ( token.equals(MeterConstants.INSTALLED_SOFTWARE) || token.equals(MeterConstants.CONNECTED_DEVICES) || 
                    			token.equals(MeterConstants.PROCESS) || token.equals(MeterConstants.SNAPSHOT)	) {
                    		// good token
                    		if ( tokenOcc.containsKey(token) ) {
                                System.out.println(" [GQMETER] Computer/Storage switch duplicates found , Process Terminating Now ..");
                                System.exit(0);
                    		}
                    		tokenOcc.put(token, 0); // value doesnt matter , may be a list is enough for this - ss sep 4 , 2013
                    		// put it in switch map for findassets method to use
                    		switchList.add(token);
                    	}
                    	else {
                            System.out.println(" [GQMETER] Computer/Storage switch has invalid entry , Process Terminating Now ..");
                            System.exit(0);
                    	}

                    }   
                    if ( keyy.equals(MeterConstants.COMPUTER_SWITCH)  ) {
                		switches.put(MeterProtocols.COMPUTER, switchList);
                    }
                    else {
                		switches.put(MeterProtocols.STORAGE, switchList);
                    }
            	} // comp switch proc ends
            	else if ( keyy.equals(MeterConstants.PRINTER_SWITCH) || keyy.equals(MeterConstants.NSRG_SWITCH) ) {
            		
            		// complain if the line starts or ends with a | symbol
            		if ( (headerToken[1].charAt(0) == '|') || ( headerToken[1].charAt(headerToken[1].length()-1) == '|') ) {
                        System.out.println(" [GQMETER] Switches cannot start or end with | chars , Process Terminating Now ..");
                        System.exit(0);
            		}

            		Map<String,Integer> tokenOcc = new HashMap<String,Integer>();
            		String switchToken[] = headerToken[1].split("\\|");
                    LinkedList<String> switchList = new LinkedList<String>();

                    for (String token : switchToken) {
                    	token = token.toLowerCase();
                    	if (  token.equals(MeterConstants.CONNECTED_DEVICES)  || token.equals(MeterConstants.SNAPSHOT)	) {
                    		// good token
                    		if ( tokenOcc.containsKey(token) ) {
                                System.out.println(" [GQMETER] Printer/NSRG switch duplicates found , Process Terminating Now ..");
                                System.exit(0);
                    		}
                    		tokenOcc.put(token, 0); // value doesnt matter , may be a list is enough for this - ss sep 4 , 2013
                    		// put it in switch map for findassets method to use
                    		switchList.add(token);
                     	}
                    	else {
                            System.out.println(" [GQMETER] Printer/NSRG switch has invalid entry , Process Terminating Now ..");
                            System.exit(0);
                    	}
                    } 
                    if ( keyy.equals(MeterConstants.PRINTER_SWITCH)  ) {
                		switches.put(MeterProtocols.PRINTER, switchList);
                    }
                    else {
                		switches.put(MeterProtocols.NSRG, switchList);
                    }
            	}
            	else {
                    System.out.println(" [GQMETER] Invalid Header Section entry , Process Terminating Now ..");
                    System.exit(0);
            	}
            	
            	// put the meterid or switch thing in the map and check for duplicates
        		if ( switchOcc.containsKey(keyy) ) {
                    System.out.println(" [GQMETER] Switch duplicates found , Process Terminating Now ..");
                    System.exit(0);
        		}
            	switchOcc.put(keyy, 0);
            	
            } // while file read ends

       		if ( ! headerFound ) {
                System.out.println(" [GQMETER] No valid header delimeter found on Input Assets File , check manual..");
                System.exit(0);
       		}
            
       		if ( switchOcc.size() != 5) {
                System.out.println(" [GQMETER] All switches need to be present on Input Assets File , check manual..");
                System.exit(0);       			
       		}
       		
       		if ( ! (headerLinesCount == 5) ) {
                System.out.println(" [GQMETER] Invalid Header Section , 5 lines are reqd , Process Terminating Now ..");
                System.exit(0);
       		}

			// send it to gate keeper and check if it is good.
			isValid(gqmid);

			// *************************
            // process the data  section
            while ( ( line=br.readLine()) != null ) {
            	 
            	line = line.trim();
            	
                if (line.startsWith("#") || line.length() == 0) {
                    continue;
                }

            	// split the line to key value
            	String dataToken[] = line.split("\\s+");
            	
            	// key can be one among the 5 keys only 
            	if ( ( dataToken.length == 1 ) || (dataToken.length > 3) ) {
                    System.out.println(" [GQMETER] Invalid data line ; wrong # of tokens , continuing process .."+ line);
                    continue;
            	}

            	if ( dataToken.length == 2 ) {
            		if (! isIPAddressValid(dataToken[1])) {
                        System.out.println(" [GQMETER] Invalid ip address ; continuing process ; line :" + line);
                        continue;
            		}
            		else {
            			communityIPMap.put(dataToken[1], dataToken[0]);	
            		}
            	}
            	else if ( dataToken.length == 3 ) {
            		if ( isIPAddressValid(dataToken[1])  && isIPAddressValid(dataToken[2]) ) {
                        // to check the lowerboundip is greater than the upperboundip
                        if (MeterUtils.ipComparator.compare(dataToken[1], dataToken[2]) == -1) {
                            communityIPMap.put(dataToken[1], dataToken[0]);
                            // Here we iterate the ip range and find & add those intermediate ips to map
                            while (!(dataToken[1] = MeterUtils.nextIpAddress(dataToken[1])) .equals(dataToken[2])) {
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
        finally {
            try {
            	gqmResponse.setAssetScanned((short) communityIPMap.size());
                gqmResponse.setErrorInformationList(gqerrorInfoList); // Added the errors to the GQMResponse
                return communityIPMap; // returns not null map with all the asset objects value
            }
            catch (Exception e) {
                System.out.println(" [GQMETER] Exception occured while closing the buffer after reading : " + e);
            }
        }
        
        return communityIPMap;
    }

    private String isValid(String gqmid) {
        try {
            System.out.println(" [GQMETER] Validating the expiry date for the meter " + gqmid);
            ClientResponse response = service.path("metercheck").queryParam("meterId", gqmid).post(ClientResponse.class);
            
            String resp = response.getEntity(String.class).trim();
            String resp1 = resp.substring(1, 6);
            String protocolId = resp.substring(6, (resp.length() - 1));
            
            if (!MeterConstants.PROTOCOL_ID.equals(protocolId)) {
                MeterConstants.PROTOCOL_ID = protocolId;
            }
            
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
        return gqmid;
    }

    private boolean isIPAddressValid(String ipAddress) {
        pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public void discover(String inputFilePath) {

        System.out.println(" [GQMETER] started successfully.......");
        gqmResponse = new GQMeterResponse();
        gqmResponse.setRecDttm(new Date());// -------------------------->test this

        // The start time of the meter execution
        long startTime = System.currentTimeMillis();

        List<ProtocolData> assetsList = null;

        HashMap<String, String> communityIPMap = this.readInput(inputFilePath);
        
        if (communityIPMap.size() > 0) {
            assetsList = findAssets(communityIPMap);
            gqmResponse.addToAssetInformationList(assetsList);
        }
        else {
            System.out.println(" [GQMETER] There are no valid IPAddresses given , Process Terminated..");
            System.exit(0);
        }

        long endTime = System.currentTimeMillis();
        gqmResponse.setRunTimeMiliSeconds((endTime - startTime));
        gqmResponse.setStatus("pass");
        gqmResponse.setVersion("1");
        gqmResponse.setGqmid(gqmid);

        System.out.println(" [GQMETER] JSON : " + gson.toJson(gqmResponse));
        System.out.println(" [GQMETER] Total number of assets(ip address) in input file : " + gqmResponse.getAssetScanned());
        System.out.println(" [GQMETER] SNMP configured on : " + this.getSnmpKnownIPList().toString());
        System.out.println(" [GQMETER] SNMP not configured on : " + this.getSnmpUnknownIPList().toString());
        System.out.println(" [GQMETER] SNMP walk succeess count is : " + this.getSnmpKnownIPList().size());
        System.out.println(" [GQMETER] TOTAL taken for meter execution : " + (endTime - startTime));
        
        // Sending the generated json output to the server
        Form form = new Form();
        form.add("gqMeterResponse", gson.toJson(gqmResponse));
        form.add("summary", "Sending the data from GQMeter to GQGatekeeper");
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

    /**
     * @return the snmpKnownIPList
     */
    public LinkedList<String> getSnmpKnownIPList() {
        return snmpKnownIPList;
    }

    /**
     * @param snmpKnownIPList the snmpKnownIPList to set
     */
    public void setSnmpKnownIPList(LinkedList<String> snmpKnownIPList) {
        this.snmpKnownIPList = snmpKnownIPList;
    }

    /**
     * @return the snmpUnknownIPList
     */
    public LinkedList<String> getSnmpUnknownIPList() {
        return snmpUnknownIPList;
    }

    /**
     * @param snmpUnknownIPList the snmpUnknownIPList to set
     */
    public void setSnmpUnknownIPList(LinkedList<String> snmpUnknownIPList) {
        this.snmpUnknownIPList = snmpUnknownIPList;
    }
}
