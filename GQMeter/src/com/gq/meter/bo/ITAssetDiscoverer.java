package com.gq.meter.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.assist.ProtocolData;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

/**
 * @author chandru.p
 * 
 */
public class ITAssetDiscoverer {

    /**
     * This method used to find the asset type
     * 
     * @param communityString
     * @param ipLowerbound
     * @param ipUpperbound
     * @return
     */
    // Gson gson = new GsonBuilder().create();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private GQMeterResponse gqmResponse = new GQMeterResponse();

    private List<ProtocolData> findassets(List<HashMap<String, LinkedList<String>>> assetsInputList) {

        Object assetObject = null;
        HashMap<String, String> snmpDetails = null;
        String snmpVersion = null;
        String assetDesc = null;
        MeterProtocols mProtocol = null;
        String communityString = null;
        String ipAddress = null;
        List<String> errorList = null;
        GQErrorInformation gqErrInfo = null;
        List<GQErrorInformation> gqerrorInfoList = new LinkedList<GQErrorInformation>();

        List<ProtocolData> pdList = new LinkedList<ProtocolData>();
        try {

            for (HashMap<String, LinkedList<String>> map : assetsInputList) {

                for (Entry<String, LinkedList<String>> entry : map.entrySet()) {
                    communityString = entry.getKey();

                    for (String ipaddresses : entry.getValue()) {
                        ipAddress = ipaddresses;
                        System.out.println("********************************************************************");
                        System.out.println("CommunityString : " + communityString + " - Ipaddress : " + ipAddress);
                        snmpDetails = MeterUtils.isSnmpConfigured(communityString, ipAddress);
                        if (snmpDetails != null && snmpDetails.size() != 0 && !snmpDetails.isEmpty()) {
                            snmpVersion = snmpDetails.get("snmpVersion");
                            assetDesc = snmpDetails.get("assetDesc");
                            mProtocol = MeterUtils.getAssetType(communityString, ipAddress, snmpVersion);

                            if (!mProtocol.equals(MeterProtocols.UNKNOWN)) {
                                assetObject = MeterUtils.getAssetObject(mProtocol, communityString, ipAddress,
                                        snmpVersion);
                                pdList.add(new ProtocolData(mProtocol, gson.toJson(assetObject)));
                            }
                            else {
                                errorList = new LinkedList<String>();
                                errorList.add(ipAddress + " - " + mProtocol.name() + " : Can't to find the asset type");
                                gqErrInfo = new GQErrorInformation(assetDesc, errorList);
                                gqerrorInfoList.add(gqErrInfo);
                            }

                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            gqmResponse.setErrorInformationList(gqerrorInfoList); // Added the errors to the GQMResponse
        }

        return pdList;
    }

    /**
     * The file shoud be kept in a place where we look for it and read.
     * 
     * @param inputFile
     */
    private GQMeterResponse readInput(String inputFilePath) {
        InputStream assetFileStream = null;
        String communityString = null;
        String ipUpperbound = null;
        String ipLowerbound = null;
        List<ProtocolData> assetsList = null;
        StringTokenizer sToken = null;
        File assetsInputFile = null;
        LinkedList<String> ipList = null;
        HashMap<String, LinkedList<String>> communityIPMap = null;
        List<HashMap<String, LinkedList<String>>> assetsInputList = new LinkedList<HashMap<String, LinkedList<String>>>();

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
                    }
                    String assetLines[] = inputAssets.trim().split("\n");

                    for (String line : assetLines) {
                        line = line.trim();
                        if (line.length() == 0 || line.startsWith("#")) {
                            continue;
                        }
                        line = line.replace("\t", " ");
                        sToken = new StringTokenizer(line, " ");
                        if (sToken.countTokens() == 2 || sToken.countTokens() == 3) {
                            communityString = sToken.nextToken().trim();
                            ipLowerbound = sToken.nextToken().trim();

                            ipList = new LinkedList<String>();
                            ipList.add(ipLowerbound);
                            if (sToken.hasMoreTokens()) {
                                ipUpperbound = sToken.nextToken().trim();
                                while (!(ipLowerbound = MeterUtils.nextIpAddress(ipLowerbound)).equals(ipUpperbound)) {
                                    ipList.add(ipLowerbound);
                                }
                                ipList.add(ipUpperbound);
                            }
                            communityIPMap = new HashMap<String, LinkedList<String>>();
                            communityIPMap.put(communityString, ipList);
                            assetsInputList.add(communityIPMap);
                        }
                        else {
                            System.out.println("INVALID : entry -" + line);
                            System.out.println("Usage : COMMUNITY_STRING IP_LOWER_BOUND IP_UPPER_BOUND");
                        }
                    }
                    assetsList = findassets(assetsInputList);
                    gqmResponse.addToAssetInformationList(assetsList);
                }
                else {
                    System.out.println("Exception occured : Unable to locate the input file");
                    System.exit(1);
                }
            }
            catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("Exception occured : Unable to locate the input file for processing : " + e);
            }
            finally {
                try {
                    if (null != assetFileStream) {
                        assetFileStream.close();
                    }
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
        Long startTime = System.currentTimeMillis();
        if (args.length != 2) {
            System.out.println("Usage : ASSETDETAILS_FILE_PATH REST_URL");
            System.exit(1);
        }
        ITAssetDiscoverer itad = new ITAssetDiscoverer();
        String inputFilePath = args[0].trim();
        String restUrl = args[1].trim();
        GQMeterResponse gqmResponse = itad.readInput(inputFilePath);
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println("json of GQMeterData = " + itad.gson.toJson(gqmResponse));
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        long endTime = System.currentTimeMillis();
        System.out.println("Total duration taken : " + (endTime - startTime));
        /*
         * Computer c = new Computer("sdfsad", 83, 100, 80, 30, 20, 500, 340, 89330, 7, 48, 10000, 22220, 2.34, "name",
         * "192.149.4.4", "desc", "contact", "location", "extras", new Date());
         * 
         * Gson gson = new GsonBuilder().create();
         * 
         * GQMeterData pd = new GQMeterData("ss", new Date() ,"power" , "v1", "sstatis", "no comment dude" ,
         * gson.toJson(c));
         * 
         * ComputerMeter cm = new ComputerMeter();
         * 
         * cm.implement(pd);
         * 
         * String json1 = gson.toJson(pd);
         * 
         * GQMeterData pd1 = gson.fromJson(json1, GQMeterData.class);
         * 
         * System.out.println("===== unmarshalled pd comment is ===== " + pd1.getComment());
         * 
         * Computer c1 = gson.fromJson(pd1.getData(), Computer.class);
         * 
         * System.out.println("===== unmarshalled computer ip is ===== "+ c1.getSysIP());
         */
    }
}
