package com.gq.meter.object;

import java.util.HashSet;

import java.util.List;

public class Printer {

    String assetId; // uniq identifier abt the asset

    long upTime; // seconds
    double tonerPercentage;
    long outOfPaperIndicator; // 0 means no paper , v2
    long printsTakenCount; // v2
    long totalMemory; // bytes
    long usedMemory; // bytes
    long totalDiskSpace; // bytes
    long usedDiskSpace; // bytes

    String sysName;
    String sysIP; // string
    String sysDescr;
    String sysContact;
    String sysLocation; // string
    String errorCondition;
    String operationalState; // for this and the next value , we maintain a table ; need to work more on it.
    String currentState;
    String mfgModel;// v2
    String isColorPrinter;

    String extras; // anything device specific but to be discussed , v2

    HashSet<String> connectedDevices;

    public String getAssetId() {
        return assetId;
    }

    public long getUpTime() {
        return upTime;
    }

    public double getTonerPercentage() {
        return tonerPercentage;
    }

    public long getOutOfPaperIndicator() {
        return outOfPaperIndicator;
    }

    public long getPrintsTakenCount() {
        return printsTakenCount;
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

    public String getErrorCondition() {
        return errorCondition;
    }

    public String getOperationalState() {
        return operationalState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getMfgModel() {
        return mfgModel;
    }

    public String getIsColorPrinter() {
        return isColorPrinter;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public long getUsedDiskSpace() {
        return usedDiskSpace;
    }

    public HashSet<String> getConnectedDevices() {
        return connectedDevices;
    }

    public String getExtras() {
        return extras;
    }

    public Printer(String assetId, long upTime, double tonerPercentage, long outOfPaperIndicator,
            long printsTakenCount, String sysName, String sysIP, String sysDescr, String sysContact,
            String sysLocation, String errorCondition, String operationalState, String currentState, String mfgModel,
            String isColorPrinter, long totalMemory, long totalDiskSpace, long usedMemory, long usedDiskSpace,
            HashSet<String> connectedDevices, String extras) {
        super();
        this.assetId = assetId;
        this.upTime = upTime;
        this.tonerPercentage = tonerPercentage;
        this.outOfPaperIndicator = outOfPaperIndicator;
        this.printsTakenCount = printsTakenCount;
        this.sysName = sysName;
        this.sysIP = sysIP;
        this.sysDescr = sysDescr;
        this.sysContact = sysContact;
        this.sysLocation = sysLocation;
        this.errorCondition = errorCondition;
        this.operationalState = operationalState;
        this.currentState = currentState;
        this.mfgModel = mfgModel;
        this.isColorPrinter = isColorPrinter;
        this.totalMemory = totalMemory;
        this.totalDiskSpace = totalDiskSpace;
        this.usedMemory = usedMemory;
        this.usedDiskSpace = usedDiskSpace;
        this.connectedDevices = connectedDevices;
        this.extras = extras;
    }

}
