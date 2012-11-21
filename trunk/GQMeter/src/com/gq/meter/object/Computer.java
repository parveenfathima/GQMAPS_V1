package com.gq.meter.object;

import java.util.Date;

public class Computer {
	
	String assetId;  // uniq identifier abt the asset
	
	int cpuLoad ;  // in percentage
	int totalMemory; 	// bytes
	int usedMemory;		// bytes
	int totalVirtualMemory;		// bytes
	int usedVirtualMemory;		// bytes
	int totalDiskSpace;		// bytes
	int usedDiskSpace;		// bytes
	int uptime;		// seconds
	int numLoggedInUsers;
	int numProcesses;
	int networkBytesIn;	// bytes
	int networkBytesOut; // bytes
	
	double clockSpeed; 	//v2
	
	String sysName;
	String sysIP;	// string
	String sysDescr; 
	String sysContact;
	String sysLocation; // string
	String extras;	// anything device specific
	
	java.util.Date sysDate;

	public Computer(String assetId, int cpuLoad, int totalMemory, int usedMemory,
			int totalVirtualMemory, int usedVirtualMemory, int totalDiskSpace,
			int usedDiskSpace, int uptime, int numLoggedInUsers,
			int numProcesses, int networkBytesIn, int networkBytesOut,
			double clockSpeed, String sysName, String sysIP, String sysDescr,
			String sysContact, String sysLocation, String extras, Date sysDate) {
		super();
		this.assetId = assetId;
		this.cpuLoad = cpuLoad;
		this.totalMemory = totalMemory;
		this.usedMemory = usedMemory;
		this.totalVirtualMemory = totalVirtualMemory;
		this.usedVirtualMemory = usedVirtualMemory;
		this.totalDiskSpace = totalDiskSpace;
		this.usedDiskSpace = usedDiskSpace;
		this.uptime = uptime;
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
		this.sysDate = sysDate;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public double getClockSpeed() {
		return clockSpeed;
	}

	public void setClockSpeed(double clockSpeed) {
		this.clockSpeed = clockSpeed;
	}
	
	public int getCpuLoad() {
		return cpuLoad;
	}

	public int getTotalMemory() {
		return totalMemory;
	}

	public int getUsedMemory() {
		return usedMemory;
	}

	public int getTotalVirtualMemory() {
		return totalVirtualMemory;
	}

	public int getUsedVirtualMemory() {
		return usedVirtualMemory;
	}

	public int getTotalDiskSpace() {
		return totalDiskSpace;
	}

	public int getUsedDiskSpace() {
		return usedDiskSpace;
	}

	public int getUptime() {
		return uptime;
	}

	public int getNumLoggedInUsers() {
		return numLoggedInUsers;
	}

	public int getNumProcesses() {
		return numProcesses;
	}

	public int getNetworkBytesIn() {
		return networkBytesIn;
	}

	public int getNetworkBytesOut() {
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

	public String getExtras() {
		return extras;
	}

	public java.util.Date getSysDate() {
		return sysDate;
	} 
	
	
}
