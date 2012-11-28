package com.gq.meter;

import java.util.List;

public class GQMeterData {

    List<GQErrorInformation> errorInformationList ;
    
    // will be one of the meter objects like computer , printer , air
    Object meterData;

	public GQMeterData(List<GQErrorInformation> errorInformationList,
			Object meterData) {
		super();
		this.errorInformationList = errorInformationList;
		this.meterData = meterData;
	}

	public List<GQErrorInformation> getErrorInformationList() {
		return errorInformationList;
	}

	public Object getMeterData() {
		return meterData;
	}
    
    
}
