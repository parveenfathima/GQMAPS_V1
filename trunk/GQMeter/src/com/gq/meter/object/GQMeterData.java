package com.gq.meter.object;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GQMeterData {

	//meter id
	String gqmid;
	
	// time when data is recorded at client site
	Date recDttm;
	
	// version of the meter - not used much in v1
	String version;
	
	// can only be 'pass' or 'fail'
	String status;
	
	// all informational details , errors or warnings as reported by the meter
	String comment;


	// class holds each asset discovery data along with the asset type
	class ProtocolData {
		
		// name of the protocol implemented.. can only be from a list of implemented protocols such as 
		// air , water , router , computer , power , switch , printer etc
		String protocol;
		
		// contains the actual protocol data , like power , computer , water etc
		String data;

	}

	// this list contains all the auto discovered assets and their details
	List<ProtocolData> assetInformationList = new LinkedList<ProtocolData> ();
	
	// this list contains error or other notifications as reported by the individual asset data gathering modules.
	// there is no one to one correspondence between this list and the asset info list
	List<GQErrorInformation> errorInformationList = new LinkedList<GQErrorInformation> ();
	
	
}
