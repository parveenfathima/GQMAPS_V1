package com.gq.meter.object.assist;

import java.util.Date;

// this class provides a container for all installed software (is). one per is
public class InstalledSoftware {

	String name;
	Date installDate;
	
	public InstalledSoftware(String name, Date installDate) {
		super();
		this.name = name;
		this.installDate = installDate;
	}

	public String getName() {
		return name;
	}

	public Date getInstallDate() {
		return installDate;
	}
	
	
}
