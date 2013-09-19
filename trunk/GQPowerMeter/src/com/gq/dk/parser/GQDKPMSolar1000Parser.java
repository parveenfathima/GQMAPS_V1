package com.gq.dk.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gq.dk.exception.GQDKPMException;
import com.gq.dk.model.Solar1000;
import com.gq.dk.util.GQDKPMConstants;

import com.gq.dk.util.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public final class GQDKPMSolar1000Parser extends GQDKPMGSVParser{
	
	private Solar1000 s1000Record = null;  // for convenience - todo shouldnt be here
	
	public Solar1000 getS1000Record() {
		return s1000Record;
	}

	public void setS1000Record(Solar1000 s1000Record) {
		this.s1000Record = s1000Record;
	}

	public GQDKPMSolar1000Parser(String deviceData) {
		setDeviceData(deviceData);
	}

	@Override
	List<String> preParseAndValidate(String deviceRecord)
			throws GQDKPMException {
			
		  // check 1- number of records vs records in snxxl string 
		  int numberOfRecords = Integer.parseInt(( deviceRecord.substring(deviceRecord.length()-3, deviceRecord.length()-1)), 16);
		  GQDKPMConstants.logger.debug("num recs - <"+ numberOfRecords+">");	
		  
		  String [] recordsArray = deviceRecord.substring(0,deviceRecord.length()-5).split("L");
		  
		  if ( recordsArray.length !=  numberOfRecords ) {
			  throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_NUM_RECS_MISMATCH);
		  }
		  
		  // check 2 - individual record length

		  for ( int i = 0 ; i < recordsArray.length ; i++ ) {
		   
			  // since split cut the last L from the records , we add it back
			  recordsArray[i] = recordsArray[i]+"L";
		   
			  if ( recordsArray[i].length() != GQDKPMConstants.GQ_DK_PM_SOLAR_1000_RECORD_LENGTH ) {
				  throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_REC_LENGTH_MISMATCH);
			  }
		  }
		  
		  // check 3- checksum of individual records

		  for ( int i = 0 ; i < recordsArray.length ; i++ ) {   
			  String crcInput = recordsArray[i].substring(recordsArray[i].length()-5, recordsArray[i].length()-1);
			  String crcCheckStr = recordsArray[i].substring(1, recordsArray[i].length()-5);

			  if ( getDecFromHex(crcInput) != calcCRC(crcCheckStr) ) {
				  throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_CHECKSUM_ERROR);
			  }
		  }
		
		  return new ArrayList<String>(Arrays.asList(recordsArray));
	}

	@Override
	void parse(String deviceRecord) throws GQDKPMException {

		String ctrlString = deviceRecord.substring(0, 1);
		
		String unitId = deviceRecord.substring(1, 5);
		String dateTime = deviceRecord.substring(5, 13);	
		String panelVoltage1 = deviceRecord.substring(13,17);	
		String panelVoltage2 = deviceRecord.substring(17,21);	
		String batteryVoltage = deviceRecord.substring(21,25);	
		String dgVoltage = deviceRecord.substring(25,29);	
		String ebRVoltage = deviceRecord.substring(29,33);	
		String ebYVoltage = deviceRecord.substring(33,37);	
		String ebBVoltage = deviceRecord.substring(37,41);	
		String solarChargeCurrent1 = deviceRecord.substring(41,45);	
		String solarChargeCurrent2 = deviceRecord.substring(45,49);	
		String btsChargeCurrent = deviceRecord.substring(49,53);	
		String dischargeCurrent = deviceRecord.substring(53,57);	
		String batteryAhPercent = deviceRecord.substring(57,61);	
		String sunlight = deviceRecord.substring(61,65);	
		String batteryTemperature = deviceRecord.substring(65,69);	
		String batteryAh = deviceRecord.substring(69,73);	
		String fuel = deviceRecord.substring(73,77);
		
		int enterpriseId = GQDKPMConstants.getEnterprise().getEnterpriseId();
		
		String crcInput = deviceRecord.substring(deviceRecord.length()-5, deviceRecord.length()-1);

		String endChar = deviceRecord.substring(deviceRecord.length()-1, deviceRecord.length());

		String crcCheckStr = deviceRecord.substring(1, deviceRecord.length()-5);

		GQDKPMConstants.logger.debug("incoming record  = <" + deviceRecord + ">" );

		GQDKPMConstants.logger.debug("control string  = " + ctrlString + " , endchar = " + endChar);
		      
		GQDKPMConstants.logger.debug("Parsed string from device => [unitId=" + unitId + ", recordingDttm="
			+ dateTime + ", panelVoltage1=" + panelVoltage1
			+ ", panelVoltage2=" + panelVoltage2 + ", batteryVoltage="
			+ batteryVoltage + ", dgVoltage=" + dgVoltage + ", ebRVoltage="
			+ ebRVoltage + ", ebYVoltage=" + ebYVoltage + ", ebBVoltage="
			+ ebBVoltage + ", solarChargeCurrent1=" + solarChargeCurrent1
			+ ", solarChargeCurrent2=" + solarChargeCurrent2
			+ ", btsChargeCurrent=" + btsChargeCurrent
			+ ", dischargeCurrent=" + dischargeCurrent
			+ ", batteryAhPercent=" + batteryAhPercent + ", sunlight="
			+ sunlight + ", batteryTemperature=" + batteryTemperature
			+ ", batteryAh=" + batteryAh + ", fuel=" + fuel + ", enterpriseId="+enterpriseId +"]");

		GQDKPMConstants.logger.debug("crc 4 byte string   = " + crcInput + " , its checksum = " + getDecFromHex(crcInput));

		GQDKPMConstants.logger.debug("crc data string  = " + crcCheckStr + " , its checksum = " + calcCRC(crcCheckStr) );
		  		  
		  // date 80CBA659 ==>19-06-2012 12:46:00
		Solar1000 gqDKdevAttr = new Solar1000( 
				getUnitId( unitId )  , 
				calcDate( dateTime ) , 
				(double)getDecFromHex( panelVoltage1 ) / 10 ,  
				(double)getDecFromHex( panelVoltage2 ) / 10 , 
				(double)getDecFromHex( batteryVoltage) / 10,
				(double)getDecFromHex( dgVoltage ) / 10,
				(double)getDecFromHex( ebRVoltage ) / 10,
				(double)getDecFromHex( ebYVoltage ) / 10,
				(double)getDecFromHex( ebBVoltage ) / 10,
				(double)getDecFromHex( solarChargeCurrent1 ) / 10,
				(double)getDecFromHex( solarChargeCurrent2 ) / 10,
				(double)getDecFromHex( btsChargeCurrent ) / 10,
				(double)getDecFromHex( dischargeCurrent ) / 10,
				(double)getDecFromHex( batteryAhPercent ) / 10,
				(double)getDecFromHex( sunlight ) / 10,
				(double)getDecFromHex( batteryTemperature ) / 10,
				(double)getDecFromHex( batteryAh ) / 10,
				(double)getDecFromHex( fuel ) / 10,
				enterpriseId);		

		// set current for retrieving in case of a problem
		setS1000Record(gqDKdevAttr);
		
		GQDKPMConstants.logger.debug("parsed device attributes  = " + gqDKdevAttr );
		Session session = null;
		Transaction tx = null;
		//Inserting device record into gqdk_solar100 table
		try{
			SessionFactory sf = HibernateUtil.getSessionFactory();
			session = sf.openSession();
			tx = session.beginTransaction();
			session.save(gqDKdevAttr);
			tx.commit();
			session.close();
		}
		catch (Exception e){
			e.printStackTrace();
			GQDKPMConstants.logger.error("db error while saving solar 1000 record  = " + e.toString() );
			tx.rollback();
			session.close();
			//GQDKPMConstants.logger.error(e.getMessage());
			throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_DB_RELATED);			
		}
		
	} // parse method ends
	
} // class ends

