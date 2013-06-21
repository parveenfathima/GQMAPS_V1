package com.gq.util;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author yogalakshmi.s
 * 
 */
public class GQEDPConstants {

    // logger specifics
    public static final Logger logger = Logger.getLogger("GQEDPConstants.class");

    private static FileAppender fileappender;
    private static String logFilePrefix = "/opt/gq/maps/logs/";
    private static String logFileName = "enterpriseDataProcessor.log";

    private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";

    static {
        logger.info("Inside create logger method");

        GQEDPConstants.logger.info("initializing gqmaps EntrprseDataProcessor logger..."); // ???? !!!!!!
        String logFile = logFilePrefix + logFileName;
        try {
            fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout(
                    "  %-15C{1} | %d{MMM dd HH:mm} | %5p | %m%n"), logFile, LOG_FILE_NAME_DATE_PATTERN);

        }
        catch (IOException e) {
            e.printStackTrace();
            GQEDPConstants.logger.error("************ Unable to create LOG file handle for file  " + logFile + " at "
                    + new Date() + ". TERMINATING server ??.... ************"); // ???? !!!!!!
        }

        logger.addAppender(fileappender);
        logger.info("after create logger method");
    }
}
