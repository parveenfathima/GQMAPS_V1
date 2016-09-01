package com.gq.util;

import java.io.IOException;

import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class GQMapsCustomerUIConstants {
    // logger specifics
    public static final Logger logger = Logger.getLogger("GQMapsCustomerConstants.class");

    private static FileAppender fileappender;
    private static String logFilePrefix = "/opt/gq/maps/logs/";
    private static String logFileName = "customerui.log";
    public static final String webSvcHost = "gqmaps.com";
  //  public static final String webSvcHost = "192.168.8.119";
    private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";
    

    static {
        logger.info("Inside create logger method");
        GQMapsCustomerUIConstants.logger.info("initializing CustomerUI......."); 
        String logFile = logFilePrefix + logFileName;
        
        try {
            fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout(
                    "  %-15C{1} | %d{MMM dd HH:mm} | %5p | %m%n"), logFile, LOG_FILE_NAME_DATE_PATTERN);
        }
        catch (IOException e) {
            e.printStackTrace();
            GQMapsCustomerUIConstants.logger.error("************ Unable to create LOG file handle for file  " + logFile + " at "
                    + new Date() + ". TERMINATING server ??.... ************"); 
        }
        logger.addAppender(fileappender);
        logger.info("after creating CustomerUI logger method");
    }
}

