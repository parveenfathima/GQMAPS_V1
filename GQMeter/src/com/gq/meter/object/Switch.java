package com.gq.meter.object;

public class Switch {
	
	String assetId;  // uniq identifier abt the asset
	
	int uptime;		// seconds
	int numberOfPorts;
	int numberOfPortsUp;	
	int networkBytesIn;	// bytes , v2
	int networkBytesOut; // bytes ,  v2
	int costToRoot;	// v2
		
	String sysName;	
	String sysIP;	// string
	String sysDescr; 
	String sysContact;
	String sysLocation; // string
	
	String extras;	// anything device specific but to be discussed , v2
	
}
