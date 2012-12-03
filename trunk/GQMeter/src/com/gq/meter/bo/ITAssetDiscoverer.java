package com.gq.meter.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private List<ProtocolData> findassets(String communityString, String ipLowerbound, String ipUpperbound) {

        Object assetObject = null;
        HashMap<String, String> snmpDetails = null;
        String snmpVersion = null;
        String assetDesc = null;
        MeterProtocols mProtocol = null;

        List<ProtocolData> pdList = new LinkedList<ProtocolData>();
        try {
            if (null != communityString && null != ipLowerbound) {
                try {
                    String currIp = ipLowerbound;
                    System.out.println("********************* ip = " + currIp + "*******************");

                    snmpDetails = MeterUtils.isSnmpConfigured(communityString, currIp);
                    if (snmpDetails != null && snmpDetails.size() != 0 && !snmpDetails.isEmpty()) {
                        snmpVersion = snmpDetails.get("snmpVersion");
                        assetDesc = snmpDetails.get("assetDesc");
                        mProtocol = MeterUtils.getAssetType(communityString, currIp, snmpVersion);

                        if (!mProtocol.equals(MeterProtocols.UNKNOWN)) {
                            assetObject = MeterUtils.getAssetObject(mProtocol, communityString, currIp, snmpVersion);
                            pdList.add(new ProtocolData(mProtocol, gson.toJson(assetObject)));
                        }
                        else {
                            pdList.add(new ProtocolData(mProtocol, assetDesc));
                        }

                    }

                    if (null != ipUpperbound) {
                        while (!(currIp = MeterUtils.nextIpAddress(currIp)).equals(ipUpperbound)) {
                            System.out.println("********************* next ip = " + currIp + "*******************");
                            snmpDetails = MeterUtils.isSnmpConfigured(communityString, currIp);
                            if (snmpDetails != null && snmpDetails.size() != 0 && !snmpDetails.isEmpty()) {
                                snmpVersion = snmpDetails.get("snmpVersion");
                                assetDesc = snmpDetails.get("assetDesc");
                                mProtocol = MeterUtils.getAssetType(communityString, currIp, snmpVersion);

                                if (!mProtocol.equals(MeterProtocols.UNKNOWN)) {
                                    assetObject = MeterUtils.getAssetObject(mProtocol, communityString, currIp,
                                            snmpVersion);
                                    pdList.add(new ProtocolData(mProtocol, gson.toJson(assetObject)));
                                }
                                else {
                                    pdList.add(new ProtocolData(mProtocol, assetDesc));
                                }
                            }
                        }

                        currIp = ipUpperbound;
                        System.out.println("********************* next ip = " + currIp + "*******************");
                        snmpDetails = MeterUtils.isSnmpConfigured(communityString, currIp);
                        if (snmpDetails != null && snmpDetails.size() != 0 && !snmpDetails.isEmpty()) {
                            snmpVersion = snmpDetails.get("snmpVersion");
                            assetDesc = snmpDetails.get("assetDesc");
                            mProtocol = MeterUtils.getAssetType(communityString, currIp, snmpVersion);

                            if (!mProtocol.equals(MeterProtocols.UNKNOWN)) {
                                assetObject = MeterUtils
                                        .getAssetObject(mProtocol, communityString, currIp, snmpVersion);
                                pdList.add(new ProtocolData(mProtocol, gson.toJson(assetObject)));
                            }
                            else {
                                pdList.add(new ProtocolData(mProtocol, assetDesc));
                            }
                        }
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception occured while finding the assets : " + e);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return pdList;
    }

    /**
     * The file shoud be kept in a place where we look for it and read.
     * 
     * @param inputFile
     */
    private GQMeterResponse readInput(String inputFilePath) {
        BufferedReader br = null;
        String line = null;
        String communityString = null;
        String ipUpperbound = null;
        String ipLowerbound = null;
        List<ProtocolData> assetsList = null;

        GQMeterResponse gqmResponse = new GQMeterResponse();

        gqmResponse.setGqmid("GQMeterResponse");

        if (null != inputFilePath && inputFilePath.trim() != "") {
            try {
                File assetsInputFile = new File(inputFilePath);

                if (assetsInputFile.exists() && assetsInputFile.isFile()) {
                    br = new BufferedReader(new FileReader(assetsInputFile));
                    StringTokenizer sToken = null;

                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.length() == 0 || line.startsWith("#")) {
                            continue;
                        }
                        if (line.contains("\t")) {
                            line = line.replace("\t", " ");
                            sToken = new StringTokenizer(line, " ");
                        }
                        else {
                            sToken = new StringTokenizer(line, " ");
                        }
                        if (sToken.countTokens() == 2) {
                            communityString = sToken.nextToken().trim();
                            ipLowerbound = sToken.nextToken().trim();
                            ipUpperbound = null;
                            assetsList = findassets(communityString, ipLowerbound, ipUpperbound);
                        }
                        else if (sToken.countTokens() == 3) {
                            communityString = sToken.nextToken().trim();
                            ipLowerbound = sToken.nextToken().trim();
                            ipUpperbound = sToken.nextToken().trim();
                            assetsList = findassets(communityString, ipLowerbound, ipUpperbound);
                        }
                        gqmResponse.addToAssetInformationList(assetsList);
                    }
                }
                else {
                    System.out.println("Exception occured : Unable locate the input file for processing");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception occured : Unable locate the input file for processing : " + e);
            }
            finally {
                try {
                    if (null != br) {
                        br.close();
                    }
                    return gqmResponse; // returns not null map with all the asset objects value
                }
                catch (IOException e) {
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

        ITAssetDiscoverer itad = new ITAssetDiscoverer();
        String inputFilePath = "C:\\AssetDetails.txt";
        GQMeterResponse gqmResponse = itad.readInput(inputFilePath);
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        System.out.println("json of GQMeterData = " + itad.gson.toJson(gqmResponse)
                + " :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
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
