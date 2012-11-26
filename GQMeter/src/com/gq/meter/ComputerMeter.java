package com.gq.meter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.Computer;
import com.gq.meter.object.assist.InstalledSoftware;
import com.gq.meter.util.MeterUtils;

public class ComputerMeter implements GQMeter {
    Snmp snmp = null;
    String protocolAddress = null;
    String communityString = null;

    private static String assetId; // uniq identifier abt the asset

    private static long cpuLoad; // in percentage
    private static long totalMemory; // bytes
    private static long usedMemory; // bytes
    private static long totalVirtualMemory; // bytes
    private static long usedVirtualMemory; // bytes
    private static long totalDiskSpace; // bytes
    private static long usedDiskSpace; // bytes
    private static long uptime; // seconds
    private static long numLoggedInUsers;
    private static long numProcesses;
    private static long networkBytesIn; // bytes
    private static long networkBytesOut; // bytes

    private static double clockSpeed; // v2

    private static String sysName;
    private static String sysIP; // string
    private static String sysDescr;
    private static String sysContact;
    private static String sysLocation; // string
    private static String extras; // anything device specific

    public static final String SNMP_VERSION_1 = "v1";
    public static final String SNMP_VERSION_2 = "v2c";
    public static final String SNMP_VERSION_3 = "v3";

    HashMap<String, String> networkBytes;

    @Override
    public Object implement(String communityString, String ipAddress, CommunityTarget target) {
        sysIP = ipAddress;
        if (communityString != null && ipAddress != null) {
            try {
                makeSnmpListen(ipAddress, communityString);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            // The following oid's is used to get system parameters

            String oidString = "1.3.6.1.2.1.1";
            String temp;
            String tempStr;
            boolean isLinux = false;
            boolean isWindows = false;

            OID rootOID = new OID(oidString);
            List<VariableBinding> result = null;

            result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is v2
            // System.out.println("result : " + result);

            temp = oidString + ".1.0";
            sysDescr = getSNMPValue(temp, result);
            System.out.println("System Description : " + sysDescr);

            if (null != sysDescr) {
                if (sysDescr.contains("Linux")) {
                    isLinux = true;
                }
                else if (sysDescr.contains("Windows")) {
                    isWindows = true;
                }
            }

            temp = oidString + ".3.0";
            tempStr = getSNMPValue(temp, result);
            System.out.println("Uptime : " + tempStr);
            uptime = upTimeCalc(tempStr);
            System.out.println("Uptime in seconds : " + uptime);

            temp = oidString + ".4.0";
            sysContact = getSNMPValue(temp, result);
            System.out.println("System Contact : " + sysContact);

            temp = oidString + ".5.0";
            sysName = getSNMPValue(temp, result);
            System.out.println("System Name : " + sysName);

            temp = oidString + ".6.0";
            sysLocation = getSNMPValue(temp, result);
            System.out.println("System Location : " + sysLocation);

            // The following oid's is used to get no. of users and processes

            oidString = "1.3.6.1.2.1.25.1";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            temp = oidString + ".5.0";
            tempStr = getSNMPValue(temp, result);
            numLoggedInUsers = Integer.parseInt(tempStr);
            System.out.println("Number logged in users : " + numLoggedInUsers);

            temp = oidString + ".6.0";
            tempStr = getSNMPValue(temp, result);
            numProcesses = Integer.parseInt(tempStr);
            System.out.println("Number of processes : " + numProcesses);

            // The following oid's is used to get disc space, physical memory, virtual memory

            oidString = "1.3.6.1.2.1.25.2.3.1";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            String variable = null;

            if (isWindows) {
                variable = ":\\";
                long windowsDriveSize = getMemorycalc(result, rootOID, variable, false);

                long usedWindowsDriveSize = getMemorycalc(result, rootOID, variable, true);

                variable = "physical memory";
                totalMemory = getMemorycalc(result, rootOID, variable, false);
                System.out.println("Total Memory : " + totalMemory);

                usedMemory = getMemorycalc(result, rootOID, variable, true);
                System.out.println("Used Memory : " + usedMemory);

                variable = "virtual memory";
                totalVirtualMemory = getMemorycalc(result, rootOID, variable, false);
                System.out.println("Total Virtual Memory : " + totalVirtualMemory);

                usedVirtualMemory = getMemorycalc(result, rootOID, variable, true);
                System.out.println("Used Virtual Memory : " + usedVirtualMemory);

                totalDiskSpace = windowsDriveSize + totalVirtualMemory;
                System.out.println("Total Disk Space : " + totalDiskSpace);

                usedDiskSpace = usedWindowsDriveSize + usedVirtualMemory;
                System.out.println("Used Disk Space : " + usedDiskSpace);

            }
            else if (isLinux) {
                variable = "/dev/shm";
                long linuxDriveSize = getMemorycalc(result, rootOID, variable, false);

                long usedLinuxDriveSize = getMemorycalc(result, rootOID, variable, true);

                variable = "/home";
                long homeSize = getMemorycalc(result, rootOID, variable, false);

                long usedHomeSize = getMemorycalc(result, rootOID, variable, true);

                variable = "swap space";
                totalVirtualMemory = getMemorycalc(result, rootOID, variable, false);
                System.out.println("Total Virtual Memory : " + totalVirtualMemory);

                usedVirtualMemory = getMemorycalc(result, rootOID, variable, true);
                System.out.println("Used Virtual Memory : " + usedVirtualMemory);

                variable = "/";
                long DiskSpace1 = getMemorycalc(result, rootOID, variable, false);

                long usedtotalDiskSpace1 = getMemorycalc(result, rootOID, variable, true);

                variable = "/boot";
                long DiskSpace2 = getMemorycalc(result, rootOID, variable, false);

                long UsedtotalDiskSpace2 = getMemorycalc(result, rootOID, variable, true);

                totalDiskSpace = linuxDriveSize + totalVirtualMemory + DiskSpace1 + DiskSpace2 + homeSize;
                System.out.println("Total Disk Space : " + totalDiskSpace);

                usedDiskSpace = usedLinuxDriveSize + usedVirtualMemory + usedtotalDiskSpace1 + UsedtotalDiskSpace2
                        + usedHomeSize;
                System.out.println("Used Disk Space : " + usedDiskSpace);

                variable = "real memory";
                totalMemory = getMemorycalc(result, rootOID, variable, false);

                if (totalMemory == 0L) {
                    variable = "physical memory";
                    totalMemory = getMemorycalc(result, rootOID, variable, false);
                    System.out.println("Total Memory : " + totalMemory);

                    usedMemory = getMemorycalc(result, rootOID, variable, true);
                    System.out.println("Used Memory : " + usedMemory);
                }
                else {
                    System.out.println("Total Memory : " + totalMemory);
                    usedMemory = getMemorycalc(result, rootOID, variable, true);
                    System.out.println("Used Memory : " + usedMemory);
                }
            } // Linux if loop ends

            // The following oid's is used to get cpu load

            oidString = ".1.3.6.1.2.1.25.3.3.1.2";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            cpuLoad = cpuLoadCalc(result, rootOID);
            System.out.println("CPU Load : " + cpuLoad);

            // the following oid's is used to get network in and out bytes for windows
            if (isWindows) {
                oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);

                HashMap<String, String> winNetworkMap = new HashMap<String, String>();
                networkBytes = winNetworkBytesCalc(result, rootOID, winNetworkMap);

                String networkBytesInStr = networkBytes.get("InBytes");
                networkBytesIn = Long.parseLong(networkBytesInStr);
                System.out.println("Network Bytes In : " + networkBytesIn);

                String networkBytesOutStr = networkBytes.get("OutBytes");
                networkBytesOut = Long.parseLong(networkBytesOutStr);
                System.out.println("Network Bytes Out : " + networkBytesOut);
                // System.out.println("macWinNetworkValue : "+windowsNetworkBytes.get("macWinNetworkValue"));
                assetId = "Computer" + "-" + sysDescr.hashCode() + "-" + networkBytes.get("macWinNetworkValue");
                System.out.println("Asset Id : " + assetId);

                // the following oid's is used to get network in and out bytes for Linux

            }
            else if (isLinux) {
                oidString = ".1.3.6.1.2.1.2.2.1";
                rootOID = new OID(oidString);
                result = MeterUtils.walk(rootOID, target);

                String[] ethernet = new String[] { "eth0", "eth1", "eth2", "en1", "en2", "en3", "em1", "em2", "em3",
                        "wlan" };
                HashMap<String, List<Long>> networkMap = new HashMap<String, List<Long>>();

                Long inBytes = 0L;
                Long outBytes = 0L;
                networkBytes = networkBytesCalc(result, rootOID, ethernet, networkMap, assetId, sysDescr);
                for (int i = 0; i < ethernet.length; i++) {
                    if (networkBytes.get(ethernet[i] + "InBytes") != null) {
                        inBytes = inBytes + Long.parseLong(networkBytes.get(ethernet[i] + "InBytes"));
                    }
                    if (networkBytes.get(ethernet[i] + "OutBytes") != null) {
                        outBytes = outBytes + Long.parseLong(networkBytes.get(ethernet[i] + "OutBytes"));
                    }
                } // for loop ends
                assetId = "Computer" + "-" + networkBytes.get("assetId");
                System.out.println("Asset Id : " + assetId);
                System.out.println("Network Bytes In : " + inBytes);
                System.out.println("Network Bytes Out : " + outBytes);
            } // Linux if loop ends

            // get the list of installed software 
            List<InstalledSoftware> installedSwList = new LinkedList<InstalledSoftware> ();

            // add code here to walk and populate list - sriram
            
            Computer compObject = new Computer(assetId, cpuLoad, totalMemory, usedMemory, totalVirtualMemory,
                    usedVirtualMemory, totalDiskSpace, usedDiskSpace, uptime, numLoggedInUsers, numProcesses,
                    networkBytesIn, networkBytesOut, clockSpeed, sysName, sysIP, sysDescr, sysContact, sysLocation,
                    extras , installedSwList);

            return compObject;
        }
        else {
            String assetDescr = ""; // put the system ip and if u want system description
            List errorList = new LinkedList<String>(); // add the null values and empty details to this list and return
            GQErrorInformation gqerr = new GQErrorInformation(assetDescr, errorList);
        }
        return null;
    }

    public void makeSnmpListen(String ip, String community) throws IOException {
        /**
         * Port 161 is used for Read and Other operations Port 162 is used for the trap generation
         */
        protocolAddress = ip; // "UDP:"+IP+"//"+"161";
        communityString = community;
        /**
         * Start the SNMP session. If you forget the listen() method you will not get any answers because the
         * communication is asynchronous and the listen() method listens for answers.
         * 
         * @throws IOException
         */
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
    }

    private String getSNMPValue(String octetString, List<VariableBinding> result) {

        for (VariableBinding vb : result) {
            if (octetString.equals(vb.getOid().toString())) {
                return vb.getVariable().toString();
            }
        } // for loop ends
        return null;
    }

    public long upTimeCalc(String time) {
        String dayString = null;
        String[] upTimeArray = null;
        String timeString = null;
        long dayseconds = 0L;
        long hourSec = 0L;
        long minSec = 0L;
        long seconds = 0L;
        String secondsConc = null;

        if (time.contains(",")) {
            upTimeArray = time.split(",");
        }
        if (upTimeArray != null) {
            dayString = upTimeArray[0].trim();
            timeString = upTimeArray[1].trim();
        }
        else {
            timeString = time.trim();
        }

        if (dayString != null) {
            System.out.println("dayString :" + dayString);
            if (dayString.split(" ")[1].toString().trim().equals("day")) {
                long day = Long.parseLong(dayString.replace("day", "").trim());
                dayseconds = TimeUnit.SECONDS.convert(day, TimeUnit.DAYS);
            }
            else {
                long days = Long.parseLong(dayString.replace("days", "").trim());
                dayseconds = TimeUnit.SECONDS.convert(days, TimeUnit.DAYS);
            }
        }
        if (timeString != null) {
            String[] timeArray = timeString.split(":");

            long hour = Long.parseLong(timeArray[0].trim());
            hourSec = TimeUnit.SECONDS.convert(hour, TimeUnit.HOURS);

            long minutes = Long.parseLong(timeArray[1].trim());
            minSec = TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

            seconds = Long.parseLong(timeArray[2].split("\\.")[0].trim());
            secondsConc = timeArray[2].split("\\.")[1].trim();
        }
        long secs = dayseconds + hourSec + minSec + seconds;
        String secsString = String.valueOf(secs) + secondsConc;
        long sec = Long.parseLong(secsString);

        return sec;
    }

    public long cpuLoadCalc(List<VariableBinding> result, OID rootOid) {
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

    public long getMemorycalc(List<VariableBinding> result, OID rootOid, String variable, boolean isUsed) {

        String mulBytes = null;
        String mulBytesOID = null;
        String toMultiply = null;
        String toMultiplyOID = null;
        String usedMultiplyOID = null;
        String usedMultiply = null;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) {

            String temp = vb.getVariable().toString().trim();
            boolean isValid = false;
            if (temp.contains("/") && temp.trim().equals(variable)) {
                // If the variable has / then skip the below condition to check
                // with lower case, because it will not convert / to lower case
                isValid = true;
            }
            else if (temp.contains(":\\") && temp.trim().contains(variable)) {
                isValid = true;
            }
            else if (!isValid && temp.trim().toLowerCase().equals(variable)) {
                // If the variable doesn't contains / then make it to lower case
                // and compare now no exception will be thrown
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
            } // if loop ends

            else if (mulBytesOID != null && vb.toString().contains(mulBytesOID)) {
                mulBytes = vb.getVariable().toString().trim();
            }
            else if (!isUsed && toMultiplyOID != null && vb.toString().contains(toMultiplyOID)) {
                toMultiply = vb.getVariable().toString().trim();
            }
            else if (isUsed && usedMultiplyOID != null && vb.toString().contains(usedMultiplyOID)) {
                usedMultiply = vb.getVariable().toString().trim();
            }
        } // for loop ends

        Long memory = 0L;
        if (!isUsed && mulBytes != null && toMultiply != null) {
            memory = Long.parseLong(mulBytes.trim()) * Long.parseLong(toMultiply.trim());
        }
        else if (isUsed && mulBytes != null && usedMultiply != null) {
            memory = Long.parseLong(mulBytes.trim()) * Long.parseLong(usedMultiply.trim());
        }
        return memory;
    }

    public HashMap<String, String> networkBytesCalc(List<VariableBinding> result, OID rootOid, String[] ethernet,
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
                if (vb.getVariable().toString().trim().equals(ethernet[i])) {
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
                                networkList.add(Long.parseLong(networkInStr));
                                networkMap.put(ethernet[i] + "InBytes", networkList);
                            }
                            else {
                                networkMap.get(ethernet[i] + "InBytes").add(Long.parseLong(networkInStr));
                            }
                        }
                        else if (networkOutOid != null && vbs.getOid().toString().trim().equals(networkOutOid)) {
                            networkOutStr = vbs.getVariable().toString();
                            if (networkMap.get(ethernet[i] + "OutBytes") == null
                                    || networkMap.get(ethernet[i] + "OutBytes").isEmpty()
                                    || networkMap.get(ethernet[i] + "OutBytes").size() == 0) {
                                List<Long> networkList = new ArrayList<Long>();
                                networkList.add(Long.parseLong(networkOutStr));
                                networkMap.put(ethernet[i] + "OutBytes", networkList);
                            }
                            else {
                                networkMap.get(ethernet[i] + "OutBytes").add(Long.parseLong(networkOutStr));
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

                    // Check MAC address and return the max value of {"eth0",
                    // "eth1", "eth2", "en1" , "en2","en3", "em1", "em2" , "em3", "wlan"}
                    // in the return values
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
            String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1);
            assetId = String.valueOf(sysDescr.hashCode()) + "-" + eth0MacAddress;
            networkValues.put("assetId", assetId);
        }
        return networkValues;
    } // network bytes calculation for Linux gets over

    public HashMap<String, String> winNetworkBytesCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, String> winNetworkMap) {

        String networkInOid = null;
        String networkOutOid = null;
        String macWinNetworkId = null;

        String rootId = rootOid.toString();

        for (VariableBinding vb : result) {
            // System.out.println("vb :" +vb);
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
                            String macWinNetworkValue = vbs.getVariable().toString().trim();
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
}
