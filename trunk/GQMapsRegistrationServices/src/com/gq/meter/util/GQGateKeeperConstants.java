package com.gq.meter.util;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author yogalakshmi.s
 * 
 */
public class GQGateKeeperConstants {

    // logger specifics
    public static final Logger logger = Logger.getLogger("GQGateKeeperConstants.class");

    private static FileAppender fileappender;
    private static String logFilePrefix = "/opt/gq/maps/logs/";
    private static String logFileName = "mapsRegistrationServices.log";
    private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";

    public static final String GQEDP_URL = "http://127.0.0.1:8080/GQEntrprseDataProcessor/";
    public static final String PROTOCOL_IT = "it";

    static {
        logger.info("Inside create logger method");

        GQGateKeeperConstants.logger.info("initializing gqmaps EntrprseDataProcessor logger...");
        String logFile = logFilePrefix + logFileName;
        try {
            fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout(
                    "  %d{MMM dd HH:mm} | %-15F(%L) | %5p | %m%n"), logFile, LOG_FILE_NAME_DATE_PATTERN);

        }
        catch (IOException e) {
            e.printStackTrace();
            GQGateKeeperConstants.logger.error("************ Unable to create LOG file handle for file  " + logFile
                    + " at " + new Date() + ". TERMINATING server ??.... ************");
        }

        logger.addAppender(fileappender);
        logger.info("after create logger method");
    }
}
