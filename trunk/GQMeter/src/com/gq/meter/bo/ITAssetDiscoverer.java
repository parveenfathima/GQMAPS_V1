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
 * 
 */
public final class ITAssetDiscoverer {

    private List<String> errorList = null;
    private GQErrorInformation gqErrInfo = null;
    private List<GQErrorInformation> gqerrorInfoList = null;
    private String gqmid = null;
    private Pattern pattern;
    private Matcher matcher;
    private int meterCount = 0;
    private int computerCount = 0;
    private int printerCount = 0;
    private int nsrgCount = 0;
    private int storageCount = 0;
    private Gson gson = new GsonBuilder().create();
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

                    if (!inputAssets.contains("$$")) {
                        System.out.println(" [GQMETER] No valid delimeter on Input Assets Section..");
                        System.out.println(" [GQMETER] Process Terminated Now..");
                        System.exit(0);
                    }
                    String headerAssetsPart = inputAssets.substring(0, inputAssets.indexOf("$$"));
                    String snmpAssetsPart = inputAssets.substring(inputAssets.indexOf("$$") + 2, inputAssets.length());

                    if (inputAssets == null || inputAssets.trim().length() == 0) {
                        System.out.println(" [GQMETER] *** The asset file is empty ***");
                        System.exit(1);
                    }// if ends

                    String headerAssetLines[] = headerAssetsPart.trim().replace("\t", " ").split("\n");
                    String snmpAssetLines[] = snmpAssetsPart.trim().replace("\t", " ").split("\n");

                    // Validation code for Header Section
                    for (String line : headerAssetLines) {
                        if (line.startsWith(MeterConstants.METER_ID)
                                || line.startsWith(MeterConstants.COMPUTER_SWITCHS)
                                || line.startsWith(MeterConstants.PRINTER_SWITCHS)
                                || line.startsWith(MeterConstants.NSRG_SWITCHS)
                                || line.startsWith(MeterConstants.STORAGE_SWITCHS) || line.startsWith("#")
                                || line.trim().length() == 0) {
                            if (line.startsWith(MeterConstants.METER_ID)) {
                                meterCount++;
                            }
                            if (line.startsWith(MeterConstants.COMPUTER_SWITCHS)) {
                                computerCount++;
                            }
                            if (line.startsWith(MeterConstants.PRINTER_SWITCHS)) {
                                printerCount++;
                            }
                            if (line.startsWith(MeterConstants.NSRG_SWITCHS)) {
                                nsrgCount++;
                            }
                            if (line.startsWith(MeterConstants.STORAGE_SWITCHS)) {
                                storageCount++;
                            }
                        }// outer if ends
                         // else part for Invalid Header
                        else {
                            System.out.println(" [GQMETER] Invalid Header Section..");
                            System.out.println(" [GQMETER] Process Terminated Now..");
                            System.exit(0);
                        }
                    }
                    // Check the default lines present or not.
                    if (!(meterCount == 1 && computerCount == 1 && printerCount == 1 && nsrgCount == 1 && storageCount == 1)) {
                        System.out.println(" [GQMETER] Invalid Header Section..");
                        System.out.println(" [GQMETER] Process Terminated Now..");
                        System.exit(0);
                    }
                    // Read the Header Section
                    for (String line : headerAssetLines) {
                        // line starts with # for Ignore the lines
                        if (line.trim().length() == 0 || line.startsWith("#")) {
                            continue;
                        }
                        // line starts with $ for meterid
                        if (line.startsWith("$")) {
                            if (line.toLowerCase().startsWith(MeterConstants.METER_ID)) {
                                gqmid = line.replace(MeterConstants.METER_ID, "").trim();
                                // This Condition used to check meter id value is empty or not
                                if (gqmid.isEmpty()) {
                                    System.out.println(" [GQMETER] Meter Id is empty");
                                    System.out.println(" [GQMETER] ..............Check Meter Id .............");
                                    System.exit(0);
                                }
                                isValid(gqmid);
                            }
                        }
                        // line starts with @ for switches
                        else if (line.startsWith("@")) {
                            line = line.trim();
                            sToken = new StringTokenizer(line, " ");
                            if (gqmid != null) {
                                if (sToken.countTokens() == 2) {
                                    line = line.toLowerCase();
                                    String switchName = line.substring(0, line.indexOf(" "));
                                    String switchValueName = line.replace(switchName, "").trim();
                                    // Check if whether valid switches and values or not
                                    if (switchValueName.contains(MeterConstants.FULL_DETAILS)
                                            || switchValueName.contains(MeterConstants.CONNECTED_DEVICES)
                                            || switchValueName.contains(MeterConstants.SNAPSHOT)
                                            || switchValueName.contains(MeterConstants.PROCESS)
                                            || switchValueName.contains(MeterConstants.INSTALLED_SOFTWARE)) {
                                        new MeterUtils().manageSwitches(line, switches);
                                    }
                                    // else part for Invalid Switch value
                                    else {
                                        System.out.println(" [GQMETER] Invalid Switch Value..");
                                        System.out.println(" [GQMETER] Line:" + line);
                                        System.out.println(" [GQMETER] Process terminated Now....");
                                        System.exit(0);
                                    }
                                }// Token count if ends
                                 // else part for empty switch value
                                else {
                                    System.out.println(" [GQMETER] Check Switch Value is empty....");
                                    System.out.println(" [GQMETER] Process should be stopped now.....");
                                    System.exit(0);
                                }
                            }// 1st if ends
                             // else part for empty meter id
                            else {
                                System.out.println(" [GQMETER] Check Meter Id...");
                                System.out.println(" [GQMETER] Process stopped........");
                                System.exit(0);
                            }
                        } // else if ends
                    }
                    // Read the snmpAssets Section
                    for (String line : snmpAssetLines) {
                        line = line.trim();
                        // line starts with # for Ignore the lines
                        if (line.length() == 0 || line.startsWith("#")) {
                            continue;
                        }
                        else {
                            sToken = new StringTokenizer(line, " ");
                            if (sToken.countTokens() == 2 || sToken.countTokens() == 3) {
                                communityString = sToken.nextToken().trim();
                                ipLowerbound = sToken.nextToken().trim();
                                if (sToken.hasMoreTokens()) {
                                    ipUpperbound = sToken.nextToken().trim();
                                    if (isIPAddressValid(ipLowerbound) && isIPAddressValid(ipUpperbound)) {
                                        // to check the lowerboundip is greater than the upperboundip
                                        if (MeterUtils.ipComparator.compare(ipLowerbound, ipUpperbound) == -1) {
                                            communityIPMap.put(ipLowerbound, communityString);
                                            // Here we iterate the ip range and find & add those intermediate ips to map
                                            while (!(ipLowerbound = MeterUtils.nextIpAddress(ipLowerbound))
                                                    .equals(ipUpperbound)) {
                                                communityIPMap.put(ipLowerbound, communityString);
                                            }
                                            communityIPMap.put(ipUpperbound, communityString);
                                        }
                                        // to check the lowerboundip and ipupperboundip as same
                                        else if (MeterUtils.ipComparator.compare(ipLowerbound, ipUpperbound) == 0) {
                                            communityIPMap.put(ipLowerbound, communityString);
                                        }
                                        // else part for Invalid IP range
                                        else {
                                            System.out.println(" [GQMETER] IP lower bound : " + ipLowerbound
                                                    + "IP upper bound : " + ipUpperbound + " -  : Invalid ip range");
                                            System.exit(0);
                                        }
                                    }
                                    // else part for Invalid Upperbound and Lowerbound
                                    else if (isIPAddressValid(ipLowerbound)) {
                                        System.out.println(" [GQMETER] INVALID UPPERBOUNDIP:" + ipUpperbound);
                                        communityIPMap.put(ipLowerbound, communityString);
                                    }
                                    else if (isIPAddressValid(ipUpperbound)) {
                                        System.out.println(" [GQMETER] INVALID LOWERBOUNDIP:" + ipLowerbound);
                                        communityIPMap.put(ipUpperbound, communityString);
                                    }
                                    else {
                                        System.out.println(" [GQMETER] INVALID UPPERBOUNDIP AND LOWERBOUNDIP:");
                                        System.out.println(" [GQMETER] " + ipLowerbound + "   " + ipUpperbound);
                                    }
                                }// if ends
                                 // if line contains Lowerbound only
                                else {
                                    if (isIPAddressValid(ipLowerbound)) {
                                        communityIPMap.put(ipLowerbound, communityString);
                                    }
                                    else {
                                        System.out.println(" [GQMETER] Invalid IPAddress:" + ipLowerbound);
                                        continue;
                                    }
                                }
                            }// if ends
                             // Missing community string or Ipaddress
                            else {
                                System.out.println(" [GQMETER] INVALID : entry -" + line);
                            }
                        } // else ends
                    }// for loop ends
                }// if ends
                 // else part for asset file missing
                else {
                    System.out.println(" [GQMETER] Exception occured : Unable to locate the input file");
                    System.exit(1);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println(" [GQMETER] Exception occured : Unable to locate the input file for processing : "
                        + e);
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
                    System.out.println(" [GQMETER] Exception occured while closing the buffer after reading : " + e);
                }
            }
        }
        return communityIPMap;
    }

    private String isValid(String gqmid) {
        try {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource service = client.resource(MeterUtils.restURL);
            service = service.path("gqm-gk").path("metercheck");
            System.out.println(" [GQMETER] Validating the expiry date for the meter " + gqmid);
            ClientResponse response = service.queryParam("meterId", gqmid).post(ClientResponse.class);
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
        new MeterUtils().compMeterTime = 0;
        new MeterUtils().printMeterTime = 0;
        new MeterUtils().nsrgMeterTime = 0;
        List<ProtocolData> assetsList = null;

        HashMap<String, String> communityIPMap = this.readInput(inputFilePath);
        if (communityIPMap.size() > 0) {
            assetsList = findassets(communityIPMap);
            gqmResponse.addToAssetInformationList(assetsList);
        }
        else {
            System.out.println(" [GQMETER] Process Terminated.....");
            System.exit(0);
        }

        long endTime = System.currentTimeMillis();
        gqmResponse.setRunTimeMiliSeconds((endTime - startTime));
        gqmResponse.setStatus("pass");
        gqmResponse.setVersion("1");
        gqmResponse.setGqmid(gqmid);

        System.out.println(" [GQMETER] Total number of assets(ip address) in input file : "
                + gqmResponse.getAssetScanned());
        System.out.println(" [GQMETER] List of snmp configured devices : " + this.getSnmpKnownIPList().toString());
        System.out.println(" [GQMETER] SNMP not configured on : " + this.getSnmpUnknownIPList().toString());
        System.out.println(" [GQMETER] SNMP walk succeeded count is : " + this.getSnmpKnownIPList().size());
        System.out.println(" [GQMETER] TOTAL duration taken for meter execution : " + (endTime - startTime));
        System.out.println(" [GQMETER] ended successfully ....");
        // Sending the generated json output to the server
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(MeterUtils.restURL);
        service = service.path("gqm-gk").path("gatekeeper");
        Form form = new Form();
        form.add("gqMeterResponse", gson.toJson(gqmResponse));
        form.add("summary", "Sending the data from GQMeter to GQGatekeeper");
        Builder builder = service.type(MediaType.APPLICATION_JSON);
        ClientResponse response = builder.post(ClientResponse.class, form);
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
