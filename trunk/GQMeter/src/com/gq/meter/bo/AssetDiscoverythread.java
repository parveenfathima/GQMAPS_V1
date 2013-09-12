package com.gq.meter.bo;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.ComputerMeter;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterData;
import com.gq.meter.NSRGMeter;
import com.gq.meter.PrinterMeter;
import com.gq.meter.StorageMeter;

import com.gq.meter.assist.ProtocolData;
import com.gq.meter.util.MeterProtocols;

/**
 * @author sriram
 * @date sep 11 , 2013
 * 
 */
public final class AssetDiscoverythread implements Runnable{

    private Gson gson = new GsonBuilder().create();

	@Override
	public void run() {
		
        System.out.println(" [GQMETER] New asset snmp thread started at "+ System.currentTimeMillis());

        List<String> errorList = new LinkedList<String>();
        
        GQMeterData assetObject = getAssetObject(assetProtocol, communityString, ipAddress, snmpVersion,  switches) ;

        if (assetObject == null) {
            errorList.add(ipAddress + " - " + assetProtocol.name()   + " : Unable to fetch the meter details");
            gqErrorInfoList.add(new GQErrorInformation("should be assetDesc but ss testing", errorList));
        }
        else {
            pdList.add(new ProtocolData(assetProtocol, gson.toJson(assetObject.getMeterData())));
            if (assetObject.getErrorInformation() != null) {
                gqErrorInfoList.add(assetObject.getErrorInformation());
            }
        }

		System.out.println(" [GQMETER] New asset snmp thread ended at "+ System.currentTimeMillis());
		
	}

	String snmpVersion ; 
	LinkedList<String> switches ;
	String communityString ;
	String ipAddress ; 
	MeterProtocols assetProtocol;
	// out object
	List<ProtocolData> pdList;
	List<GQErrorInformation> gqErrorInfoList;

	public AssetDiscoverythread(String snmpVersion,
			LinkedList<String> switches, String communityString,
			String ipAddress, MeterProtocols assetProtocol , List<ProtocolData> pdList , List<GQErrorInformation> gqErrorInfoList) {

		this.snmpVersion = snmpVersion;
		this.switches = switches;
		this.communityString = communityString;
		this.ipAddress = ipAddress;
		this.assetProtocol = assetProtocol;
		
		// pd list is for output , we dont set anything to it here
		this.pdList = pdList;
		this.gqErrorInfoList =  gqErrorInfoList;
	}

	private  GQMeterData getAssetObject(MeterProtocols protocol, String communityString, String currIp,
            String snmpVersion, LinkedList<String> switchList) {

        GQMeterData assetObject = null;
        
        if (protocol.equals(MeterProtocols.PRINTER) ) {
            assetObject = new PrinterMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        else if (protocol.equals(MeterProtocols.NSRG) ) {
            assetObject = new NSRGMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        else if (protocol.equals(MeterProtocols.COMPUTER) ) {
            assetObject = new ComputerMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        else if (protocol.equals(MeterProtocols.STORAGE) ) {
            assetObject = new StorageMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        return assetObject;
    }

 
}//class ends
