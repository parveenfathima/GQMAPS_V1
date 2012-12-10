package com.gq.meter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
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

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion) {
        long computerstartTime = System.currentTimeMillis();
        Snmp snmp = null;
        String assetId = null; // unique identifier about the asset
        String sysName = null;
        String sysIP = null;
        String sysDescr = null;
        String sysContact = null;
        String sysLocation = null;
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

        sysIP = ipAddress;
        CommunityTarget target = null;
        HashMap<String, String> printerStatus;
        List<String> errorList = new LinkedList<String>();

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

        String oidString = "1.3.6.1.2.1.1";
        String temp;
        String tempStr;

        OID rootOID = new OID(oidString);
        List<VariableBinding> result = null;

        result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is v2
        if (result != null && !result.isEmpty()) {

            temp = oidString + ".1.0";
            sysDescr = MeterUtils.getSNMPValue(temp, result);

            temp = oidString + ".3.0";
            tempStr = MeterUtils.getSNMPValue(temp, result);
            upTime = MeterUtils.upTimeCalc(tempStr);

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
        // The following oid's is used to get the errorCondition, operationalState , currentState , mfgMakeAndModel

        oidString = "1.3.6.1.2.1.25.3";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            temp = oidString + ".5.1.2.1";
            errorCondition = MeterUtils.getSNMPValue(temp, result);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.25.3" + " " + "Unable to determine printer error condition");
        }

        oidString = "1.3.6.1.2.1.25.3";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            printerStatus = printerStatusCalc(result, rootOID);
            currentState = printerStatus.get("currentState");
            operationalState = printerStatus.get("operationalState");

            temp = oidString + ".2.1.3.1";
            mfgModel = MeterUtils.getSNMPValue(temp, result);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.25.3" + " "
                    + "Unable to determine manufacture model, current state and operational state of the printer");
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

        Printer printerObject = new Printer(assetId, upTime, tonerPercentage, outOfPaperIndicator, printsTakenCount,
                sysName, sysIP, sysDescr, sysContact, sysLocation, errorCondition, operationalState, currentState,
                mfgModel, isColorPrinter, extras);

        GQErrorInformation gqErrorInfo = null;

        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(sysDescr, errorList);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, printerObject);
        long computerendTime = System.currentTimeMillis();
        System.out.println("Time taken bye the printer meter is : " + (computerendTime - computerstartTime));
        return gqMeterObject;
    }

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
                totalValueFloat = Double.parseDouble(totalValuestr);
            }
            else if (remainingUsage != null && vb.getOid().toString().equals(remainingUsage)) {

                String remainingUsageStr = vb.getVariable().toString().trim();
                remainingUsagefloat = Double.parseDouble(remainingUsageStr);
            }

        } // for loop ends
        double finalTonerPercentageCalc = (remainingUsagefloat / totalValueFloat) * 100;
        DecimalFormat df = new DecimalFormat("#0.00");
        String finalTonerPercentageStr = df.format(finalTonerPercentageCalc);
        finalTonerPercentage = Double.parseDouble(finalTonerPercentageStr);
        return finalTonerPercentage;
    }

    private HashMap<String, String> printerStatusCalc(List<VariableBinding> result, OID rootOid) {

        String operationalStateKey = "operationalState";
        String currentStateKey = "currentState";
        String currentStatus = null;
        String operationalStatus = null;
        String rootId = rootOid.toString();
        String currentStateOid = rootId + ".5.1.1.1";
        String operationalStateOid = rootId + ".2.1.5.1";
        String N_A = "Not Avaiable";

        // Predefined printer status map
        HashMap<String, String> printerOperationalStateMap = new HashMap<String, String>();
        printerOperationalStateMap.put("1", "Unknown");
        printerOperationalStateMap.put("2", "Running");
        printerOperationalStateMap.put("3", "Warning");
        printerOperationalStateMap.put("4", "Testing");
        printerOperationalStateMap.put("5", "Down");

        HashMap<String, String> printerCurrentStateMap = new HashMap<String, String>();
        printerCurrentStateMap.put("1", "Other");
        printerCurrentStateMap.put("2", "Unknown");
        printerCurrentStateMap.put("3", "Idle");
        printerCurrentStateMap.put("4", "Printing");
        printerCurrentStateMap.put("5", "WarmUp");

        for (VariableBinding vb : result) {
            if (currentStateOid != null && vb.getOid().toString().equals(currentStateOid)) {
                String currentStateValue = vb.getVariable().toString().trim();
                // check in the predefined map whether the map has the value
                if (printerCurrentStateMap.containsKey(currentStateValue)) {
                    currentStatus = printerCurrentStateMap.get(currentStateValue);
                }
                else {
                    currentStatus = N_A;
                }

            }
            else if (operationalStateOid != null && vb.getOid().toString().equals(operationalStateOid)) {
                String operationalStateValue = vb.getVariable().toString().trim();
                // check in the predefined map whether the map has the value
                if (printerOperationalStateMap.containsKey(operationalStateValue)) {
                    operationalStatus = printerOperationalStateMap.get(operationalStateValue);
                }
                else {
                    operationalStatus = N_A;
                }
            }
        }
        // return the status of the printer with currentState and operationalState
        HashMap<String, String> printerStatus = new HashMap<String, String>();

        printerStatus.put(currentStateKey, currentStatus); // current
        printerStatus.put(operationalStateKey, operationalStatus); // operational
        return printerStatus;
    }

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
}