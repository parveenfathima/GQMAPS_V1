package com.gq.meter.object;

public class Router {

	String assetId; // uniq identifier abt the asset

	String sysName;
    String sysIP; // string
    String sysDescr;
    String sysContact;
    String sysLocation; // string
    String extras; // anything device specific

    long cpuLoad; // in percentage for the last 5 mins
    long numberOfPorts; 
    long multicastEnabled;	// 0 means no , 1 means yes
    long multicastRouteTblSize;
    
	public String getAssetId() {
		return assetId;
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

	public long getCpuLoad() {
		return cpuLoad;
	}

	public long getNumberOfPorts() {
		return numberOfPorts;
	}

	public long getMulticastEnabled() {
		return multicastEnabled;
	}

	public long getMulticastRouteTblSize() {
		return multicastRouteTblSize;
	}



}
