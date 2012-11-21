package com.gq.meter.object;

public class Printer {
	
	String assetId;  // uniq identifier abt the asset
	
	int uptime;		// seconds
	int tonerPercentage;
	int outOfPaperIndicator;	// 0 means no paper , v2
	int networkBytesIn;	// bytes , v2
	int networkBytesOut; // bytes ,  v2
	int printsTakenCount;	// v2
		
	String sysName;	
	String sysIP;	// string
	String sysDescr; 
	String sysContact;
	String sysLocation; // string
	String errorCondition;	
	String operationalState;	// for this and the next value , we maintain a table ; need to work more on it.
	String currentState;
	String mfgModel;	//v2
	
	String extras;	// anything device specific but to be discussed , v2
	
}
