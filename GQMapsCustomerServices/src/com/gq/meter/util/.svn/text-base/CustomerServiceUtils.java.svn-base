package com.gq.meter.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomerServiceUtils {

    // logger specifics
    public static final Logger logger = Logger.getLogger("CustomerServiceConstant.class");

    private static FileAppender fileappender;
    private static String logFilePrefix = "/opt/gq/maps/logs/";
    private static String logFileName = "customerservices.log";

    private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";

    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    static {
        logger.info("Inside create logger method");

        CustomerServiceUtils.logger.info("initializing gqmaps CustomerServices logger..."); // ???? !!!!!!
        String logFile = logFilePrefix + logFileName;
        try {
            fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout(
                    "%d{MMM dd HH:mm} | %-15F(%L) | %5p | %m%n"), logFile, LOG_FILE_NAME_DATE_PATTERN);

        }
        catch (IOException e) {
            e.printStackTrace();
            CustomerServiceUtils.logger.error("************ Unable to create LOG file handle for file  " + logFile
                    + " at " + new Date() + ". TERMINATING server ??.... ************"); // ???? !!!!!!
        }

        logger.addAppender(fileappender);
        logger.info("after create logger method");
    
    } // end of static block
    
	// this method returns
	// 1.ip address , if cant resolve it to hostname
	// 2.trimmed to last domain name in case of valid host resolution
	// 3.localhost for 127.0.0.1 related strings
	// 4.null for null
	public static String getTrimmedHostName(String ipAddress) {
		System.out.println("incoming ip address  = "+ ipAddress );
		if ( ( ipAddress == null ) || ( ipAddress.trim().equals("") ) ) {
			return null;
		}
		
		ipAddress = ipAddress.trim();
		
		if ( ipAddress.contains("127.0.0.1")) {
			return "localhost";
		}
		else { // probably a good ip
	        InetAddress addr;
			try {
				addr = InetAddress.getByName(ipAddress);
			} catch (UnknownHostException e) {
				return "unknown-host";
			}
			
	        String cnName = addr.getCanonicalHostName();

	        if ( cnName.equals(ipAddress)) {
	        	return ipAddress;
	        }
	        else {
		   	    String[] hostNameParts = cnName.split("\\.");
		   	       
		   	    //to trim the unwanted characters and to display a valid domain names like google.co.in..
		   	    if(hostNameParts.length >= 3) {
		   	    	return hostNameParts[hostNameParts.length-3]+"."+hostNameParts[hostNameParts.length-2]+"."+hostNameParts[hostNameParts.length-1];
		   	    } 
		   	    else {// to display a completely qualified domain names
		   	    	return hostNameParts[hostNameParts.length-2]+"."+hostNameParts[hostNameParts.length-1];
		   	    }
	        }
		}
	} // getTrimmedHostName ends
	
    
    
}
