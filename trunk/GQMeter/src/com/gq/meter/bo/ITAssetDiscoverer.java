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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterData;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author chandru.p
 * 
 */
public class ITAssetDiscoverer {

    List<String> errorList = null;
    GQErrorInformation gqErrInfo = null;
    List<GQErrorInformation> gqerrorInfoList = null;

    // Gson gson = new GsonBuilder().create();
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private GQMeterResponse gqmResponse = new GQMeterResponse();
    static HashMap<String, String> communityIPMap = new HashMap<String, String>();
    HashMap<MeterProtocols, LinkedList<String>> switches = new HashMap<MeterProtocols, LinkedList<String>>();

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

        MeterProtocols mProtocol = null;
        GQMeterData assetObject = null;
        HashMap<String, String> snmpDetails = null;
        gqerrorInfoList = new LinkedList<GQErrorInformation>();
        List<ProtocolData> pdList = new LinkedList<ProtocolData>();

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
                    mProtocol = MeterUtils.getAssetType(communityString, ipAddress, snmpVersion);

                    if (!mProtocol.equals(MeterProtocols.UNKNOWN)) {
                        assetObject = MeterUtils.getAssetObject(mProtocol, communityString, ipAddress, snmpVersion,
                                switches.get(mProtocol));

                        if (assetObject == null) {
                            errorList = new LinkedList<String>();
                            errorList.add(ipAddress + " - " + mProtocol.name() + " : Cannot fetch the meter details");
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
                }// if ends
                else {
                    errorList = new LinkedList<String>();
                    errorList.add(ipAddress + " -  : Cannot reach the asset");
                    gqErrInfo = new GQErrorInformation(null, errorList);
                    gqerrorInfoList.add(gqErrInfo);
                }
            }// for loop ends
        }
        catch (Exception e) {
            System.out.println("Exception occured while processing the communityIpMap ");
            e.printStackTrace();
        }
        finally {
            gqmResponse.setErrorInformationList(gqerrorInfoList); // Added the errors to the GQMResponse
        }
        return pdList;
    }

    /**
     * @param inputFilePath
     * @return
     */
    private GQMeterResponse readInput(String inputFilePath) {

        InputStream assetFileStream = null;
        String communityString = null;
        String ipUpperbound = null;
        String ipLowerbound = null;
        List<ProtocolData> assetsList = null;
        StringTokenizer sToken = null;
        File assetsInputFile = null;

        gqerrorInfoList = new LinkedList<GQErrorInformation>();
        gqmResponse.setGqmid("GQMeterResponse");

        if (null != inputFilePath && inputFilePath.trim() != "") {
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

                        // line ! starts with @ for switches
                        if (line.startsWith("@")) {
                            MeterUtils.manageSwitches(line, switches);
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

                                        // /Here we iterate the ip range and find & add those intermediate ips to the
                                        // map
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

                    if (communityIPMap.size() > 0) {
                        assetsList = findassets(communityIPMap);
                        gqmResponse.addToAssetInformationList(assetsList);
                    }
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
                    gqmResponse.setErrorInformationList(gqerrorInfoList); // Added the errors to the GQMResponse

                    return gqmResponse; // returns not null map with all the asset objects value
                }
                catch (Exception e) {
                    System.out.println("Exception occured while closing the buffer after reading : " + e);
                }
            }
        }
        return gqmResponse;
    }

    // implement a power meter , this involves 3 distinct steps
    // 1.gather data to make the power protocol object
    // 2.make the json
    // 3.make a https connection to the gate keeper url to send it to the server
    public void implement(Object o) {
        // use the following for pretty print.. atleast for testing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // use the following for normal print.
        // Gson gson = new Gson();
        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(o);
        System.out.println("===== json is ======");
        System.out.println(json);

    }

    public static void main(String[] args) throws IOException {
        // The start time of the meter execution
        long startTime = System.currentTimeMillis();

        if (args.length != 1) {
            System.out.println("Usage : ASSETDETAILS_FILE_PATH");
            System.exit(1);
        }
        ITAssetDiscoverer itad = new ITAssetDiscoverer();
        String inputFilePath = args[0].trim();

        GQMeterResponse gqmResponse = itad.readInput(inputFilePath);
        long endTime = System.currentTimeMillis();

        gqmResponse.setAssetScanned((short) communityIPMap.size());
        gqmResponse.setAssetDiscovered((short) MeterUtils.snmpKnownIPList.size());
        gqmResponse.setRecDttm(new Date());
        gqmResponse.setRunTimeMiliSeconds((endTime - startTime));
        gqmResponse.setStatus("pass");
        gqmResponse.setVersion("1");

        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println("json of GQMeterData = " + itad.gson.toJson(gqmResponse));
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        // The end time of the meter execution

        System.out.println("Total number of assets taken after processing the inputfile : " + communityIPMap.size());

        System.out.println("Total time taken to know snmp is installed on the devices : " + MeterUtils.snmpKnownTime);
        System.out.println("Total time taken to know snmp is not installed on the devices : "
                + MeterUtils.snmpUnknownTime);

        System.out.println("The list of snmp configured devices : " + MeterUtils.snmpKnownIPList.toString());
        System.out.println("The list of no snmp configured devices : " + MeterUtils.snmpUnknownIPList.toString());
        System.out.println("Total number of assets processed : " + MeterUtils.snmpKnownIPList.size());
        System.out.println("Total number of assets not processed : " + MeterUtils.snmpUnknownIPList.size());

        System.out.println("Total time taken for all COMPUTER meters : " + MeterUtils.compMeterTime);
        System.out.println("Total time taken for all PRINTER meters : " + MeterUtils.printMeterTime);
        System.out.println("Total time taken for all ISR meters : " + MeterUtils.isrMeterTime);
        System.out.println("TOTAL duration taken for meter execution : " + (endTime - startTime));

        // Sending the generated json output to the server
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(MeterUtils.restURL);

        com.sun.jersey.api.representation.Form form = new com.sun.jersey.api.representation.Form();
        form.add("gqMeterResponse", itad.gson.toJson(gqmResponse));
        form.add("summary", "Demonstration of the client lib for forms");
        ClientResponse response = service.path("gqm-gk").path("gatekeeper")
                .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, form);
        // System.out.println("Form response " + response.getEntity(String.class));
    }
}
