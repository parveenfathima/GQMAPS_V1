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

import com.gq.meter.bo.ITAssetDiscoverer;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.NSRGConnDevice;
import com.gq.meter.object.NSRGConnDeviceId;
import com.gq.meter.object.NSRGSnapshot;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

public class NSRGMeter implements GQSNMPMeter {

    List<String> errorList = new LinkedList<String>();

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches) {

        long computerstartTime = System.currentTimeMillis();
        Snmp snmp = null;

        String assetId = null; // unique identifier about the asset
        CPNId id = null;

        // ASSET
        String sysName = null;
        String sysDescr = null;
        String sysContact = null;
        String sysLocation = null;
        String extras = null; // anything device specific but to be discussed v2

        // SNAPSHOT
        String sysIP = null; // string
        long upTime = 0; // seconds
        Short numberOfPorts = 0;
        Short numberOfPortsUp = 0;
        long networkBytesIn = 0; // bytes , v2
        long networkBytesOut = 0; // bytes , v2

        CommunityTarget target = null;
        HashMap<String, Long> networkBytes = null;
        HashSet<NSRGConnDevice> connectedDevices = null;

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
        try {
            String oidString = "1.3.6.1.2.1.1";
            String temp;
            String tempStr;
            int sysLength;

            OID rootOID = new OID(oidString);
            List<VariableBinding> result = null;

            result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is v2
            if (result != null && !result.isEmpty()) {

                temp = oidString + ".1.0";
                sysDescr = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysDescr.length();
                if (sysLength >= 200) {
                    sysDescr = sysDescr.substring(0, 199);
                }

                temp = oidString + ".4.0";
                sysContact = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysContact.length();
                if (sysLength >= 45) {
                    sysContact = sysContact.substring(0, 44);
                }

                temp = oidString + ".5.0";
                sysName = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysName.length();
                if (sysLength >= 45) {
                    sysName = sysName.substring(0, 44);
                }

                temp = oidString + ".6.0";
                sysLocation = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysLocation.length();
                if (sysLength >= 45) {
                    sysLocation = sysLocation.substring(0, 44);
                }
            }
            else {
                errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + MeterConstants.STANDARD_SYSTEM_ATTRIBUTES_ERROR);
            }

            // The following oid's is used to get the asset ID.
            oidString = "1.3.6.1.2.1.2.2.1.6";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            if (result != null && !result.isEmpty()) {
                temp = oidString + ".1";
                String assetIdVal = MeterUtils.getSNMPValue(temp, result);
                assetId = MeterProtocols.NSRG + "-" + assetIdVal.replaceAll(":", "");
            }
            else {
                errorList.add("Root OID : 1.3.6.1.2.1.2.2.1.6" + " " + MeterConstants.ASSET_ID_ERROR);
            }

            // ASSET ID , RUN ID STARTS HERE.
            int runId = ITAssetDiscoverer.runId;

            id = new CPNId(runId, assetId);

            for (String element : toggleSwitches) {
                if (element.equalsIgnoreCase(MeterConstants.FULL_DETAILS)
                        || element.equalsIgnoreCase(MeterConstants.SNAPSHOT)) {
                    // The following oid's is used to get number of ports

                    sysIP = ipAddress;

                    oidString = "1.3.6.1.2.1.1";
                    rootOID = new OID(oidString);

                    result = MeterUtils.walk(rootOID, target);
                    if (result != null && !result.isEmpty()) {

                        temp = oidString + ".3.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        upTime = MeterUtils.upTimeCalc(tempStr);
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
                        errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + "Unable to determine number of ports");
                    }
                    // The following oid's is used to get number of active ports

                    oidString = "1.3.6.1.2.1.2.2.1.7";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        numberOfPortsUp = (short) activePortsCalc(result, rootOID);

                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.2.2.1.7" + " "
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
                        errorList.add("Root OID : 1.3.6.1.2.1.2.2.1" + " " + "Unable to get network bandwidth details");
                    }
                }
                // The following oid's is used to get the devices that are connected to NSRG.

                if (element.equalsIgnoreCase(MeterConstants.FULL_DETAILS)
                        || element.equalsIgnoreCase(MeterConstants.CONNECTED_DEVICES)) {
                    oidString = ".1.3.6.1.2.1.4.22.1.4";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        connectedDevices = ConnectedDevicesCalc(result, rootOID, id);
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.4.22.1.4" + " "
                                + "Unable to provide list of connected devices");
                    }
                }
            }// for loop ends
        }
        catch (Exception e) {
            errorList.add(ipAddress + " " + e.getMessage());
        }

        String protocolId = "protocolid";
        String appId = "app_id";
        String assetUsg = "assetUsg";
        Byte assetStrength = 1;
        String ctlgId = "ctlg_id";

        Asset assetObj = new Asset(assetId, protocolId, sysName, sysDescr, sysContact, sysLocation, appId, assetUsg,
                assetStrength, ctlgId);

        NSRGSnapshot nsrgSnapShot = new NSRGSnapshot(id, sysIP, upTime, numberOfPorts, numberOfPortsUp, networkBytesIn,
                networkBytesOut, extras);

        NSRG nsrg = new NSRG(id, assetObj, nsrgSnapShot, connectedDevices);

        GQErrorInformation gqErrorInfo = null;
        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(sysDescr, errorList);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, nsrg);

        long computerendTime = System.currentTimeMillis();
        MeterUtils.isrMeterTime = MeterUtils.isrMeterTime + (computerendTime - computerstartTime);
        System.out.println("Time taken bye the isr meter is : " + (computerendTime - computerstartTime));
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

        for (VariableBinding vb : result) {
            String lastchar = String.valueOf(vb.getOid().last());

            networkInOid = rootId + "." + "10" + "." + lastchar;

            networkOutOid = rootId + "." + "16" + "." + lastchar;

            if (networkInOid != null && vb.getOid().toString().contains(networkInOid)) {

                String switchNetworkInVal = vb.getVariable().toString().trim();
                if (!switchNetworkInVal.equalsIgnoreCase("0")) {

                    String switchNetworkInStr = vb.getVariable().toString().trim();

                    if (!switchNetworkInStr.trim().isEmpty() && switchNetworkInStr != null) {
                        long switchNetworkInValue = Long.parseLong(switchNetworkInStr);
                        switchNetworkIn = switchNetworkIn + switchNetworkInValue;
                        switchNetworkMap.put("InBytes", switchNetworkIn);
                    }

                } // 2nd if loop ends
            } // if loop ends
            else if (networkOutOid != null && vb.getOid().toString().contains(networkOutOid)) {
                String switchNetworkOutVal = vb.getVariable().toString().trim();

                if (!switchNetworkOutVal.equalsIgnoreCase("0")) {
                    String switchNetworkOutStr = vb.getVariable().toString().trim();

                    if (!switchNetworkOutStr.trim().isEmpty() && switchNetworkOutStr != null) {
                        long switchNetworkOutValue = Long.parseLong(switchNetworkOutStr);
                        switchNetworkOut = switchNetworkIn + switchNetworkOutValue;
                        switchNetworkMap.put("OutBytes", switchNetworkOut);
                    }
                    else {
                        errorList.add(MeterConstants.NO_VALUE + switchNetworkOut);
                    }
                }
            }
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

        int runId = id.getRunId();
        String assetId = id.getAssetId();

        HashSet<NSRGConnDevice> connectedDevices = new HashSet<NSRGConnDevice>();
        String finalIP = null;
        NSRGConnDevice nsrgConnDevice = null;
        NSRGConnDeviceId nsrgConnDeviceId = null;

        for (VariableBinding vb : result) {
            String dynamic = vb.getVariable().toString().trim();
            if (dynamic.trim().equalsIgnoreCase("3")) {
                String dynamicOID = vb.getOid().toString();
                finalIP = dynamicOID.substring(23);
                if (finalIP != null && finalIP.trim().length() != 0) {
                    nsrgConnDeviceId = new NSRGConnDeviceId(runId, assetId, finalIP);
                    nsrgConnDevice = new NSRGConnDevice(nsrgConnDeviceId);
                    connectedDevices.add(nsrgConnDevice);
                }

            }
        }
        return connectedDevices;
    }
}
