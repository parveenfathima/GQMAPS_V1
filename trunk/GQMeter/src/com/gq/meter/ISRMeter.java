package com.gq.meter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.IntegratedSwitchRouter;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

public class ISRMeter implements GQSNMPMeter {

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion) {
        long computerstartTime = System.currentTimeMillis();
        Snmp snmp = null;
        String assetId = null; // unique identifier about the asset
        String sysName = null;
        String sysIP = null; // string
        String sysDescr = null;
        String sysContact = null;
        String sysLocation = null; // string
        String connectedDevices = null;
        String extras = null; // anything device specific but to be discussed v2

        long upTime = 0; // seconds
        long numberOfPorts = 0;
        long numberOfPortsUp = 0;
        long networkBytesIn = 0; // bytes , v2
        long networkBytesOut = 0; // bytes , v2

        sysIP = ipAddress;
        CommunityTarget target = null;
        HashMap<String, Long> networkBytes = null;
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
        // The following oid's is used to get system parameters

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
        // The following oid's is used to get number of ports

        oidString = "1.3.6.1.2.1.2.1";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            temp = oidString + ".0";
            tempStr = MeterUtils.getSNMPValue(temp, result);
            numberOfPorts = Integer.parseInt(tempStr);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + "Unable to determine number of ports");
        }
        // The following oid's is used to get number of active ports

        oidString = "1.3.6.1.2.1.2.2.1.7";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            numberOfPortsUp = activePortsCalc(result, rootOID);

        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.2.2.1.7" + " " + "Unable to determine total number of active ports");
        }
        // The following oid's is used to get the network in and out bytes

        oidString = ".1.3.6.1.2.1.2.2.1";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {

            HashMap<String, Long> switchNetworkMap = new HashMap<String, Long>();
            networkBytes = switchNetworkBytesCalc(result, rootOID, switchNetworkMap);
            networkBytesIn = networkBytes.get("InBytes");
            networkBytesOut = networkBytes.get("OutBytes");
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.2.2.1" + " " + "Unable to get network bandwidth details");
        }
        // The following oid's is used to get the devices that are connected to ISR.

        oidString = ".1.3.6.1.2.1.31.1.1.1.18";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {

            connectedDevices = ConnectedDevicesCalc(result, rootOID);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.31.1.1.1.18" + " " + "Unable to provide list of connected devices");
        }
        // The following oid's is used to get the asset ID.

        oidString = "1.3.6.1.2.1.2.2.1.6";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            temp = oidString + ".1";
            String assetIdVal = MeterUtils.getSNMPValue(temp, result);
            assetId = MeterProtocols.ISR + "-" + assetIdVal.replaceAll(":", "");
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.2.2.1.6" + " " + MeterConstants.ASSET_ID_ERROR);
        }

        IntegratedSwitchRouter switchObject = new IntegratedSwitchRouter(assetId, upTime, numberOfPorts,
                numberOfPortsUp, networkBytesIn, networkBytesOut, sysName, sysIP, sysDescr, sysContact, sysLocation,
                connectedDevices, extras);

        GQErrorInformation gqErrorInfo = null;
        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(sysDescr, errorList);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, switchObject);

        long computerendTime = System.currentTimeMillis();
        System.out.println("Time taken bye the isr meter is : " + (computerendTime - computerstartTime));
        return gqMeterObject;
    }

    private long activePortsCalc(List<VariableBinding> result, OID rootOid) {

        String rootId = rootOid.toString();
        String totalPorts = null;
        long totalActivePorts = 0;
        for (VariableBinding vb : result) {

            String lastchar = String.valueOf(vb.getOid().last());
            totalPorts = rootId + "." + lastchar;

            if (totalPorts != null && vb.getOid().toString().equals(totalPorts)) {

                String activeAndInactivePorts = vb.getVariable().toString().trim();
                if (!activeAndInactivePorts.equalsIgnoreCase("2")) {
                    totalActivePorts++;
                }
            }
        }
        return totalActivePorts;
    }

    private HashMap<String, Long> switchNetworkBytesCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, Long> switchNetworkMap) {

        String networkInOid = null;
        String networkOutOid = null;
        long switchNetworkIn = 0;
        long switchNetworkOut = 0;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) {
            String lastchar = String.valueOf(vb.getOid().last());

            networkInOid = rootId + "." + "10" + "." + lastchar;

            networkOutOid = rootId + "." + "16" + "." + lastchar;

            if (networkInOid != null && vb.getOid().toString().contains(networkInOid)) {

                String switchNetworkInVal = vb.getVariable().toString().trim();
                if (!switchNetworkInVal.equalsIgnoreCase("0")) {

                    String switchNetworkInStr = vb.getVariable().toString().trim();
                    long switchNetworkInValue = Long.parseLong(switchNetworkInStr);
                    switchNetworkIn = switchNetworkIn + switchNetworkInValue;
                    switchNetworkMap.put("InBytes", switchNetworkIn);

                } // 2nd if loop ends
            } // if loop ends
            else if (networkOutOid != null && vb.getOid().toString().contains(networkOutOid)) {
                String switchNetworkOutVal = vb.getVariable().toString().trim();
                if (!switchNetworkOutVal.equalsIgnoreCase("0")) {
                    String switchNetworkOutStr = vb.getVariable().toString().trim();
                    long switchNetworkOutValue = Long.parseLong(switchNetworkOutStr);
                    switchNetworkOut = switchNetworkIn + switchNetworkOutValue;
                    switchNetworkMap.put("OutBytes", switchNetworkOut);
                }
            }
        } // for loop ends
        return switchNetworkMap;

    } // network bytes calculation for switch gets over.

    private String ConnectedDevicesCalc(List<VariableBinding> result, OID rootOid) {
        Set<String> connectedDevice = new HashSet<String>();
        String value = null;
        for (VariableBinding vb : result) {
            value = vb.getVariable().toString();
            if (value != null && value.trim().length() != 0) {
                value = value.replaceAll("[<>]", "").trim().toUpperCase();
                connectedDevice.add(value);
            }
        }
        String connectedDevices = connectedDevice.toString().substring(1, connectedDevice.toString().length() - 1);
        return connectedDevices;
    }

}
