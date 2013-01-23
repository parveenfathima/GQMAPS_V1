package com.gq.meter.object.assist;

public class Process {
	
	String name;
	int cpuShareCentiSecs;
	int memShareKb;
	
	public String getName() {
		return name;
	}

	public int getCpuShareCentiSecs() {
		return cpuShareCentiSecs;
	}

	public int getMemShareKb() {
		return memShareKb;
	}

	public Process(String name, int cpuShareCentiSecs, int memShareKb) {
		super();
		this.name = name;
		this.cpuShareCentiSecs = cpuShareCentiSecs;
		this.memShareKb = memShareKb;
	}
	
	
	
}
