package com.gq.dk.parser;

import java.util.List;

import com.gq.dk.exception.GQDKPMException;

public abstract class GQDKPMParser {

	private String deviceData;
	protected List<String> records;	
	
	public String getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(String deviceData) {
		this.deviceData = deviceData;
	}

	// the following are validated here...
	// - number of records vs records in snxxl string 
	// - individual record length
	// - checksum of individual records
	// the list of valid records is returned after parsing..
	abstract List<String> preParseAndValidate(String deviceRecord) throws GQDKPMException;
	
	// actual parsing and storing 
	abstract void parse(String deviceRecord) throws GQDKPMException;
	
    /* A template method : */
    public final void process() throws GQDKPMException {
    	
    	records = preParseAndValidate(deviceData);
    	
    	// if above method succeeded , parse the record
    	for ( int i=0 ; i < records.size() ; i++ ) {
        	parse(records.get(i));
    	}
        	
    }

} // class ends
