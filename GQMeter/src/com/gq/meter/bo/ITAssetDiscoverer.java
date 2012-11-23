package com.gq.meter.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.object.GQMeterData;
import com.gq.meter.object.ProtocolData;
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

        LinkedList<Object> deadAsset = new LinkedList<Object>(); // Planned to send dead assets to the user
        LinkedList<Object> liveAsset = new LinkedList<Object>(); // Planned to send dead assets to the user
        HashMap<String, LinkedList<Object>> assetsMap = new HashMap<String, LinkedList<Object>>();
        Object assetObject = null;
        String asset = null;

        List<ProtocolData> pdList = new LinkedList<ProtocolData>();

        if (null != communityString && null != ipLowerbound) {
            try {

                String currIp = ipLowerbound;
                System.out.println("********************* ip = " + currIp + "*******************");
                HashMap<String, Object> assetType = null;
                CommunityTarget target = null;

                assetType = MeterUtils.isSnmpConfigured(communityString, currIp);
                if (assetType != null && assetType.size() != 0 && !assetType.isEmpty()) {
                    target = (CommunityTarget) assetType.get("target");
                    asset = assetType.get("asset").toString();
                    assetObject = MeterUtils.getAssetObject(asset, communityString, currIp, target);
                    pdList.add(new ProtocolData(asset, gson.toJson(assetObject)));
                    liveAsset.add(assetObject);
                }
                else {
                    System.out.println("Adding asset to the dead asset list");
                    deadAsset.add(currIp);
                }

                if (null != ipUpperbound) {
                    while (!(currIp = MeterUtils.nextIpAddress(currIp)).equals(ipUpperbound)) {
                        System.out.println("********************* next ip = " + currIp + "*******************");
                        assetType = MeterUtils.isSnmpConfigured(communityString, currIp);
                        if (assetType != null && assetType.size() != 0 && !assetType.isEmpty()) {
                            target = (CommunityTarget) assetType.get("target");
                            asset = assetType.get("asset").toString();
                            assetObject = MeterUtils.getAssetObject(asset, communityString, currIp, target);
                            pdList.add(new ProtocolData(asset, gson.toJson(assetObject)));
                            // liveAsset.add(assetObject);
                        }
                        else {
                            System.out.println("Adding asset to the dead asset list");
                            deadAsset.add(currIp);
                        }
                    }

                    currIp = ipUpperbound;
                    System.out.println("********************* next ip = " + currIp + "*******************");
                    assetType = MeterUtils.isSnmpConfigured(communityString, currIp);
                    if (assetType != null && assetType.size() != 0 && !assetType.isEmpty()) {
                        target = (CommunityTarget) assetType.get("target");
                        asset = assetType.get("asset").toString();
                        assetObject = MeterUtils.getAssetObject(asset, communityString, currIp, target);
                        pdList.add(new ProtocolData(asset, gson.toJson(assetObject)));
                        // liveAsset.add(assetObject);
                    }
                    else {
                        System.out.println("Adding asset to the dead asset list");
                        deadAsset.add(currIp);
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Exception occured while finding the assets : " + e);
            }
            finally {
                assetsMap.put("liveAssets", liveAsset);
                assetsMap.put("deadAssets", deadAsset);
            }
        }

        return pdList;
    }

    /**
     * The file shoud be kept in a place where we look for it and read.
     * 
     * @param inputFile
     */
    private GQMeterData readInput(String inputFilePath) {
        BufferedReader br = null;
        String line = null;
        String assetaArray[] = null;
        String communityString = null;
        String ipUpperbound = null;
        String ipLowerbound = null;
        List<ProtocolData> assetsList = null;

        GQMeterData gqmData = new GQMeterData();

        gqmData.setGqmid("GQITMeter1");

        if (null != inputFilePath && inputFilePath.trim() != "") {
            try {
                File assetsInputFile = new File(inputFilePath);

                if (assetsInputFile.exists() && assetsInputFile.isFile()) {
                    br = new BufferedReader(new FileReader(assetsInputFile));

                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (null == line || line.length() == 0 || line.contains("#")) {
                            continue;
                        }
                        else if (line.contains(" ")) {
                            assetaArray = line.split(" ");
                            if (assetaArray.length == 2) {
                                communityString = assetaArray[0];
                                ipLowerbound = assetaArray[1];
                                ipUpperbound = null;
                                assetsList = findassets(communityString, ipLowerbound, ipUpperbound);
                            }
                            else if (assetaArray.length == 3) {
                                communityString = assetaArray[0];
                                ipLowerbound = assetaArray[1];
                                ipUpperbound = assetaArray[2];
                                assetsList = findassets(communityString, ipLowerbound, ipUpperbound);
                            }
                        }

                        gqmData.addToAssetInformationList(assetsList);
                    }
                }
                else {
                    System.out.println("Exception occured : Unable locate the input file for processing");
                }
            }
            catch (Exception e) {
                System.out.println("Exception occured : Unable locate the input file for processing : " + e);
            }
            finally {
                try {
                    if (null != br) {
                        br.close();
                    }
                    return gqmData; // returns not null map with all the asset objects value
                }
                catch (IOException e) {
                    System.out.println("Exception occured while closing the buffer after reading : " + e);
                }
            }
        }
        return gqmData;
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
        String inputFilePath = "C:\\Users\\chandru.p\\AssetDetails.txt";
        GQMeterData assetsMap = itad.readInput(inputFilePath);
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        System.out.println("json of GQMeterData = " + itad.gson.toJson(assetsMap)
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
