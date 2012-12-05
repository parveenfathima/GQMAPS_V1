package com.gq.meter.object;

import java.util.List;

import com.gq.meter.object.assist.InstalledSoftware;

public class Computer {

    String assetId; // uniq identifier abt the asset

    long cpuLoad; // in percentage
    long totalMemory; // bytes
    long usedMemory; // bytes
    long totalVirtualMemory; // bytes
    long usedVirtualMemory; // bytes
    long totalDiskSpace; // bytes
    long usedDiskSpace; // bytes
    long upTime; // seconds
    long numLoggedInUsers;
    long numProcesses;
    long networkBytesIn; // bytes
    long networkBytesOut; // bytes

    List<InstalledSoftware> installedSwList;

    double clockSpeed; // v2

    String sysName;
    String sysIP; // string
    String sysDescr;
    String sysContact;
    String sysLocation; // string
    String extras; // anything device specific

    public String getAssetId() {
        return assetId;
    }

    public long getCpuLoad() {
        return cpuLoad;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public long getTotalVirtualMemory() {
        return totalVirtualMemory;
    }

    public long getUsedVirtualMemory() {
        return usedVirtualMemory;
    }

    public long getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public long getUsedDiskSpace() {
        return usedDiskSpace;
    }

    public long getUpTime() {
        return upTime;
    }

    public long getNumLoggedInUsers() {
        return numLoggedInUsers;
    }

    public long getNumProcesses() {
        return numProcesses;
    }

    public long getNetworkBytesIn() {
        return networkBytesIn;
    }

    public long getNetworkBytesOut() {
        return networkBytesOut;
    }

    public double getClockSpeed() {
        return clockSpeed;
    }

    public String getSysName() {
        return sysName;
    }

    public String getSysIP() {
        return sysIP;
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public String getSysContact() {
        return sysContact;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public String getExtras() {
        return extras;
    }

    public List<InstalledSoftware> getInstalledSwList() {
        return installedSwList;
    }

    public Computer(String assetId, long cpuLoad, long totalMemory, long usedMemory, long totalVirtualMemory,
            long usedVirtualMemory, long totalDiskSpace, long usedDiskSpace, long upTime, long numLoggedInUsers,
            long numProcesses, long networkBytesIn, long networkBytesOut, double clockSpeed, String sysName,
            String sysIP, String sysDescr, String sysContact, String sysLocation, String extras,
            List<InstalledSoftware> installedSwList) {
        super();
        this.assetId = assetId;
        this.cpuLoad = cpuLoad;
        this.totalMemory = totalMemory;
        this.usedMemory = usedMemory;
        this.totalVirtualMemory = totalVirtualMemory;
        this.usedVirtualMemory = usedVirtualMemory;
        this.totalDiskSpace = totalDiskSpace;
        this.usedDiskSpace = usedDiskSpace;
        this.upTime = upTime;
        this.numLoggedInUsers = numLoggedInUsers;
        this.numProcesses = numProcesses;
        this.networkBytesIn = networkBytesIn;
        this.networkBytesOut = networkBytesOut;
        this.clockSpeed = clockSpeed;
        this.sysName = sysName;
        this.sysIP = sysIP;
        this.sysDescr = sysDescr;
        this.sysContact = sysContact;
        this.sysLocation = sysLocation;
        this.extras = extras;
        this.installedSwList = installedSwList;
    }

}
