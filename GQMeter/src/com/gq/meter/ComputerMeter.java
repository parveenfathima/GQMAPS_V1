package com.gq.meter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.CompConnDevice;
import com.gq.meter.object.CompConnDeviceId;
import com.gq.meter.object.CompInstSoftware;
import com.gq.meter.object.CompInstSoftwareId;
import com.gq.meter.object.CompProcess;
import com.gq.meter.object.CompProcessId;
import com.gq.meter.object.CompSnapshot;
import com.gq.meter.object.Computer;
import com.gq.meter.object.OsType;

import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterUtils;

/**
 * @author yogalakshmi.s,sriram
 * @change parveen
 */

public class ComputerMeter implements GQSNMPMeter {

    List<String> errorList = new LinkedList<String>();

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches) {

        long computerstartTime = System.currentTimeMillis();
        Snmp snmp = null;
        Long runId = 0L;
        String assetId = null; // unique identifier about the asset
        String sysDescription = null;
        CPNId id = null;
        // Variables that are used to get the computer snapshot
        String sysIP = null; // string
        String osId = null; // string
        short cpuLoad = 0; // in percentage
        long totalMemory = 0; // bytes
        long usedMemory = 0; // bytes
        long totalVirtualMemory = 0; // bytes
        long usedVirtualMemory = 0; // bytes
        long totalDiskSpace = 0; // bytes
        long usedDiskSpace = 0; // bytes
        long upTime = 0; // seconds
        short numLoggedInUsers = 0;
        short numProcesses = 0;
        long networkBytesIn = 0; // bytes
        long networkBytesOut = 0; // bytes
        double clockSpeed = 0; // v2
        String extras = null; // anything device specific but to be discussed v2
        CommunityTarget target = null;
        HashMap<String, String> networkBytes = null;
        OsType osTypeObj = null;

        // objects that go in finally
        CompSnapshot snapShot = null;
        ArrayList<CompInstSoftware> installedSwList = null;
        HashSet<CompConnDevice> connectedDevices = null;
        ArrayList<CompProcess> processList = null;
        Asset assetObj = null;
        
        //snmp listented here.
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
            assetObj.setProtocolId(MeterConstants.COMPUTER_PROTOCOL);
            String oidString = null;
            String temp;
            String tempStr;
            boolean isWindows = false;
            boolean isLinux = false;
            boolean isMac = false;
            
            OID rootOID = null;
            List<VariableBinding> result = null;
            List<VariableBinding> result1 = null;
            sysDescription = assetObj.getDescr();
            
            if (null != sysDescription) { // 1st if starts
            	sysDescription = sysDescription.toLowerCase();
                if (sysDescription.contains("windows")) {
                    osId = "windows";
                    isWindows = true;
                }
                else if (sysDescription.contains("linux")) {
                    osId = "linux";
                    isLinux = true;
                }
                else if (sysDescription.contains("unix")) {
                    osId = "unix";
                }
                //we are here for mac all the processes are same as that of linux machine except asset id calculations.
                else if(sysDescription.contains("mac")) {
                	osId = "mac";
                	isMac = true;
                }
            }// 1st if ends
             // the following oid's is used to get the asset id for windows.
            if (isWindows) {
                oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);
                if (result != null && !result.isEmpty()) {
                    HashMap<String, String> winNetworkMap = new HashMap<String, String>();
                    networkBytes = winAssetIdCalc(result, rootOID, winNetworkMap);
                    assetId = "C-" + networkBytes.get("macWinNetworkValue");
                    assetObj.setAssetId(assetId);
                }// 2nd if ends
            }// 1st if ends
             // the following oid's is used to get the asset id for Linux.
            else if(isLinux) {
                oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);
                if (result != null && !result.isEmpty()) {
                    String[] ethernet = new String[] { "eth2", "eth1", "eth0", "en3","en2", "en1", "en0", "em3", "em2",
                            "em1", "wlan"};
                    HashMap<String, List<Long>> networkMap = new HashMap<String, List<Long>>();
                    networkBytes = linuxAssetIdCalc(result, rootOID, ethernet, networkMap, assetId, sysDescription);
                    assetId = "C-" + networkBytes.get("assetId");
                    assetObj.setAssetId(assetId);
                }
                else {
                    errorList.add(assetId + " Root OID : 1.3.6.1.2.1.2.2.1" + " "
                            + "Unable to get network bandwidth details and unable to collate asset ID");
                }// 2nd else ends
            }// 1st else ends
            
            else if(isMac) {
            	oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);
                if (result != null && !result.isEmpty()) {
                    HashMap<String, String> macNetworkMap = new HashMap<String, String>();
                    networkBytes = macAssetIdCalc(result, rootOID, macNetworkMap);
                    assetId = "C-" + networkBytes.get("macWinNetworkValue");
                    assetObj.setAssetId(assetId);
                }// 2nd if ends
            }//else if ends
           
            // ASSET ID , RUN ID STARTS HERE.
            id = new CPNId(runId, assetId);
            for (String element : toggleSwitches) { // main for loop starts here

                if (element.equalsIgnoreCase(MeterConstants.SNAPSHOT)) { // main if loop starts here
                    sysIP = ipAddress;
                    oidString = "1.3.6.1.2.1.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);
                    if (result != null && !result.isEmpty()) {
                        temp = oidString + ".3.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        upTime = MeterUtils.upTimeCalc(tempStr);
                    }

                    // The following oid's is used to get no. of users and processes
                    oidString = "1.3.6.1.2.1.25.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) { // 1st if loop starts
                        temp = oidString + ".5.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        if (tempStr != null) {
                            numLoggedInUsers = (short) Integer.parseInt(tempStr);
                        }
                        temp = oidString + ".6.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        if (tempStr != null) { // 2nd if loop starts
                            numProcesses = (short) Integer.parseInt(tempStr);
                        } // 2nd if loop ends
                    } // 1st if loop ends
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.25.1" + " "
                                + "Unable to get number of users and processes");
                    }

                    // The following oid's is used to get disc space, physical memory, virtual memory
                    oidString = "1.3.6.1.2.1.25.2.3.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) { // 1st if loop starts
                        String variable = null;

                        if (isWindows) { // 2nd if loop starts
                            variable = ":\\";
                            long windowsDriveSize = getMemorycalc(result, rootOID, variable, false);
                            long usedWindowsDriveSize = getMemorycalc(result, rootOID, variable, true);

                            variable = "physical memory";
                            totalMemory = getMemorycalc(result, rootOID, variable, false);
                            usedMemory = getMemorycalc(result, rootOID, variable, true);

                            variable = "virtual memory";
                            totalVirtualMemory = getMemorycalc(result, rootOID, variable, false);
                            usedVirtualMemory = getMemorycalc(result, rootOID, variable, true);

                            totalDiskSpace = windowsDriveSize + totalVirtualMemory;
                            usedDiskSpace = usedWindowsDriveSize + usedVirtualMemory;
                        }// 2nd if loop ends
                        else { // 1st else loop starts
                            variable = "/dev/shm";
                            long linuxDriveSize = getMemorycalc(result, rootOID, variable, false);
                            long usedLinuxDriveSize = getMemorycalc(result, rootOID, variable, true);
                            

                            variable = "/home";
                            long homeSize = getMemorycalc(result, rootOID, variable, false);
                            long usedHomeSize = getMemorycalc(result, rootOID, variable, true);

                            variable = "swap space";
                            totalVirtualMemory = getMemorycalc(result, rootOID, variable, false);
                            usedVirtualMemory = getMemorycalc(result, rootOID, variable, true);

                            variable = "/";
                            long DiskSpace1 = getMemorycalc(result, rootOID, variable, false);
                            long usedtotalDiskSpace1 = getMemorycalc(result, rootOID, variable, true);

                            variable = "/boot";
                            long DiskSpace2 = getMemorycalc(result, rootOID, variable, false);
                            long UsedtotalDiskSpace2 = getMemorycalc(result, rootOID, variable, true);

                            totalDiskSpace = linuxDriveSize + totalVirtualMemory + DiskSpace1 + DiskSpace2 + homeSize;
                            usedDiskSpace = usedLinuxDriveSize + usedVirtualMemory + usedtotalDiskSpace1
                                    + UsedtotalDiskSpace2 + usedHomeSize;

                            variable = "real memory";
                            totalMemory = getMemorycalc(result, rootOID, variable, false);

                            if (totalMemory == 0L) {
                                variable = "physical memory";
                                totalMemory = getMemorycalc(result, rootOID, variable, false);
                                usedMemory = getMemorycalc(result, rootOID, variable, true);
                            }
                            else { // 2nd else loop starts
                                usedMemory = getMemorycalc(result, rootOID, variable, true);
                            } // 2nd else loop ends
                            if (usedMemory == 0) {
                               errorList.add(ipAddress + " " + "Unable to get used Memory");
                           }
                        } // 1st else loop ends
                    } // 1st if loop ends
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.25.2.3.1" + " "
                                + "Unable to get disk and memory details");
                    }

                    // The following oid's is used to get CPU load
                    oidString = ".1.3.6.1.2.1.25.3.3.1.2";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        cpuLoad = (short) cpuLoadCalc(result, rootOID);
                    }
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.25.3.3.1.2" + " "
                                + "Unable to compute CPU load");
                    }

                    // the following oid's is used to get network in and out bytes for windows
                    if (isWindows) { // 1st if loop starts
                        oidString = ".1.3.6.1.2.1.2.2.1";
                        rootOID = new OID(oidString);
                        result = MeterUtils.walk(rootOID, target);

                        if (result != null && !result.isEmpty()) { // 2nd if loop starts
                            HashMap<String, String> winNetworkMap = new HashMap<String, String>();
                            networkBytes = winNetworkBytesCalc(result, rootOID, winNetworkMap);

                            String networkBytesInStr = networkBytes.get("InBytes");
                            networkBytesIn = Long.parseLong(networkBytesInStr);

                            String networkBytesOutStr = networkBytes.get("OutBytes");
                            networkBytesOut = Long.parseLong(networkBytesOutStr);
                        } // 2nd if loop ends
                        else {
                            errorList.add(assetId + " Root OID : 1.3.6.1.2.1.2.2.1" + " "
                                    + "Unable to get network bandwidth details and unable to collate asset ID");
                        }
                    }// 1st if loop ends

                    // the following oid's is used to get network in and out bytes for Linux
                    else { // 1st else loop starts
                        oidString = ".1.3.6.1.2.1.2.2.1";
                        rootOID = new OID(oidString);
                        result = MeterUtils.walk(rootOID, target);

                        if (result != null && !result.isEmpty()) { // 1st if loop starts
                            String[] ethernet = new String[] { "eth2", "eth1", "eth0", "en3","en2", "en1", "en0", "em3", "em2",
                                    "em1", "wlan" };

                            HashMap<String, List<Long>> networkMap = new HashMap<String, List<Long>>();
                            networkBytes = networkBytesCalc(result, rootOID, ethernet, networkMap, assetId,
                                    sysDescription);
                            for (int i = 0; i < ethernet.length; i++) {
                                if (networkBytes.get(ethernet[i] + "InBytes") != null) {
                                    networkBytesIn = networkBytesIn
                                            + Long.parseLong(networkBytes.get(ethernet[i] + "InBytes"));
                                }
                                if (networkBytes.get(ethernet[i] + "OutBytes") != null) {
                                    networkBytesOut = networkBytesOut
                                            + Long.parseLong(networkBytes.get(ethernet[i] + "OutBytes"));
                                }
                            } // for loop ends
                        }// 1st if loop ends
                        else {
                            errorList.add(assetId + " Root OID : 1.3.6.1.2.1.2.2.1" + " "
                                    + "Unable to get network bandwidth details  and unable to collate asset ID");
                        }
                    } // 1st else loop ends

                    osTypeObj = new OsType(osId, sysDescription);

                    snapShot = new CompSnapshot(id, sysIP, osId, totalMemory, usedMemory, totalVirtualMemory,
                            usedVirtualMemory, totalDiskSpace, usedDiskSpace, cpuLoad, upTime, numLoggedInUsers,
                            numProcesses, networkBytesIn, networkBytesOut, clockSpeed, extras);

                    continue;

                } // main if loop ends here

                // the following oid's is used to get the installed software list
                if (element.equalsIgnoreCase(MeterConstants.INSTALLED_SOFTWARE)) { // 1st if loop starts

                    oidString = ".1.3.6.1.2.1.25.6.3.1.4";
                    rootOID = new OID(oidString);
                    List<VariableBinding> appResult = MeterUtils.walk(rootOID, target);

                    if (appResult != null && !appResult.isEmpty()) { // 2nd if loop starts

                        oidString = ".1.3.6.1.2.1.25.6.3.1.2";
                        rootOID = new OID(oidString);
                        List<VariableBinding> softwareResult = MeterUtils.walk(rootOID, target);

                        oidString = ".1.3.6.1.2.1.25.6.3.1.5";
                        rootOID = new OID(oidString);
                        List<VariableBinding> dateResult = MeterUtils.walk(rootOID, target);

                        // Here to check count of applicationtype,softwarename and date must be equal.
                        // because due to network speed count of applicationtype,softwarename and date must not be
                        // equal.
                        if (appResult.size() == softwareResult.size() && appResult.size() == dateResult.size()) {
                            installedSwList = installedSwListCalc(appResult, softwareResult, dateResult, rootOID,
                                    isWindows, isLinux, isMac, id);
                        }
                        else {
                            errorList.add(assetId + " Unable to fetch list of installed software due to network speed");
                        }
                    } // 2nd if loop ends
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.25.6.3.1.4" + " "
                                + "Unable to get list of installed software");
                    }
                    continue;
                } // 1st if loop ends.

                // the following oid's is used to get the IP and port number for the devices that is connected.
                if (element.equalsIgnoreCase(MeterConstants.CONNECTED_DEVICES)) { // 1st if loop starts

                    if (result != null && !result.isEmpty()) {
                        oidString = ".1.3.6.1.2.1.6.13.1.4";
                        rootOID = new OID(oidString);
                        result = MeterUtils.walk(rootOID, target);
                        oidString = ".1.3.6.1.2.1.6.13.1.5";
                        rootOID = new OID(oidString);
                        result1 = MeterUtils.walk(rootOID, target);
                        connectedDevices = ConnectedDevicesCalc(result, result1, ipAddress, id);
                    }
                    else {
                        errorList.add(assetId + " Root OID : 1.3.6.1.2.1.6.13.1.1" + " "
                                + "Unable to get port number and ip address of connected devices");
                    }
                    continue;

                } // 1st if loop ends

                // The following OID is used to get the System run name, CPU and memory share for a particular process .
                if (element.equalsIgnoreCase(MeterConstants.PROCESS)) { // 1st if loop starts
                    if (result != null && !result.isEmpty()) { // 2nd if loop starts
                        oidString = ".1.3.6.1.2.1.25.4.2.1.2";
                        rootOID = new OID(oidString);
                        List<VariableBinding> sysRunNameResult = MeterUtils.walk(rootOID, target);

                        oidString = ".1.3.6.1.2.1.25.5.1.1.1";
                        rootOID = new OID(oidString);
                        List<VariableBinding> cpuShareResult = MeterUtils.walk(rootOID, target);

                        oidString = ".1.3.6.1.2.1.25.5.1.1.2";
                        rootOID = new OID(oidString);
                        List<VariableBinding> memShareResult = MeterUtils.walk(rootOID, target);

                        if (sysRunNameResult.size() == cpuShareResult.size()
                                && sysRunNameResult.size() == memShareResult.size()) {
                            processList = ProcessCalc(result, rootOID, sysRunNameResult, cpuShareResult,
                                    memShareResult, id);
                        }
                        else {
                            errorList.add(ipAddress + " Unable to get List of process due to network speed ");
                        }

                    } // 2nd if loop ends
                    else {
                        errorList.add(assetId + " Root OID : .1.3.6.1.2.1.25" + " "
                                + "Unable to get the system run name, cpu and memory share");
                    }
                    continue;

                } // 1st if loop ends

            }// main for loop ends here

        }// try ends
        catch (Exception e) {
        	e.printStackTrace();
            errorList.add(ipAddress + " " + e.getMessage());
        }
        
        Computer compObject = new Computer(id, assetObj, osTypeObj, snapShot, installedSwList, processList,
                connectedDevices);

        if (compObject.getAssetObj().getAssetId().equalsIgnoreCase("C-null")) {
            errorList.add(ipAddress + " " + "Asset Id is null");
        }

        GQErrorInformation gqErrorInfo = null;
        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(sysDescription, errorList);
        }
        
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, compObject);
        
        long computerendTime = System.currentTimeMillis();
        new MeterUtils().compMeterTime = new MeterUtils().compMeterTime + (computerendTime - computerstartTime);

        return gqMeterObject;

    } // GQMeterData method ends

    /**
     * This method is used to calculate the CPU Load that is consumed by the asset.
     * 
     * @param result
     * @param rootOid
     * @return
     */
    private long cpuLoadCalc(List<VariableBinding> result, OID rootOid) {
        long totalCpuValue = 0;
        long totalKeys = 0;

        for (VariableBinding vb : result) {
            totalKeys++;
            String cpuValueStr = vb.getVariable().toString().trim();
            long cpuValue = Long.parseLong(cpuValueStr);
            totalCpuValue = totalCpuValue + cpuValue;
        }
        long finalCpuLoad = totalCpuValue / totalKeys;
        return finalCpuLoad;
    }

    /**
     * This method is used to get the disc space, physical memory and virtual memory of a asset
     * 
     * @param result
     * @param rootOid
     * @param variable
     * @param isUsed
     * @return
     */
    private long getMemorycalc(List<VariableBinding> result, OID rootOid, String variable, boolean isUsed) {

        String mulBytes = null;
        String mulBytesOID = null;
        String toMultiply = null;
        String toMultiplyOID = null;
        String usedMultiplyOID = null;
        String usedMultiply = null;
        String rootId = rootOid.toString();
        Long memory = 0L;

        for (VariableBinding vb : result) { // 1st for loop starts
            String temp = vb.getVariable().toString().trim();
            boolean isValid = false;
            
            if (temp.contains("/") && temp.trim().equals(variable)) {
                isValid = true;
            }
            else if (temp.contains(":\\") && temp.trim().contains(variable)) {
                isValid = true;
            }
            else if (!isValid && temp.trim().toLowerCase().equals(variable)) {
                isValid = true;
            }

            if (isValid) { // 2nd if loop starts
                String lastValue = String.valueOf(vb.getOid().last());
                mulBytesOID = rootId + "." + "4" + "." + lastValue;
                if (!isUsed) {
                    toMultiplyOID = rootId + "." + "5" + "." + lastValue;
                }
                else {
                    usedMultiplyOID = rootId + "." + "6" + "." + lastValue;
                }
                for (VariableBinding vbs : result) { // 2nd for loop starts
                    if (vbs.getOid().toString().trim().equals(mulBytesOID)) {
                        mulBytes = vbs.getVariable().toString().trim();
                    }
                    else if (!isUsed && vbs.getOid().toString().trim().equals(toMultiplyOID)) {
                        toMultiply = vbs.getVariable().toString().trim();
                    }
                    else if (isUsed && vbs.getOid().toString().trim().equals(usedMultiplyOID)) {
                        usedMultiply = vbs.getVariable().toString().trim();
                    }
                } // 2nd for loop ends
                if (!isUsed && mulBytes != null && toMultiply != null) {
                    memory = memory + Long.parseLong(mulBytes.trim()) * Long.parseLong(toMultiply.trim());
                }
                else if (isUsed && mulBytes != null && usedMultiply != null) {
                    memory = memory + Long.parseLong(mulBytes.trim()) * Long.parseLong(usedMultiply.trim());
                }
            } // 2nd if loop ends
        } // 1st for loop ends
        return memory;
    }

    /**
     * This method is used to get the Linux and Mac  network in and out bytes of a asset.
     * 
     * @param result
     * @param rootOid
     * @param ethernet
     * @param networkMap
     * @param assetId
     * @param sysDescr
     * @return
     */
    private HashMap<String, String> networkBytesCalc(List<VariableBinding> result, OID rootOid, String[] ethernet,
            HashMap<String, List<Long>> networkMap, String assetId, String sysDescription) {

        String networkInOid = null;
        String networkInStr = null;
        String networkOutOid = null;
        String networkOutStr = null;
        String macOid = null;
        String rootId = rootOid.toString();
        HashMap<String, HashMap<String, String>> macOidMap = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> networkValues = new HashMap<String, String>();
        
        for (int i = 0; i < ethernet.length; i++) { // 1st for loop starts
            for (VariableBinding vb : result) { // 2nd for loo starts
                if (vb.getVariable().toString().trim().equalsIgnoreCase(ethernet[i])) { // 1st if loop starts
                    String lastchar = String.valueOf(vb.getOid().last());

                    macOid = rootId + "." + "6" + "." + lastchar;

                    networkInOid = rootId + "." + "10" + "." + lastchar;

                    networkOutOid = rootId + "." + "16" + "." + lastchar;

                    for (VariableBinding vbs : result) { // 3rd for loop starts
                        if (networkInOid != null && vbs.getOid().toString().trim().equals(networkInOid)) {
                            networkInStr = vbs.getVariable().toString();
                            if (networkMap.get(ethernet[i] + "InBytes") == null
                                    || networkMap.get(ethernet[i] + "InBytes").isEmpty()
                                    || networkMap.get(ethernet[i] + "InBytes").size() == 0) {
                                List<Long> networkList = new ArrayList<Long>();

                                if (!networkInStr.trim().isEmpty() && networkInStr != null) {
                                    networkList.add(Long.parseLong(networkInStr));
                                    networkMap.put(ethernet[i] + "InBytes", networkList);
                                }
                                else {
                                    networkMap.get(ethernet[i] + "InBytes").add(Long.parseLong(networkInStr));
                                }
                            }

                        }
                        else if (networkOutOid != null && vbs.getOid().toString().trim().equals(networkOutOid)) {
                            networkOutStr = vbs.getVariable().toString();
                            if (networkMap.get(ethernet[i] + "OutBytes") == null
                                    || networkMap.get(ethernet[i] + "OutBytes").isEmpty()
                                    || networkMap.get(ethernet[i] + "OutBytes").size() == 0) {
                                List<Long> networkList = new ArrayList<Long>();

                                if (!networkOutStr.trim().isEmpty() && networkOutStr != null) {
                                    networkList.add(Long.parseLong(networkOutStr));
                                    networkMap.put(ethernet[i] + "OutBytes", networkList);
                                }
                                else {
                                    networkMap.get(ethernet[i] + "OutBytes").add(Long.parseLong(networkOutStr));
                                }
                            }

                        }
                        else if (vbs.getOid().toString().trim().equals(macOid)) {
                            if (macOidMap.get(ethernet[i]) == null || macOidMap.get(ethernet[i]).size() == 0
                                    || macOidMap.get(ethernet[i]).isEmpty()) {
                                HashMap<String, String> macMap = new HashMap<String, String>();
                                macMap.put(vbs.getOid().toString(), vbs.getVariable().toString());
                                macOidMap.put(ethernet[i], macMap);
                            }
                            else {
                                macOidMap.get(ethernet[i]).put(vbs.getOid().toString(), vbs.getVariable().toString());
                            }
                        }
                    } // 3rd for loop ends

                    // Check MAC address and return the max value of {"eth0", "eth1", "eth2", "en1" , "en2","en3",
                    // "em1", "em2" ,"em3", "wlan"} in the return values
                    Set<String> uniqueValues = new HashSet<String>(macOidMap.get(ethernet[i]).values());
                    if (macOidMap.get(ethernet[i]).size() > 1 && uniqueValues != null) {
                        if (uniqueValues.size() == 1) {
                            networkValues.put(ethernet[i] + "InBytes",
                                    String.valueOf(Collections.max(networkMap.get(ethernet[i] + "InBytes"))));
                            networkValues.put(ethernet[i] + "OutBytes",
                                    String.valueOf(Collections.max(networkMap.get(ethernet[i] + "OutBytes"))));
                        }
                        else if (uniqueValues.size() > 1) {
                        }
                    }
                    else if (macOidMap.get(ethernet[i]).size() == 1) {
                        networkValues.put(ethernet[i] + "InBytes",
                                String.valueOf(networkMap.get(ethernet[i] + "InBytes").get(0)));
                        networkValues.put(ethernet[i] + "OutBytes",
                                String.valueOf(networkMap.get(ethernet[i] + "OutBytes").get(0)));
                    }
                } // if loop ends
            } // 2nd for loop ends
        } // 1st for loop ends
        if( sysDescription.contains("mac") ){
        	
        	Set<String> uniqueValues = new HashSet<String>(macOidMap.get("en0").values());
        	if (macOidMap.get("eth0") != null && macOidMap.get("en0").size() != 0) {
        		String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1).trim()
                    .replaceAll(":", "");
        		assetId = eth0MacAddress;
        		networkValues.put("assetId", assetId);
        	}	
        }
        else {
        		if (macOidMap.get("eth0") != null && macOidMap.get("eth0").size() != 0) {
        			Set<String> uniqueValues = new HashSet<String>(macOidMap.get("eth0").values());
        			String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1).trim()
                    .replaceAll(":", "");
        			assetId = eth0MacAddress;
        			networkValues.put("assetId", assetId);
        		}
        }	
        return networkValues;
    } // network bytes calculation for Linux and Mac gets over

    /**
     * This method is used to get the Windows , network in and out bytes of a asset.
     * 
     * @param result
     * @param rootOid
     * @param winNetworkMap
     * @return
     */
    private HashMap<String, String> winNetworkBytesCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, String> winNetworkMap) {

        String networkInOid = null;
        String networkOutOid = null;
        String macWinNetworkId = null;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) { // 1st for loop starts
            String lastchar = String.valueOf(vb.getOid().last());

            networkInOid = rootId + "." + "10" + "." + lastchar;

            networkOutOid = rootId + "." + "16" + "." + lastchar;

            if (networkInOid != null && vb.getOid().toString().contains(networkInOid)) { // 1st if loop starts
                String winNetworkInVal = vb.getVariable().toString().trim();

                if (!winNetworkInVal.equalsIgnoreCase("0")) {
                    String winNetworkInValue = vb.getVariable().toString().trim();
                    winNetworkMap.put("InBytes", winNetworkInValue);

                    macWinNetworkId = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) { // 2nd for loop starts
                        if (macWinNetworkId != null && vbs.getOid().toString().contains(macWinNetworkId)) {
                            String macWinNetworkValue = vbs.getVariable().toString().trim().replaceAll(":", "");
                            winNetworkMap.put("macWinNetworkValue", macWinNetworkValue);
                        }
                    }// 2nd for loop ends
                } // 2nd if loop ends
            } // 1st if loop ends
            else if (networkOutOid != null && vb.getOid().toString().contains(networkOutOid)) {
                String winNetworkOutVal = vb.getVariable().toString().trim();
                if (!winNetworkOutVal.equalsIgnoreCase("0")) {
                    String winNetworkOutValue = vb.getVariable().toString().trim();
                    winNetworkMap.put("OutBytes", winNetworkOutValue);
                }
            } // else if loop ends
        } // 1st for loop ends
        return winNetworkMap;
    } // network bytes calculation for windows gets over.

    /**
     * This method is used to get the Linux Asset ID
     * 
     * @param result
     * @param rootOid
     * @param ethernet
     * @param networkMap
     * @param assetId
     * @param sysDescr
     * @return
     */
    private HashMap<String, String> linuxAssetIdCalc(List<VariableBinding> result, OID rootOid, String[] ethernet,
            HashMap<String, List<Long>> networkMap, String assetId, String sysDescription) {
        String macOid = null;
        String rootId = rootOid.toString();
        HashMap<String, HashMap<String, String>> macOidMap = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> networkValues = new HashMap<String, String>();

        for (int i = 0; i < ethernet.length; i++) { // 1st for loop starts
            for (VariableBinding vb : result) { // 2nd for loop starts
                if (vb.getVariable().toString().trim().equalsIgnoreCase(ethernet[i])) { // 1st if loop starts
                    String lastchar = String.valueOf(vb.getOid().last());

                    macOid = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) { // 3rd for loop starts

                        if (vbs.getOid().toString().trim().equals(macOid)) { // 2nd if loop starts
                            if (macOidMap.get(ethernet[i]) == null || macOidMap.get(ethernet[i]).size() == 0
                                    || macOidMap.get(ethernet[i]).isEmpty()) {
                                HashMap<String, String> macMap = new HashMap<String, String>();
                                macMap.put(vbs.getOid().toString(), vbs.getVariable().toString());
                                macOidMap.put(ethernet[i], macMap);
                            }
                            else {
                                macOidMap.get(ethernet[i]).put(vbs.getOid().toString(), vbs.getVariable().toString());
                            }
                        } // 2nd if loop ends
                    } // 3rd for loop ends
                } // 1st if loop ends
            } // 2nd for loop ends
           
        } // 1st for loop ends
        	//Asset Id calculations are based on ethernet values.
        	for (int i = 0; i < ethernet.length; i++) {
        		if (macOidMap.get(ethernet[i]) != null && macOidMap.get(ethernet[i]).size() != 0) {
        			Set<String> uniqueValues = new HashSet<String>(macOidMap.get(ethernet[i]).values());
        			String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1).trim()
        			.replaceAll(":", "");
        			assetId = eth0MacAddress;
        			networkValues.put("assetId", assetId);
        		}
        	}
        return networkValues;
    }
    
    /**
     * This method is used to calculate Mac Asset ID
     * 
     * @param result
     * @param rootOid
     * @param macNetworkMap
     * @return
     */
    private HashMap<String, String> macAssetIdCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, String> macNetworkMap) {

        String networkOid = null;
        String macWinNetworkId = null;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) { // 1st for loop starts
            String lastchar = String.valueOf(vb.getOid().last());
            networkOid = rootId + "." + "10" + "." + lastchar;

            if (networkOid != null && vb.getOid().toString().contains(networkOid)) { // 1st if loop starts
                String macNetworkInVal = vb.getVariable().toString().trim();

                if (!macNetworkInVal.equalsIgnoreCase("0")) { // 2nd if loop starts
                    String winNetworkInValue = vb.getVariable().toString().trim();
                    macNetworkMap.put("InBytes", winNetworkInValue);

                    macWinNetworkId = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) { // 2nd for loop starts
                        if (macWinNetworkId != null && vbs.getOid().toString().contains(macWinNetworkId)) {
                            String macWinNetworkValue = vbs.getVariable().toString().trim().replaceAll(":", "");
                            macNetworkMap.put("macWinNetworkValue", macWinNetworkValue);
                        }
                    }// 2nd for loop ends
                } // 2nd if loop ends
            } // 1st if loop ends
        }// 1st for loop ends
        return macNetworkMap;
    }

    /**
     * This method is used to Windows Asset ID
     * 
     * @param result
     * @param rootOid
     * @param winNetworkMap
     * @return
     */
    private HashMap<String, String> winAssetIdCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, String> winNetworkMap) {

        String networkOid = null;
        String macWinNetworkId = null;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) { // 1st for loop starts
            String lastchar = String.valueOf(vb.getOid().last());
            networkOid = rootId + "." + "10" + "." + lastchar;

            if (networkOid != null && vb.getOid().toString().contains(networkOid)) { // 1st if loop starts
                String winNetworkInVal = vb.getVariable().toString().trim();

                if (!winNetworkInVal.equalsIgnoreCase("0")) { // 2nd if loop starts
                    String winNetworkInValue = vb.getVariable().toString().trim();
                    winNetworkMap.put("InBytes", winNetworkInValue);

                    macWinNetworkId = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) { // 2nd for loop starts
                        if (macWinNetworkId != null && vbs.getOid().toString().contains(macWinNetworkId)) {
                            String macWinNetworkValue = vbs.getVariable().toString().trim().replaceAll(":", "");
                            winNetworkMap.put("macWinNetworkValue", macWinNetworkValue);
                        }
                    }// 2nd for loop ends
                } // 2nd if loop ends
            } // 1st if loop ends
        }// 1st for loop ends
        return winNetworkMap;
    }

    /**
     * This method is used to get the installed software list of a asset.
     * 
     * @param appResult
     * @param softwareResult
     * @param dateResult
     * @param rootOid
     * @param isWindows
     * @return
     */
    private ArrayList<CompInstSoftware> installedSwListCalc(List<VariableBinding> appResult,
            List<VariableBinding> softwareResult, List<VariableBinding> dateResult, OID rootOid, boolean isWindows,
            boolean isLinux,boolean isMac, CPNId id) {

        Long runId = id.getRunId();
        String assetId = id.getAssetId();

        ArrayList<CompInstSoftware> installedSwList = new ArrayList<CompInstSoftware>(appResult.size());
        CompInstSoftwareId ins = null;
        
        try {
        	//calculations done on either windows or mac.
            if (isWindows || isMac) {
                for (int i = 0; i < appResult.size(); i++) { // for loop starts
                    if (appResult.get(i).getVariable().toString().trim().equals("4")) { // 1st if loop starts
                        String softwareName = softwareResult.get(i).getVariable().toString().trim();
                        Date installDate = getDate(dateResult.get(i).getVariable().toString().trim(), isWindows);
                        if (softwareName != null && softwareName.trim().length() != 0) { // 2nd if loop starts
                            int SoftwareLength = softwareName.length();
                            if (SoftwareLength >= 100) {
                                softwareName = softwareName.substring(0, 99);
                            }
                            ins = new CompInstSoftwareId(runId, assetId, softwareName, installDate);
                            CompInstSoftware cis = new CompInstSoftware(ins);
                            installedSwList.add(cis);
                        } // 2nd if loop ends
                    } // 1st if loop ends
                } // for loop ends
            }

            if (isLinux) {
                for (int appResultCount = 0; appResultCount < appResult.size(); appResultCount++) { // for loop starts
                    if (appResult.get(appResultCount).getVariable().toString().trim().equals("4")) {

                        String softwareName = softwareResult.get(appResultCount).getVariable().toString().trim();
                        Date installDate = getDate(dateResult.get(appResultCount).getVariable().toString().trim(),
                                isLinux);
                        String[] softwareNameTokens = softwareName.split("-");
                        String finalSoftwareName = "";
                        for (int count = 0; count < softwareNameTokens.length; count++) {
                            if ((softwareNameTokens[count].charAt(0) >= 65 && softwareNameTokens[count].charAt(0) <= 91)
                                    || (softwareNameTokens[count].charAt(0) >= 97 && softwareNameTokens[count]
                                            .charAt(0) <= 123)) {
                                finalSoftwareName += softwareNameTokens[count] + "-";
                            }
                        }

                        finalSoftwareName = finalSoftwareName.substring(0, finalSoftwareName.lastIndexOf("-"));
                        if (!MeterConstants.InstSwMap.containsKey(finalSoftwareName)) {
                            if (softwareName != null && softwareName.trim().length() != 0) { // 2nd if loop starts
                                int SoftwareLength = softwareName.length();
                                if (SoftwareLength >= 100) {
                                    softwareName = softwareName.substring(0, 99);
                                }
                                ins = new CompInstSoftwareId(runId, assetId, softwareName, installDate);
                                CompInstSoftware cis = new CompInstSoftware(ins);
                                installedSwList.add(cis);
                            }// 2nd if loop ends
                        } // 1st if loop ends
                    }
                }// for ends
            }// if ends
        }// try ends
        catch (Exception e) {
            e.printStackTrace();
        }
        return installedSwList;
    }

    /**
     * This method is used to get the Date of a asset
     * 
     * @param hexDate
     * @param isWindows
     * @return
     */
    private static Date getDate(String hexDate, boolean isWindows) {
        Date newDate = null;
        
        if (!isWindows) {
            hexDate = hexDate.substring(0, hexDate.length() - 12);
        }
        
        String year = hexDate.split(":")[0] + hexDate.split(":")[1];
        int intOfYear = Integer.parseInt(year, 16);
        String yearStr = Integer.toString(intOfYear);

        String month = hexDate.split(":")[2];
        int intOfMonth = Integer.parseInt(month, 16);
        String monthStr = Integer.toString(intOfMonth);

        String day = hexDate.split(":")[3];
        int intOfDay = Integer.parseInt(day, 16);
        String dayStr = Integer.toString(intOfDay);

        String hour = hexDate.split(":")[4];
        int intOfHour = Integer.parseInt(hour, 16);
        String hourStr = Integer.toString(intOfHour);

        String minute = hexDate.split(":")[5];
        int intOfMinute = Integer.parseInt(minute, 16);
        String minuteStr = Integer.toString(intOfMinute);

        String second = hexDate.split(":")[6];
        int intOfSecond = Integer.parseInt(second, 16);
        String secondStr = Integer.toString(intOfSecond);

        String datestr = yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr + ":" + secondStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            newDate = sdf.parse(datestr);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * This method is used to get the Connected Device of a asset
     * 
     * @param connDeviceIPResult
     * @param connDevicePortResult
     * @param ipAddress
     * @param AssetId
     * @return
     */
    private HashSet<CompConnDevice> ConnectedDevicesCalc(List<VariableBinding> connDeviceIPResult, List<VariableBinding> connDevicePortResult,
            String ipAddress, CPNId id) {

        HashSet<CompConnDevice> connectedDevices = new HashSet<CompConnDevice>();

        CompConnDevice connDevice = null;
        Long runId = id.getRunId();
        String assetId = id.getAssetId();
        CompConnDeviceId compConnDeviceId = null;
        
        String ip[] = new String[connDeviceIPResult.size()];
        int port[] = new int[connDevicePortResult.size()];
        String ip_addr;
        int ip_port, ipCount = 0;
        
        // Getting IPaddress from List
        for (VariableBinding vb : connDeviceIPResult) {
            ip_addr = vb.getVariable().toString();
            ip[ipCount] = ip_addr;
            ipCount++;
        }
        ipCount = 0;
        
        // Getting ports from List
        for (VariableBinding vb : connDevicePortResult) {
            ip_port = Integer.parseInt(vb.getVariable().toString());
            port[ipCount] = ip_port;
            ipCount++;
        }
        // Used to get the IPaddress and port from the SNMPwalkresult based on the condition
        int portNum = 0;
        for (ipCount = 0; ipCount < connDeviceIPResult.size(); ipCount++) {
            if (!ip[ipCount].equals("0.0.0.0") && !ip[ipCount].equals("127.0.0.1") && !ip[ipCount].equals(ipAddress) ) {
            	if (port[ipCount] <= 12000) {
            		portNum = port[ipCount];
            	}
            	else {
            		portNum = 0;
            	}	
            }
            if(ip[ipCount].equals("0.0.0.0")) {
            	continue;
            }
        	// figure out the host name of the machine as well and insert it in another column
            compConnDeviceId = new CompConnDeviceId(runId, assetId, ip[ipCount], portNum, MeterUtils.getTrimmedHostName(ip[ipCount]));
            connDevice = new CompConnDevice(compConnDeviceId);
            connectedDevices.add(connDevice);
        }
        return connectedDevices;
    }

    /**
     * This method is used to get the running process of a asset
     * 
     * @param result
     * @param rootOid
     * @param sysRunNameResult
     * @param cpuShareResult
     * @param memShareResult
     * @return
     */
    private ArrayList<CompProcess> ProcessCalc(List<VariableBinding> result, OID rootOid,
            List<VariableBinding> sysRunNameResult, List<VariableBinding> cpuShareResult,
            List<VariableBinding> memShareResult, CPNId id) {

        Long runId = id.getRunId();
        String assetId = id.getAssetId();
        int processSize = sysRunNameResult.size();
        
        ArrayList<CompProcess> processList = new ArrayList<CompProcess>(processSize);
        CompProcess process = null;
        CompProcessId compProcessId = null;
        String runName = null;
        int cpuShare = 0;
        int memShare = 0;
        
        for (int i = 0; i < processSize; i++) { // for loop starts
            
        	runName = sysRunNameResult.get(i).getVariable().toString().trim();
            cpuShare = Integer.parseInt(cpuShareResult.get(i).getVariable().toString().trim());
            memShare = Integer.parseInt(memShareResult.get(i).getVariable().toString().trim());
            
            if (runName != null && runName.trim().length() != 0) { // if loop starts
                
            	int runNameLength = runName.length();
                
                if (runNameLength >= 45) {
                    runName = runName.substring(0, 44);
                }
                compProcessId = new CompProcessId(runId, assetId, runName, cpuShare, memShare);
                process = new CompProcess(compProcessId);
                processList.add(process);
            } // if loop ends
        }// for loop ends
        return processList;
    }
}
