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

import com.gq.meter.object.Computer;
import com.gq.meter.object.assist.InstalledSoftware;
import com.gq.meter.object.assist.Process;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterProtocols;
import com.gq.meter.util.MeterUtils;

public class ComputerMeter implements GQSNMPMeter {

    List<String> errorList = new LinkedList<String>();

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches) {

        long computerstartTime = System.currentTimeMillis();

        Snmp snmp = null;
        // ASSET
        String assetId = null; // unique identifier about the asset
        String sysName = null; // string
        String sysDescr = null; // string
        String sysContact = null; // string
        String sysLocation = null; // string
        String extras = null; // anything device specific but to be discussed v2

        // SNAPSHOT
        String sysIP = null; // string
        long cpuLoad = 0; // in percentage
        long totalMemory = 0; // bytes
        long usedMemory = 0; // bytes
        long totalVirtualMemory = 0; // bytes
        long usedVirtualMemory = 0; // bytes
        long totalDiskSpace = 0; // bytes
        long usedDiskSpace = 0; // bytes
        long upTime = 0; // seconds
        long numLoggedInUsers = 0;
        long numProcesses = 0;
        long networkBytesIn = 0; // bytes
        long networkBytesOut = 0; // bytes
        double clockSpeed = 0; // v2

        CommunityTarget target = null;
        HashMap<String, String> networkBytes = null;
        List<InstalledSoftware> installedSwList = null;
        HashSet<String> connectedDevices = null;
        List<Process> ProcessList = null;

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
            boolean isWindows = false;

            OID rootOID = new OID(oidString);
            List<VariableBinding> result = null;

            result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is v2

            if (result != null && !result.isEmpty()) {
                temp = oidString + ".1.0";
                sysDescr = MeterUtils.getSNMPValue(temp, result);

                if (null != sysDescr) {
                    if (sysDescr.contains("Windows")) {
                        isWindows = true;
                    }
                }
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

            // the following oid's is used to get the asset id for windows.
            if (isWindows) {
                oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);

                if (result != null && !result.isEmpty()) {
                    HashMap<String, String> winNetworkMap = new HashMap<String, String>();
                    networkBytes = winAssetIdCalc(result, rootOID, winNetworkMap);

                    assetId = MeterProtocols.COMPUTER + "-" + networkBytes.get("macWinNetworkValue");

                }
            }// if ends

            // the following oid's is used to get the asset id for linux.
            else {
                oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);

                if (result != null && !result.isEmpty()) {
                    String[] ethernet = new String[] { "eth0", "eth1", "eth2", "en1", "en2", "en3", "em1", "em2",
                            "em3", "wlan" };

                    HashMap<String, List<Long>> networkMap = new HashMap<String, List<Long>>();
                    networkBytes = linuxAssetIdCalc(result, rootOID, ethernet, networkMap, assetId, sysDescr);
                    assetId = MeterProtocols.COMPUTER + "-" + networkBytes.get("assetId");
                }
                else {
                    errorList.add("Root OID : 1.3.6.1.2.1.2.2.1" + " "
                            + "Unable to get network bandwidth details and unable to collate asset ID");
                }
            }// else ends

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

                    // The following oid's is used to get no. of users and processes
                    oidString = "1.3.6.1.2.1.25.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        temp = oidString + ".5.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        if (tempStr != null) {
                            numLoggedInUsers = Integer.parseInt(tempStr);
                        }

                        temp = oidString + ".6.0";
                        tempStr = MeterUtils.getSNMPValue(temp, result);
                        if (tempStr != null) {
                            numProcesses = Integer.parseInt(tempStr);
                        }
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.25.1" + " "
                                + "Unable to get number of users and processes");
                    }

                    // The following oid's is used to get disc space, physical memory, virtual memory

                    oidString = "1.3.6.1.2.1.25.2.3.1";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        String variable = null;

                        if (isWindows) {
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

                        }// if ends
                        else {
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
                            else {
                                usedMemory = getMemorycalc(result, rootOID, variable, true);
                            }
                        } // else loop ends
                    }
                    else {
                        errorList
                                .add("Root OID : 1.3.6.1.2.1.25.2.3.1" + " " + "Unable to get disk and memory details");
                    }

                    // The following oid's is used to get CPU load
                    oidString = ".1.3.6.1.2.1.25.3.3.1.2";
                    rootOID = new OID(oidString);
                    result = MeterUtils.walk(rootOID, target);

                    if (result != null && !result.isEmpty()) {
                        cpuLoad = cpuLoadCalc(result, rootOID);
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.25.3.3.1.2" + " " + "Unable to compute CPU load");
                    }

                    // the following oid's is used to get network in and out bytes for windows
                    if (isWindows) {
                        oidString = ".1.3.6.1.2.1.2.2.1";
                        rootOID = new OID(oidString);
                        result = MeterUtils.walk(rootOID, target);

                        if (result != null && !result.isEmpty()) {
                            HashMap<String, String> winNetworkMap = new HashMap<String, String>();
                            networkBytes = winNetworkBytesCalc(result, rootOID, winNetworkMap);

                            String networkBytesInStr = networkBytes.get("InBytes");
                            networkBytesIn = Long.parseLong(networkBytesInStr);

                            String networkBytesOutStr = networkBytes.get("OutBytes");
                            networkBytesOut = Long.parseLong(networkBytesOutStr);
                            // assetId = MeterProtocols.COMPUTER + "-" + networkBytes.get("macWinNetworkValue");
                        }
                        else {
                            errorList.add("Root OID : 1.3.6.1.2.1.2.2.1" + " "
                                    + "Unable to get network bandwidth details and unable to collate asset ID");
                        }
                    }// if ends

                    // the following oid's is used to get network in and out bytes for Linux
                    else {
                        oidString = ".1.3.6.1.2.1.2.2.1";
                        rootOID = new OID(oidString);
                        result = MeterUtils.walk(rootOID, target);

                        if (result != null && !result.isEmpty()) {
                            String[] ethernet = new String[] { "eth0", "eth1", "eth2", "en1", "en2", "en3", "em1",
                                    "em2", "em3", "wlan" };

                            HashMap<String, List<Long>> networkMap = new HashMap<String, List<Long>>();
                            networkBytes = networkBytesCalc(result, rootOID, ethernet, networkMap, assetId, sysDescr);
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
                              // assetId = MeterProtocols.COMPUTER + "-" + networkBytes.get("assetId");
                        }
                        else {
                            errorList.add("Root OID : 1.3.6.1.2.1.2.2.1" + " "
                                    + "Unable to get network bandwidth details  and unable to collate asset ID");
                        }
                    }
                }

                // the following oid's is used to get the installed software list
                if (element.equalsIgnoreCase(MeterConstants.FULL_DETAILS)
                        || element.equalsIgnoreCase(MeterConstants.INSTALLED_SOFTWARE)) {

                    oidString = ".1.3.6.1.2.1.25.6.3.1.4";
                    rootOID = new OID(oidString);
                    List<VariableBinding> appResult = MeterUtils.walk(rootOID, target);

                    if (appResult != null && !appResult.isEmpty()) {
                        oidString = ".1.3.6.1.2.1.25.6.3.1.2";
                        rootOID = new OID(oidString);
                        List<VariableBinding> softwareResult = MeterUtils.walk(rootOID, target);

                        oidString = ".1.3.6.1.2.1.25.6.3.1.5";
                        rootOID = new OID(oidString);
                        List<VariableBinding> dateResult = MeterUtils.walk(rootOID, target);

                        installedSwList = installedSwListCalc(appResult, softwareResult, dateResult, rootOID, isWindows);
                    }
                    else {
                        errorList.add("Root OID : 1.3.6.1.2.1.25.6.3.1.4" + " "
                                + "Unable to get list of installed software");
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

                // The following OID is used to get the System run name, cpu and memory share for a particular process .
                if (element.equalsIgnoreCase(MeterConstants.FULL_DETAILS)
                        || element.equalsIgnoreCase(MeterConstants.PROCESS)) {
                    if (result != null && !result.isEmpty()) {
                        oidString = ".1.3.6.1.2.1.25.4.2.1.2";
                        rootOID = new OID(oidString);
                        List<VariableBinding> sysRunNameResult = MeterUtils.walk(rootOID, target);

                        oidString = ".1.3.6.1.2.1.25.5.1.1.1";
                        rootOID = new OID(oidString);
                        List<VariableBinding> cpuShareResult = MeterUtils.walk(rootOID, target);

                        oidString = ".1.3.6.1.2.1.25.5.1.1.2";
                        rootOID = new OID(oidString);
                        List<VariableBinding> memShareResult = MeterUtils.walk(rootOID, target);

                        ProcessList = ProcessCalc(result, rootOID, sysRunNameResult, cpuShareResult, memShareResult);
                    }
                    else {
                        errorList.add("Root OID : .1.3.6.1.2.1.25" + " "
                                + "Unable to get the system run name, cpu and memory share");
                    }
                }
            }// for loop ends
        }
        catch (Exception e) {
            errorList.add(ipAddress + " " + e.getMessage());
        }

        Computer compObject = new Computer(assetId, cpuLoad, totalMemory, usedMemory, totalVirtualMemory,
                usedVirtualMemory, totalDiskSpace, usedDiskSpace, upTime, numLoggedInUsers, numProcesses,
                networkBytesIn, networkBytesOut, clockSpeed, sysName, sysIP, sysDescr, sysContact, sysLocation, extras,
                installedSwList, connectedDevices, ProcessList);

        GQErrorInformation gqErrorInfo = null;
        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(sysDescr, errorList);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, compObject);
        long computerendTime = System.currentTimeMillis();
        MeterUtils.compMeterTime = MeterUtils.compMeterTime + (computerendTime - computerstartTime);
        System.out.println("Time taken bye the computer meter is : " + (computerendTime - computerstartTime));
        return gqMeterObject;

    }

    /**
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

        for (VariableBinding vb : result) {
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

            if (isValid) {
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
                }
                if (!isUsed && mulBytes != null && toMultiply != null) {
                    memory = memory + Long.parseLong(mulBytes.trim()) * Long.parseLong(toMultiply.trim());
                }
                else if (isUsed && mulBytes != null && usedMultiply != null) {
                    memory = memory + Long.parseLong(mulBytes.trim()) * Long.parseLong(usedMultiply.trim());
                }
                else {
                    errorList.add(MeterConstants.NO_VALUE + " " + memory);
                }
            } // if loop ends
        } // for loop ends
        return memory;
    }

    /**
     * @param result
     * @param rootOid
     * @param ethernet
     * @param networkMap
     * @param assetId
     * @param sysDescr
     * @return
     */
    private HashMap<String, String> networkBytesCalc(List<VariableBinding> result, OID rootOid, String[] ethernet,
            HashMap<String, List<Long>> networkMap, String assetId, String sysDescr) {

        String networkInOid = null;
        String networkInStr = null;
        String networkOutOid = null;
        String networkOutStr = null;
        String macOid = null;
        String rootId = rootOid.toString();
        HashMap<String, HashMap<String, String>> macOidMap = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> networkValues = new HashMap<String, String>();
        for (int i = 0; i < ethernet.length; i++) {
            for (VariableBinding vb : result) {
                if (vb.getVariable().toString().trim().equalsIgnoreCase(ethernet[i])) {
                    String lastchar = String.valueOf(vb.getOid().last());

                    macOid = rootId + "." + "6" + "." + lastchar;

                    networkInOid = rootId + "." + "10" + "." + lastchar;

                    networkOutOid = rootId + "." + "16" + "." + lastchar;

                    for (VariableBinding vbs : result) {
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
                            else {
                                errorList.add(MeterConstants.NO_VALUE + networkInStr);
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
                            else {
                                errorList.add(MeterConstants.NO_VALUE + networkOutStr);
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
                    }

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

        Set<String> uniqueValues = new HashSet<String>(macOidMap.get("eth0").values());
        if (macOidMap.get("eth0") != null && macOidMap.get("eth0").size() != 0) {
            String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1).trim()
                    .replaceAll(":", "");
            assetId = eth0MacAddress;
            networkValues.put("assetId", assetId);
        }
        return networkValues;
    } // network bytes calculation for Linux gets over

    /**
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

        for (VariableBinding vb : result) {
            String lastchar = String.valueOf(vb.getOid().last());

            networkInOid = rootId + "." + "10" + "." + lastchar;

            networkOutOid = rootId + "." + "16" + "." + lastchar;

            if (networkInOid != null && vb.getOid().toString().contains(networkInOid)) {
                String winNetworkInVal = vb.getVariable().toString().trim();

                if (!winNetworkInVal.equalsIgnoreCase("0")) {
                    String winNetworkInValue = vb.getVariable().toString().trim();
                    winNetworkMap.put("InBytes", winNetworkInValue);

                    macWinNetworkId = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) {
                        if (macWinNetworkId != null && vbs.getOid().toString().contains(macWinNetworkId)) {
                            String macWinNetworkValue = vbs.getVariable().toString().trim().replaceAll(":", "");
                            winNetworkMap.put("macWinNetworkValue", macWinNetworkValue);
                        }
                    }// for loop ends
                } // 2nd if loop ends

            }
            else if (networkOutOid != null && vb.getOid().toString().contains(networkOutOid)) {
                String winNetworkOutVal = vb.getVariable().toString().trim();
                if (!winNetworkOutVal.equalsIgnoreCase("0")) {
                    String winNetworkOutValue = vb.getVariable().toString().trim();
                    winNetworkMap.put("OutBytes", winNetworkOutValue);
                }
            }

        } // for loop ends
        return winNetworkMap;

    } // network bytes calculation for windows gets over.

    /**
     * @param result
     * @param rootOid
     * @param ethernet
     * @param networkMap
     * @param assetId
     * @param sysDescr
     * @return
     */
    private HashMap<String, String> linuxAssetIdCalc(List<VariableBinding> result, OID rootOid, String[] ethernet,
            HashMap<String, List<Long>> networkMap, String assetId, String sysDescr) {
        String macOid = null;
        String rootId = rootOid.toString();
        HashMap<String, HashMap<String, String>> macOidMap = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> networkValues = new HashMap<String, String>();

        for (int i = 0; i < ethernet.length; i++) {
            for (VariableBinding vb : result) {
                if (vb.getVariable().toString().trim().equalsIgnoreCase(ethernet[i])) {
                    String lastchar = String.valueOf(vb.getOid().last());

                    macOid = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) {

                        if (vbs.getOid().toString().trim().equals(macOid)) {
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

                    }
                }
            }
        }// for loop ends

        Set<String> uniqueValues = new HashSet<String>(macOidMap.get("eth0").values());
        if (macOidMap.get("eth0") != null && macOidMap.get("eth0").size() != 0) {
            String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1).trim()
                    .replaceAll(":", "");
            assetId = eth0MacAddress;
            networkValues.put("assetId", assetId);
        }
        return networkValues;

    }

    /**
     * @param result
     * @param rootOid
     * @param winNetworkMap
     * @return
     */
    private HashMap<String, String> winAssetIdCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, String> winNetworkMap) {

        String macWinNetworkId = null;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) {
            String lastchar = String.valueOf(vb.getOid().last());

            macWinNetworkId = rootId + "." + "6" + "." + lastchar;
            for (VariableBinding vbs : result) {
                if (macWinNetworkId != null && vbs.getOid().toString().contains(macWinNetworkId)) {
                    String macWinNetworkValue = vbs.getVariable().toString().trim().replaceAll(":", "");
                    winNetworkMap.put("macWinNetworkValue", macWinNetworkValue);
                }
            }// for loop ends
        }// for loop ends
        return winNetworkMap;
    }

    /**
     * @param appResult
     * @param softwareResult
     * @param dateResult
     * @param rootOid
     * @param isWindows
     * @return
     */
    private List<InstalledSoftware> installedSwListCalc(List<VariableBinding> appResult,
            List<VariableBinding> softwareResult, List<VariableBinding> dateResult, OID rootOid, boolean isWindows) {

        LinkedList<InstalledSoftware> installedSwList = new LinkedList<InstalledSoftware>();
        InstalledSoftware ins = null;
        try {
            for (int i = 0; i < appResult.size(); i++) {
                if (appResult.get(i).getVariable().toString().trim().equals("4")) {
                    String softwareName = softwareResult.get(i).getVariable().toString().trim();
                    Date installDate = getDate(dateResult.get(i).getVariable().toString().trim(), isWindows);
                    ins = new InstalledSoftware(softwareName, installDate);
                    installedSwList.add(ins);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return installedSwList;
    }

    /**
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

    /**
     * @param result
     * @param rootOid
     * @param sysRunNameResult
     * @param cpuShareResult
     * @param memShareResult
     * @return
     */
    private List<Process> ProcessCalc(List<VariableBinding> result, OID rootOid,
            List<VariableBinding> sysRunNameResult, List<VariableBinding> cpuShareResult,
            List<VariableBinding> memShareResult) {

        LinkedList<Process> ProcessList = new LinkedList<Process>();
        Process process = null;
        String runName = null;
        int cpuShare = 0;
        int memShare = 0;
        for (int i = 0; i < sysRunNameResult.size(); i++) {
            runName = sysRunNameResult.get(i).getVariable().toString().trim();
            cpuShare = Integer.parseInt(cpuShareResult.get(i).getVariable().toString().trim());
            memShare = Integer.parseInt(memShareResult.get(i).getVariable().toString().trim());
            process = new Process(runName, cpuShare, memShare);
            ProcessList.add(process);
        }
        return ProcessList;
    }
}
