package com.gq.meter.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

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
 * 
 */
public final class ITAssetDiscoverer {

    private List<String> errorList = null;
    private GQErrorInformation gqErrInfo = null;
    private List<GQErrorInformation> gqerrorInfoList = null;
    private static String gqmid = null;

    private Gson gson = new GsonBuilder().create();
    // public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private GQMeterResponse gqmResponse = null;
    private LinkedList<String> snmpKnownIPList = null;
    private LinkedList<String> snmpUnknownIPList = null;

    private HashMap<MeterProtocols, LinkedList<String>> switches = new HashMap<MeterProtocols, LinkedList<String>>();

    /**
     * This method used to find the asset type
     * 
     * @param communityIPMap
     * @return
     */
    private List<ProtocolData> findassets(HashMap<String, String> communityIPMap) {

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

                System.out.println("********************************************************************");
                System.out.println("CommunityString : " + communityString + " - Ipaddress : " + ipAddress);

                snmpDetails = MeterUtils.isSnmpConfigured(communityString, ipAddress);

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
                        errorList.add(ipAddress + " -  : Cannot reach the asset");
                        gqErrInfo = new GQErrorInformation(null, errorList);
                        gqerrorInfoList.add(gqErrInfo);
                    }
                }// if ends
            }// for loop ends
        }
        catch (Exception e) {
            System.out.println("Exception occured while processing the communityIpMap ");
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

        InputStream assetFileStream = null;
        String communityString = null;
        String ipUpperbound = null;
        String ipLowerbound = null;
        StringTokenizer sToken = null;
        File assetsInputFile = null;

        HashMap<String, String> communityIPMap = new HashMap<String, String>();
        gqerrorInfoList = new LinkedList<GQErrorInformation>();
        gqmResponse.setGqmid("GQMeterResponse");

        if (null != inputFilePath && inputFilePath.trim().length() != 0) {
            try {
                assetsInputFile = new File(inputFilePath);

                if (assetsInputFile.exists() && assetsInputFile.isFile()) {

                    assetFileStream = new FileInputStream(assetsInputFile);
                    String inputAssets = MeterUtils.read(assetFileStream);

                    if (inputAssets == null || inputAssets.trim().length() == 0) {
                        System.out.println("*** The asset file is empty ***");
                        System.exit(1);
                    }// if ends

                    String assetLines[] = inputAssets.trim().replace("\t", " ").split("\n");

                    for (String line : assetLines) {
                        line = line.trim();

                        if (line.length() == 0 || line.startsWith("#")) {
                            continue;
                        }
                        // line starts with $ for meterid
                        if (line.startsWith("$")) 
                        {
                            if (line.toLowerCase().startsWith(MeterConstants.METER_ID)) {
                                gqmid = line.replace(MeterConstants.METER_ID, "").trim();
                                
                                if(gqmid.isEmpty())
                                {
                                System.out.println("Meter Id is empty");
                                System.out.println("..............Check Meter Id .............");
                                System.exit(0);
                                }
                            }
                        }
                        // line starts with @ for switches
                        else if (line.startsWith("@")) 
                        {
                        	if(gqmid!=null)
                        	{
                        		String s_name=line.substring(1,line.indexOf(" "));
                            	String v_name=line.substring(line.indexOf(" ")+1,line.length());
                            	
                            	if(s_name.equalsIgnoreCase(MeterConstants.COMPUTER_PROTOCOL)||s_name.equalsIgnoreCase(MeterConstants.PRINTER_PROTOCOL)||s_name.equalsIgnoreCase(MeterConstants.NSRG_PROTOCOL))
                            	{
                            		
                            		if(v_name.equalsIgnoreCase(MeterConstants.FULL_DETAILS)||v_name.equalsIgnoreCase(MeterConstants.CONNECTED_DEVICES)||v_name.equalsIgnoreCase(MeterConstants.SNAPSHOT)||v_name.equalsIgnoreCase(MeterConstants.PROCESS)||v_name.equalsIgnoreCase(MeterConstants.INSTALLED_SOFTWARE)){
                            		MeterUtils.manageSwitches(line, switches);
                            		}
                            		else {
                            		MeterUtils.count++;
                            		//System.out.println("Count2:"+MeterUtils.count);
                            		}
                            	}
                            	else{
                            		//MeterUtils.count++;
                            		//System.out.println("Count1:"+MeterUtils.count);
                            	}
                        	}
                        	else
                        	{
                        		System.out.println("Check Meter Id...");
                        		System.out.println("Process stopped........");
                        		System.exit(0);
                        	}
                        }
                        else {
                            sToken = new StringTokenizer(line, " ");

                             if (sToken.countTokens() == 2 || sToken.countTokens() == 3) {
                                communityString = sToken.nextToken().trim();
                                ipLowerbound = sToken.nextToken().trim();

                                if (sToken.hasMoreTokens()) {
                                    ipUpperbound = sToken.nextToken().trim();

                                    // This condition used to check the lowerboundip is smaller than the upperboundip
                                    if (MeterUtils.ipComparator.compare(ipLowerbound, ipUpperbound) == -1) {
                                        communityIPMap.put(ipLowerbound, communityString);

                                        // Here we iterate the ip range and find & add those intermediate ips to the map
                                        while (!(ipLowerbound = MeterUtils.nextIpAddress(ipLowerbound))
                                                .equals(ipUpperbound)) {
                                            communityIPMap.put(ipLowerbound, communityString);
                                        }

                                        communityIPMap.put(ipUpperbound, communityString);
                                    }
                                    else {
                                        System.out.println("IP lower bound : " + ipLowerbound + "IP upper bound : "
                                                + ipUpperbound + " -  : Invalid ip range");
                                        errorList = new LinkedList<String>();
                                        errorList.add(sToken + " -  : Invalid ip range");
                                        gqErrInfo = new GQErrorInformation("Invalid asset ip range", errorList);
                                        gqerrorInfoList.add(gqErrInfo);
                                    }
                                }// if ends
                                else {
                                    communityIPMap.put(ipLowerbound, communityString);
                                }
                            }// if ends
                            else {
                                System.out.println("INVALID : entry -" + line);
                                System.out.println("Usage : COMMUNITY_STRING IP_LOWER_BOUND IP_UPPER_BOUND");
                            }
                        }// else ends
                    }// for loop ends
                }// if ends
                else {
                    System.out.println("Exception occured : Unable to locate the input file");
                    System.exit(1);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception occured : Unable to locate the input file for processing : " + e);
            }
            finally {
                try {
                    if (null != assetFileStream) {
                        assetFileStream.close();
                    }
                    gqmResponse.setAssetScanned((short) communityIPMap.size());
                    gqmResponse.setErrorInformationList(gqerrorInfoList); // Added the errors to the GQMResponse

                    return communityIPMap; // returns not null map with all the asset objects value
                }
                catch (Exception e) {
                    System.out.println("Exception occured while closing the buffer after reading : " + e);
                }
            }
        }
        return communityIPMap;
    }

    public void discover(String inputFilePath) {
        gqmResponse = new GQMeterResponse();
        gqmResponse.setRecDttm(new Date());// -------------------------->test this

        // The start time of the meter execution
        long startTime = System.currentTimeMillis();
        MeterUtils.compMeterTime = 0;
        MeterUtils.printMeterTime = 0;
        MeterUtils.nsrgMeterTime = 0;

        List<ProtocolData> assetsList = null;

        HashMap<String, String> communityIPMap = this.readInput(inputFilePath);
        if (communityIPMap.size() > 0) {
            assetsList = findassets(communityIPMap);
            gqmResponse.addToAssetInformationList(assetsList);
        }
        long endTime = System.currentTimeMillis();

        gqmResponse.setRunTimeMiliSeconds((endTime - startTime));
        gqmResponse.setStatus("pass");
        gqmResponse.setVersion("1");
        gqmResponse.setGqmid(gqmid);

        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println("json of GQMeterData = " + gson.toJson(gqmResponse));
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        // The end time of the meter execution

        System.out.println("Total number of assets taken after processing the inputfile : "
                + gqmResponse.getAssetScanned());

        System.out.println("Total time taken to know snmp is installed on the devices : " + MeterUtils.snmpKnownTime);
        System.out.println("Total time taken to know snmp is not installed on the devices : "
                + MeterUtils.snmpUnknownTime);

        System.out.println("The list of snmp configured devices : " + this.getSnmpKnownIPList().toString());
        System.out.println("The list of no snmp configured devices : " + this.getSnmpUnknownIPList().toString());
        System.out.println("Total number of assets processed : " + this.getSnmpKnownIPList().size());
        System.out.println("Total number of assets not processed : " + this.getSnmpUnknownIPList().size());

        System.out.println("Total time taken for all COMPUTER meters : " + MeterUtils.compMeterTime);
        System.out.println("Total time taken for all PRINTER meters : " + MeterUtils.printMeterTime);
        System.out.println("Total time taken for all NSRG meters : " + MeterUtils.nsrgMeterTime);
        System.out.println("TOTAL duration taken for meter execution : " + (endTime - startTime));

        // Sending the generated json output to the server
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource service = client.resource(MeterUtils.restURL);
        service = service.path("gqm-gk").path("gatekeeper");
        System.out.println("Service:"+service);	
        Form form = new Form();
        form.add("gqMeterResponse", gson.toJson(gqmResponse));
        form.add("summary", "Sending the data from GQMeter to GQGatekeeper");

        Builder builder = service.type(MediaType.APPLICATION_JSON);
        ClientResponse response = builder.post(ClientResponse.class, form);
      //  System.out.println("Form response " + response.getEntity(String.class));
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage : ASSETDETAILS_FILE_PATH");
            System.exit(1);
        }
        ITAssetDiscoverer itad = new ITAssetDiscoverer();
        String inputFilePath = args[0].trim();
        itad.discover(inputFilePath);
        
        System.out.println("Input File PATH:"+inputFilePath);
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
