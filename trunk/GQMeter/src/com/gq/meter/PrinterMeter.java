package com.gq.meter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.Printer;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

public class PrinterMeter implements GQSNMPMeter {

    static HashMap<Integer, String> printerOperationalStateMap = new HashMap<Integer, String>();
    static HashMap<Integer, String> printerCurrentStateMap = new HashMap<Integer, String>();
    static HashMap<Integer, String> printerErrorConditionMap = new HashMap<Integer, String>();

    List<String> errorList = new LinkedList<String>();

    static {
        // Predefined printer status map

        printerOperationalStateMap.put(1, "Unknown");
        printerOperationalStateMap.put(2, "Running");
        printerOperationalStateMap.put(3, "Warning");
        printerOperationalStateMap.put(4, "Testing");
        printerOperationalStateMap.put(5, "Down");

        printerCurrentStateMap.put(1, "Other");
        printerCurrentStateMap.put(2, "Unknown");
        printerCurrentStateMap.put(3, "Idle");
        printerCurrentStateMap.put(4, "Printing");
        printerCurrentStateMap.put(5, "WarmUp");

        printerErrorConditionMap.put(0, "No Error");
        printerErrorConditionMap.put(2, "Warming up");
        printerErrorConditionMap.put(8, "Cartridge Removed");
        printerErrorConditionMap.put(11, "Paper Out");
        printerErrorConditionMap.put(12, "Open or no EP");
        printerErrorConditionMap.put(13, "Paper Jam");
        printerErrorConditionMap.put(14, "No paper cart Or no toner cart");
        printerErrorConditionMap.put(16, "Toner low");
        printerErrorConditionMap.put(18, "MIO not ready");
        printerErrorConditionMap.put(20, "Memory OverFlow");
        printerErrorConditionMap.put(21, "Printer Over run");
        printerErrorConditionMap.put(22, "EIO configuration Error");
        printerErrorConditionMap.put(23, "I/O not ready");
        printerErrorConditionMap.put(24, "Job memory full");
        printerErrorConditionMap.put(30, "PS error");
        printerErrorConditionMap.put(40, "Data transfer error");
        printerErrorConditionMap.put(41, "Temporary print engine failure");
        printerErrorConditionMap.put(49, "A Communication or critical Firmware Error");
    }

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches) {

        long printerStartTime = System.currentTimeMillis();
        Snmp snmp = null;

        // ASSET
        String assetId = null; // unique identifier about the asset
        String sysName = null;
        String sysDescr = null;
        String sysContact = null;
        String sysLocation = null;

        // SNAPSHOT
        String sysIP = null;
        String errorCondition = null;
        String operationalState = null;
        String currentState = null;
        String mfgModel = null;
        String isColorPrinter = null;
        String extras = null; // anything device specific but to be discussed , v2

        long upTime = 0; // seconds
        long outOfPaperIndicator = 0; // 0 means no paper , v2
        long printsTakenCount = 0; // v2
        double tonerPercentage = 0;
        long totalMemory = 0; // bytes
        long usedMemory = 0; // bytes
        long totalDiskSpace = 0; // bytes
        long usedDiskSpace = 0; // bytes

        CommunityTarget target = null;
        HashMap<String, String> printerStatus;
        List<String> errorList = new LinkedList<String>();
        HashSet<String> connectedDevices = null;

        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
            target = MeterUtils.makeTarget(ipAddress, communityString, snmpVersion);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // The following oid's is used to get system basic info
        try {
            String oidString = "1.3.6.1.2.1.1";
            String temp;
            String tempStr;

            OID rootOID = new OID(oidString);
            List<VariableBinding> result = null;

            result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is v2
            if (result != null && !result.isEmpty()) {

                temp = oidString + ".1.0";
                sysDescr = MeterUtils.getSNMPValue(temp, result);

                temp = oidString + ".4.0";
                sysContact = MeterUtils.getSNMPValue(temp, result);

                temp = oidString + ".5.0";
                sysName = MeterUtils.getSNMPValue(temp, result);

                temp = oidString + ".6.0";
                sysLocation = MeterUtils.getSNMPValue(temp, result);
            }
            else {
                errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + MeterConstants.STANDARD_SYSTEM_ATTRIBUTES_ERROR);
            }

            // The below oid's is used to get the asset id
            oidString = ".1.3.6.1.2.1.2.2.1";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            if (result != null && !result.isEmpty()) {
                String assetVal = assetCalc(result, rootOID);
                assetId = MeterProtocols.PRINTER + "-" + assetVal;
            }
            else {
                errorList.add("Root OID : .1.3.6.1.2.1.2.2.1" + " " + MeterConstants.ASSET_ID_ERROR);
            }

            for (String element : toggleSwitches) {
                if (element.equalsIgnoreCase(MeterConstants.FULL_DETAILS)
                        || element.equalsIgnoreCase(MeterConstants.SNAPSHOT)) {
                    sysIP = ipAddress;

                    oidString = "1.3.6.1.2.1.1";
                    rootOID = new OID(oidString);

                    result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is
                                                               // v2
                    if (result != null && !result.isEmpty()) {
                        temp = oidString + ".3.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        upTime = MeterUtils.upTimeCalc(tempStr);
                    }

                    // The below oid's is used to determine color printer
                    oidString = "1.3.6.1.2.1.43.11.1.1.6";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        if (result.size() <= 2) {
                            isColorPrinter = MeterConstants.BLACKWHITE_PRINTER;
                        }
                        else {
                            isColorPrinter = MeterConstants.COLOR_PRINTER;
                        }
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.43.11.1.1.6" + " "
                                + "Unable to determine if printer is a color printer");
                    }

                    // The following oid's is used to get the errorCondition, operationalState , currentState ,
                    // mfgMakeAndModel
                    oidString = "1.3.6.1.2.1.25.3";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        printerStatus = printerStatusCalc(result, rootOID);
                        currentState = printerStatus.get("currentState");
                        operationalState = printerStatus.get("operationalState");
                        errorCondition = printerStatus.get("errorCondition");
                        temp = oidString + ".2.1.3.1";
                        mfgModel = MeterUtils.getSNMPValue(temp, result);
                    }
                    else {
                        errorList
                                .add("Root OID : 1.3.6.1.2.1.25.3 - Unable to determine manufacture model, current state and operational state of the printer");
                    }

                    // The below oid's is used to get the toner percentage
                    oidString = ".1.3.6.1.2.1.43.11.1.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        tonerPercentage = tonerPercentageCalc(result, rootOID);
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.43.11.1.1" + " " + "Unable to get the toner percentage");
                    }

                    oidString = "1.3.6.1.2.1.25.2.3.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        String OID = null;

                        OID = "1.3.6.1.2.1.25.2.1.2";
                        totalMemory = MemHardDiscCalc(result, rootOID, OID, false);
                        usedMemory = MemHardDiscCalc(result, rootOID, OID, true);

                        OID = "1.3.6.1.2.1.25.2.1.4";
                        long mainTotalDiskSpace = MemHardDiscCalc(result, rootOID, OID, false);
                        long mainUsedDiskSpace = MemHardDiscCalc(result, rootOID, OID, true);

                        OID = "1.3.6.1.2.1.25.2.1.9";
                        long subTotalDiskSpace = MemHardDiscCalc(result, rootOID, OID, false);
                        long subusedDiskSpace = MemHardDiscCalc(result, rootOID, OID, true);

                        totalDiskSpace = mainTotalDiskSpace + subTotalDiskSpace;
                        usedDiskSpace = mainUsedDiskSpace + subusedDiskSpace;
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.25.2.3.1 - Unable to get disk and memory details");
                    }
                }

                // the following oid's is used to get the ip and port no of devices that is connected.
                if (element.equalsIgnoreCase(MeterConstants.FULL_DETAILS)
                        || element.equalsIgnoreCase(MeterConstants.CONNECTED_DEVICES)) {

                    if (result != null && !result.isEmpty()) {
                        oidString = ".1.3.6.1.2.1.6.13.1.1";
                        rootOID = new OID(oidString);
                        result = MeterUtils.walk(rootOID, target);
                        connectedDevices = ConnectedDevicesCalc(result, ipAddress);
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.6.13.1.1" + " "
                                + "Unable to get port number and ip address of connected devices");
                    }

                }
            }// for loop ends
        }
        catch (Exception e) {
            errorList.add(ipAddress + " " + e.getMessage());
        }

        Printer printerObject = new Printer(assetId, upTime, tonerPercentage, outOfPaperIndicator, printsTakenCount,
                sysName, sysIP, sysDescr, sysContact, sysLocation, errorCondition, operationalState, currentState,
                mfgModel, isColorPrinter, totalMemory, totalDiskSpace, usedMemory, usedDiskSpace, connectedDevices,
                extras);

        GQErrorInformation gqErrorInfo = null;

        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(sysDescr, errorList);
        }

        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, printerObject);
        long printerEndTime = System.currentTimeMillis();
        MeterUtils.printMeterTime = MeterUtils.printMeterTime + (printerEndTime - printerStartTime);
        System.out.println("Time taken bye the printer meter is : " + (printerEndTime - printerStartTime));
        return gqMeterObject;
    }

    /**
     * @param result
     * @param rootOid
     * @param OID
     * @param isUsed
     * @return
     */
    private long MemHardDiscCalc(List<VariableBinding> result, OID rootOid, String OID, boolean isUsed) {
        String mulBytes = null;
        String mulBytesOID = null;
        String toMultiply = null;
        String toMultiplyOID = null;
        String usedMultiplyOID = null;
        String usedMultiply = null;
        String rootId = rootOid.toString();
        Long memHdd = 0L;

        for (VariableBinding vb : result) {
            String temp = vb.getVariable().toString().trim();

            if (temp.trim().equals(OID)) {
                String lastValue = String.valueOf(vb.getOid().last());
                mulBytesOID = rootId + "." + "4" + "." + lastValue;

                if (!isUsed) {
                    toMultiplyOID = rootId + "." + "5" + "." + lastValue;
                }
                else {
                    usedMultiplyOID = rootId + "." + "6" + "." + lastValue;
                }

                for (VariableBinding vbs : result) {
                    if (vbs.getOid().toString().trim().equals(mulBytesOID)) {
                        mulBytes = vbs.getVariable().toString().trim();
                    }
                    else if (!isUsed && vbs.getOid().toString().trim().equals(toMultiplyOID)) {
                        toMultiply = vbs.getVariable().toString().trim();
                    }
                    else if (isUsed && vbs.getOid().toString().trim().equals(usedMultiplyOID)) {
                        usedMultiply = vbs.getVariable().toString().trim();
                    }
                }// for loop ends

                if (!isUsed && mulBytes != null && !mulBytes.trim().isEmpty() && toMultiply != null
                        && !toMultiply.trim().isEmpty()) {
                    memHdd = memHdd + Long.parseLong(mulBytes.trim()) * Long.parseLong(toMultiply.trim());
                }
                else if (isUsed && mulBytes != null && !mulBytes.trim().isEmpty() && usedMultiply != null
                        && !usedMultiply.trim().isEmpty()) {
                    memHdd = memHdd + Long.parseLong(mulBytes.trim()) * Long.parseLong(usedMultiply.trim());
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + memHdd);
                }
            }
        }
        return memHdd;
    }

    /**
     * @param result
     * @param rootOid
     * @return
     */
    private double tonerPercentageCalc(List<VariableBinding> result, OID rootOid) {

        String totalValue = null;
        String remainingUsage = null;
        double totalValueFloat = 0L;
        double remainingUsagefloat = 0L;
        double finalTonerPercentage = 0L;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) {

            totalValue = rootId + ".8.1.1";
            remainingUsage = rootId + ".9.1.1";

            if (totalValue != null && vb.getOid().toString().equals(totalValue)) {

                String totalValuestr = vb.getVariable().toString().trim();
                if (!totalValuestr.trim().isEmpty() && totalValuestr != null) {
                    totalValueFloat = Double.parseDouble(totalValuestr);
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + " " + totalValueFloat);
                }
            }
            else if (remainingUsage != null && vb.getOid().toString().equals(remainingUsage)) {

                String remainingUsageStr = vb.getVariable().toString().trim();
                if (!remainingUsageStr.trim().isEmpty() && remainingUsageStr != null) {
                    remainingUsagefloat = Double.parseDouble(remainingUsageStr);
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + " " + remainingUsagefloat);
                }
            }

        } // for loop ends
        double finalTonerPercentageCalc = (remainingUsagefloat / totalValueFloat) * 100;
        DecimalFormat df = new DecimalFormat("#0.00");
        String finalTonerPercentageStr = df.format(finalTonerPercentageCalc);
        finalTonerPercentage = Double.parseDouble(finalTonerPercentageStr);
        return finalTonerPercentage;
    }

    /**
     * @param result
     * @param rootOid
     * @return
     */
    private HashMap<String, String> printerStatusCalc(List<VariableBinding> result, OID rootOid) {

        String operationalStateKey = "operationalState";
        String currentStateKey = "currentState";
        String errorConditionKey = "errorCondition";
        String currentStatus = null;
        String operationalStatus = null;
        String errorConditionalStatus = null;
        String rootId = rootOid.toString();
        String currentStateOid = rootId + ".5.1.1.1";
        String operationalStateOid = rootId + ".2.1.5.1";
        String errorConditionOid = rootId + ".5.1.2.1";
        String N_A = "Not Avaiable";

        for (VariableBinding vb : result) {
            if (currentStateOid != null && !currentStateOid.trim().isEmpty()
                    && vb.getOid().trim().toString().equals(currentStateOid)) {
                String currentStateValueStr = vb.getVariable().toString().trim();

                if (!currentStateValueStr.trim().isEmpty() && currentStateValueStr != null) {
                    int currentStateValue = Integer.parseInt(currentStateValueStr);
                    // check in the predefined map whether the map has the value
                    if (printerCurrentStateMap.containsKey(currentStateValue)) {
                        currentStatus = currentStateValue + " " + "-" + " "
                                + printerCurrentStateMap.get(currentStateValue);
                    }
                    else {
                        currentStatus = currentStateValue + " " + "-" + " " + N_A;
                    }
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + currentStatus);
                }
            }
            else if (operationalStateOid != null && !operationalStateOid.trim().isEmpty()
                    && vb.getOid().trim().toString().equals(operationalStateOid)) {
                String operationalStateValueStr = vb.getVariable().toString().trim();

                if (!operationalStateValueStr.trim().isEmpty() && operationalStateValueStr != null) {
                    int operationalStateValue = Integer.parseInt(operationalStateValueStr);
                    // check in the predefined map whether the map has the value
                    if (printerOperationalStateMap.containsKey(operationalStateValue)) {
                        operationalStatus = operationalStateValue + " " + "-" + " "
                                + printerOperationalStateMap.get(operationalStateValue);
                    }
                    else {
                        operationalStatus = operationalStateValue + " " + "-" + " " + N_A;
                    }
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + operationalStatus);
                }

            }

            else if (errorConditionOid != null && !errorConditionOid.trim().isEmpty()
                    && vb.getOid().trim().toString().equals(errorConditionOid)) {
                String errorConditionValueStr = vb.getVariable().toString().trim();

                if (!errorConditionValueStr.trim().isEmpty() && errorConditionValueStr != null) {
                    int errorConditionValue = Integer.parseInt(errorConditionValueStr);
                    // check in the predefined map whether the map has the value
                    if (printerErrorConditionMap.containsKey(errorConditionValue)) {
                        errorConditionalStatus = errorConditionValue + " " + "-" + " "
                                + printerErrorConditionMap.get(errorConditionValue);
                    }

                    else {
                        errorConditionalStatus = errorConditionValue + " " + "-" + " " + N_A;
                    }
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + errorConditionalStatus);
                }
            }
        }
        // return the status of the printer with currentState and operationalState
        HashMap<String, String> printerStatus = new HashMap<String, String>();

        printerStatus.put(currentStateKey, currentStatus); // current
        printerStatus.put(operationalStateKey, operationalStatus); // operational
        printerStatus.put(errorConditionKey, errorConditionalStatus); // errorCondition
        return printerStatus;
    }

    /**
     * @param result
     * @param rootOid
     * @return
     */
    private String assetCalc(List<VariableBinding> result, OID rootOid) {
        String rootId = rootOid.toString();
        String lastchar = null;
        String assetOid = null;
        String assetStr = null;

        for (VariableBinding vb : result) {
            if (vb.getVariable().toString().trim().equals("6")) {
                lastchar = String.valueOf(vb.getOid().last());
                assetOid = rootId + ".6" + "." + lastchar;

            }
            if (assetOid != null && vb.getOid().toString().trim().equals(assetOid)) {
                assetStr = vb.getVariable().toString().trim().replaceAll(":", "");

            }
        }
        return assetStr;
    }

    /**
     * @param result
     * @param ipAddress
     * @return
     */
    private HashSet<String> ConnectedDevicesCalc(List<VariableBinding> result, String ipAddress) {

        HashSet<String> connectedDevices = new HashSet<String>();

        for (VariableBinding vb : result) {
            String expectedStr = vb.getVariable().toString();
            if (expectedStr != null && vb.getOid().toString().contains(expectedStr)
                    && expectedStr.equalsIgnoreCase("5")) {

                String targetOID = vb.getOid().toString();
                String[] preFinalOID = targetOID.toString().split("\\.");
                String one = preFinalOID[15];
                String two = preFinalOID[16];
                String three = preFinalOID[17];
                String four = preFinalOID[18];
                String Final = one + "." + two + "." + three + "." + four;

                if (!Final.trim().equals(ipAddress) && !Final.trim().equals("0.0.0.0")
                        && !Final.trim().equals("127.0.0.1")) {
                    connectedDevices.add(Final);
                }

            }
        }
        return connectedDevices;
    }
}