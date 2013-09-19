
package com.gq.dk.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.gq.dk.model.Enterprise;
import com.gq.dk.model.GQDKPMDBManager;
import com.gq.dk.model.SystemProfile;

public class GQDKPMConstants {

	// logger specifics
	public static final Logger logger = Logger.getLogger("GQDKPMConstants.class");
	private static FileAppender fileappender ;
	private static String logFilePrefix = "/opt/gq-dk-pm/logs/";
	private static String logFileName = "application.log";
	
	// socket specifics
	//public static final int GQ_DK_PM_SERVER_LISTEN_PORT = 1100; // todo , should be a user input based on profile.
	public static final int GQ_DK_PM_INCOMPLETE_PACKET_TIMEOUT_SECONDS = 60;
	public static final int GQ_DK_PM_NUM_RETRIES = 3;

	// we may not use these specifics for now , we will go with the above common number
	public static final int GQ_DK_PM_CHECKSUM_ERROR_RETRIES = 3;
	public static final int GQ_DK_PM_NUM_RECS_MISMATCH_RETRIES = 3;
	
	// data record specifics
	public static final int GQ_DK_PM_SOLAR_1000_RECORD_LENGTH = 82;

	// exception codes 
	public static final String GQ_DK_PM_EXCEPTION_CHECKSUM_ERROR = "GQ_DK_PM_EXCEPTION_CHECKSUM_ERROR";
	public static final String GQ_DK_PM_EXCEPTION_REC_LENGTH_MISMATCH = "GQ_DK_PM_EXCEPTION_REC_LENGTH_MISMATCH";
	public static final String GQ_DK_PM_EXCEPTION_NUM_RECS_MISMATCH = "GQ_DK_PM_EXCEPTION_NUM_RECS_MISMATCH";
	public static final String GQ_DK_PM_EXCEPTION_DB_RELATED = "GQ_DK_PM_EXCEPTION_DB_RELATED";

	// factory related variables
	public static final int GQ_DK_PM_PROTOCOL_SOLAR_1000 = 1;
	public static final int GQ_DK_PM_PROTOCOL_SOLAR_1000_NEW = 2;

	private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";
	
	// these 2 guys shouldnt belong here in a strict sense. but will have em for a while - ss aug 10,2012 , -todo-
	private static Enterprise enterprise;
	
	private static Map<String , String> profileMap = new HashMap<String , String> ();

	public static String getProfileEntry(String key) {
		return profileMap.get(key);
	}

	public static void addToProfileMap(String key , String value) {
		GQDKPMConstants.profileMap.put(key, value);
	}


	public static void createLogger( int enterpriseId ) {

		GQDKPMConstants.logger.info("initializing gq dk performance monitor application logger...");   //???? !!!!!!
		String logFile = logFilePrefix + "//" + enterpriseId + "//" + logFileName;
		try {
			fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout("%d | %5p | %m%n") , 
					 logFile , LOG_FILE_NAME_DATE_PATTERN);
			
		} catch (IOException e) {
			e.printStackTrace();
			GQDKPMConstants.logger.error("************ Unable to create LOG file handle for file  "+ logFile +" at " 
					+ new Date() + ". TERMINATING server ??.... ************" );  //???? !!!!!!
		}

		logger.addAppender(fileappender);
	}


	public static void setEnterprise(Enterprise enterprise) {
		GQDKPMConstants.enterprise = enterprise;
	}


	public static Enterprise getEnterprise() {
		
		return enterprise;
	}
	
//	private static int getEnterpriseId() {
//		
//		if ( enterprise == null ) {
//			System.out.println("Could not obtain enterprise id for logging purposes , exitting listener...");
//
//			System.exit(1);
//		}
//		
//		return enterprise.getEnterpriseId();
//	}
	
} // class ends

