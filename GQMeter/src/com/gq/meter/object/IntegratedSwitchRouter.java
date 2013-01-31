package com.gq.meter.object;

import java.util.HashSet;

import java.util.List;

public class IntegratedSwitchRouter {

    String assetId; // uniq identifier abt the asset

    long upTime; // seconds
    long numberOfPorts;
    long numberOfPortsUp;
    long networkBytesIn; // bytes
    long networkBytesOut; // bytes

    String sysName;
    String sysIP; // string
    String sysDescr;
    String sysContact;
    String sysLocation; // string

    HashSet<String> connectedDevices;

    String extras; // anything device specific but to be discussed , v2

    public String getAssetId() {
        return assetId;
    }

    public long getUpTime() {
        return upTime;
    }

    public long getNumberOfPorts() {
        return numberOfPorts;
    }

    public long getNumberOfPortsUp() {
        return numberOfPortsUp;
    }

    public long getNetworkBytesIn() {
        return networkBytesIn;
    }

    public long getNetworkBytesOut() {
        return networkBytesOut;
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

    public HashSet<String> getConnectedDevices() {
        return connectedDevices;
    }

    public String getExtras() {
        return extras;
    }

    public IntegratedSwitchRouter(String assetId, long upTime, long numberOfPorts, long numberOfPortsUp,
            long networkBytesIn, long networkBytesOut, String sysName, String sysIP, String sysDescr,
            String sysContact, String sysLocation, HashSet<String> connectedDevices, String extras) {
        super();
        this.assetId = assetId;
        this.upTime = upTime;
        this.numberOfPorts = numberOfPorts;
        this.numberOfPortsUp = numberOfPortsUp;
        this.networkBytesIn = networkBytesIn;
        this.networkBytesOut = networkBytesOut;
        this.sysName = sysName;
        this.sysIP = sysIP;
        this.sysDescr = sysDescr;
        this.sysContact = sysContact;
        this.sysLocation = sysLocation;
        this.connectedDevices = connectedDevices;
        this.extras = extras;
    }

}
