package com.gq.meter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.Printer;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

public class PrinterMeter implements GQSNMPMeter {

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion) {

        Snmp snmp = null;

        String assetId = null; // unique identifier about the asset
        String sysName = null;
        String sysIP = null; // string
        String sysDescr = null;
        String sysContact = null;
        String sysLocation = null; // string
        String errorCondition = null;
        String operationalState = null; // for this and the next value , we maintain a table ; need to work more on it.
        String currentState = null;
        String mfgModel = null; // v2
        String isColorPrinter = null;
        String extras = null; // anything device specific but to be discussed , v2

        long uptime = 0; // seconds
        long outOfPaperIndicator = 0; // 0 means no paper , v2
        long networkBytesIn = 0; // bytes , v2
        long networkBytesOut = 0; // bytes , v2
        long printsTakenCount = 0; // v2

        double tonerPercentage = 0;

        HashMap<String, String> printerStatus;

        sysIP = ipAddress;

        CommunityTarget target = null;

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
            System.out.println("result : " + result);

            temp = oidString + ".1.0";
            sysDescr = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Description : " + sysDescr);

            temp = oidString + ".3.0";
            tempStr = MeterUtils.getSNMPValue(temp, result);
            // System.out.println("Uptime : " + tempStr);

            uptime = MeterUtils.upTimeCalc(tempStr);
            System.out.println("Uptime : " + uptime);

            temp = oidString + ".4.0";
            sysContact = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Contact : " + sysContact);

            temp = oidString + ".5.0";
            sysName = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Name : " + sysName);

            temp = oidString + ".6.0";
            sysLocation = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Location : " + sysLocation);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + "Basic info of a asset gets failed");
        }
        // The below oid's is used to get the toner percentage

        oidString = ".1.3.6.1.2.1.43.11.1.1";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            tonerPercentage = tonerPercentageCalc(result, rootOID);
            System.out.println("Toner Percentage : " + tonerPercentage);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.43.11.1.1" + " " + "Toner percentage of a printer gets failed");
        }

        oidString = "1.3.6.1.2.1.43.11.1.1.6";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);
        if (result != null && !result.isEmpty()) {
            if (result.size() == 1) {
                if (result.toString().trim().contains("1.3.6.1.2.1.43.11.1.1.6.1.1")) {
                    String blackToner = "Black and white printer";
                    System.out.println("isColorPrinter : " + blackToner);
                    isColorPrinter = blackToner;
                }
            }
            else {
                String ColorToner = "Color printer";
                System.out.println("isColorPrinter : " + ColorToner);
                isColorPrinter = ColorToner;
            }
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.43.11.1.1.6" + " " + "Toner color of a printer gets failed");
        }

        // The following oid's is used to get the errorCondition,
        // operationalState , currentState , mfgMakeAndModel

        oidString = "1.3.6.1.2.1.25.3";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            temp = oidString + ".5.1.2.1";
            errorCondition = MeterUtils.getSNMPValue(temp, result);
            System.out.println("Error Condition : " + errorCondition);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.25.3" + " " + "Error condition of a printer gets failed");
        }

        oidString = "1.3.6.1.2.1.25.3";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            printerStatus = printerStatusCalc(result, rootOID);
            currentState = printerStatus.get("currentState");
            System.out.println("Current State : " + currentState);
            operationalState = printerStatus.get("operationalState");
            System.out.println("Operational State : " + operationalState);

            temp = oidString + ".2.1.3.1";
            mfgModel = MeterUtils.getSNMPValue(temp, result);
            System.out.println("Manufacture Make And Model : " + mfgModel);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.25.3" + " "
                    + "current, operational state and mfg model of a printer gets failed");
        }

        // The below oid's is used to get the asset id

        oidString = "1.3.6.1.2.1.43.5.1.1";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            temp = oidString + ".17.1";
            tempStr = MeterUtils.getSNMPValue(temp, result);
            assetId = MeterProtocols.PRINTER + "-" + tempStr;
            System.out.println("Asset Id : " + assetId);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.43.5.1.1" + " " + "asset id of a printer gets failed");
        }

        Printer printerObject = new Printer(assetId, uptime, tonerPercentage, outOfPaperIndicator, networkBytesIn,
                networkBytesOut, printsTakenCount, sysName, sysIP, sysDescr, sysContact, sysLocation, errorCondition,
                operationalState, currentState, mfgModel, isColorPrinter, extras);

        GQErrorInformation GqError = null;
        List<GQErrorInformation> gqerrorInformationList = new LinkedList<GQErrorInformation>();
        if (errorList == null || errorList.size() == 0 || errorList.isEmpty()) {
            gqerrorInformationList.add(GqError);
        }
        else {
            GqError = new GQErrorInformation(sysDescr, errorList);
            gqerrorInformationList.add(GqError);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqerrorInformationList, printerObject);
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
        finalTonerPercentage = (remainingUsagefloat / totalValueFloat) * 100;
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
                if (printerCurrentStateMap.containsKey(currentStateValue)) {
                    currentStatus = printerCurrentStateMap.get(currentStateValue);
                }
                else {
                    currentStatus = N_A;
                }

            }
            else if (operationalStateOid != null && vb.getOid().toString().equals(operationalStateOid)) {
                String operationalStateValue = vb.getVariable().toString().trim();
                if (printerOperationalStateMap.containsKey(operationalStateValue)) { // check in the
                                                                                     // predefined
                                                                                     // map whether
                                                                                     // the map has
                                                                                     // the value
                    operationalStatus = printerOperationalStateMap.get(operationalStateValue); // returns running or
                                                                                               // warning
                }
                else {
                    operationalStatus = N_A;
                }
            }
        }
        HashMap<String, String> printerStatus = new HashMap<String, String>(); // return
                                                                               // the
                                                                               // status
                                                                               // of
                                                                               // the
                                                                               // printer
                                                                               // with
                                                                               // currentstate
                                                                               // and
                                                                               // operationalState
        printerStatus.put(currentStateKey, currentStatus); // current
        printerStatus.put(operationalStateKey, operationalStatus); // operational
        return printerStatus;
    }
}