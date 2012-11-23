package com.gq.meter.object;

public class Printer {

    String assetId; // uniq identifier abt the asset

    long uptime; // seconds
    double tonerPercentage;
    long outOfPaperIndicator; // 0 means no paper , v2
    long networkBytesIn; // bytes , v2
    long networkBytesOut; // bytes , v2
    long printsTakenCount; // v2

    String sysName;
    String sysIP; // string
    String sysDescr;
    String sysContact;
    String sysLocation; // string
    String errorCondition;
    String operationalState; // for this and the next value , we maintain a table ; need to work more on it.
    String currentState;
    String mfgModel;// v2
    String tonerColor;

    String extras; // anything device specific but to be discussed , v2

    public String getAssetId() {
        return assetId;
    }

    public long getUptime() {
        return uptime;
    }

    public double getTonerPercentage() {
        return tonerPercentage;
    }

    public long getOutOfPaperIndicator() {
        return outOfPaperIndicator;
    }

    public long getNetworkBytesIn() {
        return networkBytesIn;
    }

    public long getNetworkBytesOut() {
        return networkBytesOut;
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

    public String getTonerColor() {
        return tonerColor;
    }

    public String getExtras() {
        return extras;
    }

    public Printer(String assetId, long uptime, double tonerPercentage, long outOfPaperIndicator, long networkBytesIn,
            long networkBytesOut, long printsTakenCount, String sysName, String sysIP, String sysDescr,
            String sysContact, String sysLocation, String errorCondition, String operationalState, String currentState,
            String mfgModel, String tonerColor, String extras) {
        super();
        this.assetId = assetId;
        this.uptime = uptime;
        this.tonerPercentage = tonerPercentage;
        this.outOfPaperIndicator = outOfPaperIndicator;
        this.networkBytesIn = networkBytesIn;
        this.networkBytesOut = networkBytesOut;
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
        this.tonerColor = tonerColor;
        this.extras = extras;
    }

}