package com.gq.meter;

import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.NSRGConnDevice;
import com.gq.meter.object.NSRGConnDeviceId;
import com.gq.meter.object.NSRGSnapshot;

import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterUtils;

/**
 * @author yogalakshmi.s
 * @change parveen
 */
public class NSRGMeter implements GQSNMPMeter {

    List<String> errorList = new LinkedList<String>();

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches) {

        long nsrgMeterTime = 0L;
        long NSRGstartTime = System.currentTimeMillis();
        Snmp snmp = null;

        Long runId = 0L;
        String assetId = null; // unique identifier about the asset
        CPNId id = null;
        // variables that are used to get the NSRG snapshot
        String sysIP = null; // string
        long upTime = 0; // seconds
        Short numberOfPorts = 0;
        Short numberOfPortsUp = 0;
        long networkBytesIn = 0; // bytes , v2
        long networkBytesOut = 0; // bytes , v2
        String extras = null; // anything device specific but to be discussed v2

        CommunityTarget target = null;
        HashMap<String, Long> networkBytes = null;

        NSRGSnapshot nsrgSnapShot = null;
        HashSet<NSRGConnDevice> connectedDevices = null;
        Asset assetObj = null;

        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
            target = MeterUtils.makeTarget(ipAddress, communityString, snmpVersion);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            // the below line is used to get the system basic info.
            assetObj = MeterUtils.sysBasicInfo(communityString, ipAddress, snmpVersion, toggleSwitches);
            assetObj.setProtocolId(MeterConstants.NSRG_PROTOCOL);

            String oidString = null;
            String temp;
            String tempStr;

            OID rootOID = null;
            List<VariableBinding> result = null;

            // The following oid's is used to get the asset ID.
            oidString = "1.3.6.1.2.1.2.2.1.6";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            if (result != null && !result.isEmpty()) {
                temp = oidString + ".1";
                String assetIdVal = MeterUtils.getSNMPValue(temp, result);
                assetId = "S-" + assetIdVal.replaceAll(":", "");
                assetObj.setAssetId(assetId);
            }
            else {
                errorList.add("Root OID : 1.3.6.1.2.1.2.2.1.6" + " " + MeterConstants.ASSET_ID_ERROR);
            }

            // ASSET ID , RUN ID STARTS HERE.
            id = new CPNId(runId, assetId);
            for (String element : toggleSwitches) { // main for loop starts
                if (element.equalsIgnoreCase(MeterConstants.SNAPSHOT)) { // main if loop starts
                    sysIP = ipAddress;

                    // The following oid's is used to get number of ports
                    oidString = "1.3.6.1.2.1.1";
                    rootOID = new OID(oidString);

                    result = MeterUtils.walk(rootOID, target);
                    if (result != null && !result.isEmpty()) {

                        temp = oidString + ".3.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        upTime = new MeterUtils().upTimeCalc(tempStr);
                    }

                    oidString = "1.3.6.1.2.1.2.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        temp = oidString + ".0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        numberOfPorts = (short) Integer.parseInt(tempStr);
                    }
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.1" + " "
                                + "Unable to determine number of ports");
                    }

                    // The following oid's is used to get number of active ports
                    oidString = "1.3.6.1.2.1.2.2.1.7";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        numberOfPortsUp = (short) activePortsCalc(result, rootOID);

                    }
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.2.2.1.7" + " "
                                + "Unable to determine total number of active ports");
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
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.2.2.1" + " "
                                + "Unable to get network bandwidth details");
                    }

                    nsrgSnapShot = new NSRGSnapshot(id, sysIP, upTime, numberOfPorts, numberOfPortsUp, networkBytesIn,
                            networkBytesOut, extras);

                    continue;
                } // main if loop ends

                // The following oid's is used to get the devices that are connected to NSRG.
                if (element.equalsIgnoreCase(MeterConstants.CONNECTED_DEVICES)) {// 1st if loop starts
                    oidString = ".1.3.6.1.2.1.4.22.1.4";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        connectedDevices = ConnectedDevicesCalc(result, rootOID, id);
                    }
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.4.22.1.4" + " "
                                + "Unable to provide list of connected devices");
                    }
                    continue;
                } // 1st if loop ends

            }// main for loop ends

        }// try ends
        catch (Exception e) {
            errorList.add(ipAddress + " " + e.getMessage());
        }

        NSRG nsrg = new NSRG(id, assetObj, nsrgSnapShot, connectedDevices);
        if (nsrg.getAssetObj().getAssetId().equalsIgnoreCase("S-null")) {
            errorList.add(ipAddress + " " + "Asset id is null");
        }
        GQErrorInformation gqErrorInfo = null;
        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(assetObj.getDescr(), errorList);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, nsrg);

        long NSRGendTime = System.currentTimeMillis();
        nsrgMeterTime = nsrgMeterTime + (NSRGendTime - NSRGstartTime);
        return gqMeterObject;
    }

    /**
     * This method is used to the ports that are active in NSRG.
     * 
     * @param result
     * @param rootOid
     * @return
     */
    private long activePortsCalc(List<VariableBinding> result, OID rootOid) {

        String rootId = rootOid.toString();
        String totalPorts = null;
        long totalActivePorts = 0;
        for (VariableBinding vb : result) { // for loop starts

            String lastchar = String.valueOf(vb.getOid().last());
            totalPorts = rootId + "." + lastchar;

            if (totalPorts != null && vb.getOid().toString().equals(totalPorts)) { // 1st if loop starts
                String activeAndInactivePorts = vb.getVariable().toString().trim();
                if (!activeAndInactivePorts.equalsIgnoreCase("2")) {
                    totalActivePorts++;
                }
            } // 1st if loop ends
        } // for loop ends
        return totalActivePorts;
    }

    /**
     * This Method is used to get the network in and out bytes of a NSRG.
     * 
     * @param result
     * @param rootOid
     * @param switchNetworkMap
     * @return
     */
    private HashMap<String, Long> switchNetworkBytesCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, Long> switchNetworkMap) {

        String networkInOid = null;
        String networkOutOid = null;
        long switchNetworkIn = 0;
        long switchNetworkOut = 0;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) { // for loop starts
            String lastchar = String.valueOf(vb.getOid().last());

            networkInOid = rootId + "." + "10" + "." + lastchar;

            networkOutOid = rootId + "." + "16" + "." + lastchar;

            if (networkInOid != null && vb.getOid().toString().contains(networkInOid)) { // 1st if loop ends

                String switchNetworkInVal = vb.getVariable().toString().trim();
                if (!switchNetworkInVal.equalsIgnoreCase("0")) { // 2nd if loop starts

                    String switchNetworkInStr = vb.getVariable().toString().trim();

                    if (!switchNetworkInStr.trim().isEmpty() && switchNetworkInStr != null) {
                        long switchNetworkInValue = Long.parseLong(switchNetworkInStr);
                        switchNetworkIn = switchNetworkIn + switchNetworkInValue;
                        switchNetworkMap.put("InBytes", switchNetworkIn);
                    }

                } // 2nd if loop ends
            } // 1st if loop ends
            else if (networkOutOid != null && vb.getOid().toString().contains(networkOutOid)) { // else if loop starts
                String switchNetworkOutVal = vb.getVariable().toString().trim();

                if (!switchNetworkOutVal.equalsIgnoreCase("0")) {
                    String switchNetworkOutStr = vb.getVariable().toString().trim();

                    if (!switchNetworkOutStr.trim().isEmpty() && switchNetworkOutStr != null) {
                        long switchNetworkOutValue = Long.parseLong(switchNetworkOutStr);
                        switchNetworkOut = switchNetworkIn + switchNetworkOutValue;
                        switchNetworkMap.put("OutBytes", switchNetworkOut);
                    }
                }
            } // else if loop ends
        } // for loop ends
        return switchNetworkMap;
    } // network bytes calculation for switch gets over.

    /**
     * This method is used to get the devices that are connected to the NSRG.
     * 
     * @param result
     * @param rootOid
     * @param id
     * @return
     */
    private HashSet<NSRGConnDevice> ConnectedDevicesCalc(List<VariableBinding> result, OID rootOid, CPNId id) {

        Long runId = id.getRunId();
        String assetId = id.getAssetId();

        HashSet<NSRGConnDevice> connectedDevices = new HashSet<NSRGConnDevice>();
        String finalIP = null;
        NSRGConnDevice nsrgConnDevice = null;
        NSRGConnDeviceId nsrgConnDeviceId = null;

        for (VariableBinding vb : result) { // for loop starts
            String dynamic = vb.getVariable().toString().trim();
            if (dynamic.trim().equalsIgnoreCase("3")) { // if loop starts
                String dynamicOID = vb.getOid().toString();
                finalIP = dynamicOID.substring(23);
                if (finalIP != null && finalIP.trim().length() != 0) {
                    nsrgConnDeviceId = new NSRGConnDeviceId(runId, assetId, finalIP);
                    nsrgConnDevice = new NSRGConnDevice(nsrgConnDeviceId);
                    connectedDevices.add(nsrgConnDevice);
                }
            } // if loop ends
        } // for loop ends
        return connectedDevices;
    }
}
