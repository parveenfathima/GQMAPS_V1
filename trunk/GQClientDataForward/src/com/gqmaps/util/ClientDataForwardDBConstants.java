package com.gqmaps.util;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class ClientDataForwardDBConstants {

    // logger specifics
    public static final Logger logger = Logger.getLogger("ClientDataForwardDBConstants.class");

    private static FileAppender fileappender;
    private static String logFilePrefix = "C:\\Users\\yogalakshmi.s\\opt\\gq\\maps\\logs";
    private static String logFileName = "clientdataforward.log";

    private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";

    static {
        logger.info("Inside create logger method");

        ClientDataForwardDBConstants.logger.info("initializing ClientDataForward logger..."); // ???? !!!!!!
        String logFile = logFilePrefix + "\\" + logFileName;
        try {
            fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout(
                    "  %-15C{1} | %d{MMM dd HH:mm} | %5p | %m%n"), logFile, LOG_FILE_NAME_DATE_PATTERN);

        }
        catch (IOException e) {
            e.printStackTrace();
            ClientDataForwardDBConstants.logger.error("************ Unable to create LOG file handle for file  "
                    + logFile + " at " + new Date() + ". TERMINATING server ??.... ************"); // ???? !!!!!!
        }

        logger.addAppender(fileappender);
        logger.info("after create logger method");
    }

}
