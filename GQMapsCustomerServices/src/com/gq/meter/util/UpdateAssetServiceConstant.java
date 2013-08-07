package com.gq.meter.util;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UpdateAssetServiceConstant {
    public static final Logger logger = Logger.getLogger("UpdateAssetServiceConstant.class");

    private static FileAppender fileappender;
    private static String logFilePrefix = "/opt/gq/maps/logs/";
    private static String logFileName = "updateassetservices.log";

    private static final String LOG_FILE_NAME_DATE_PATTERN = "yyyy-MM-dd";

    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    static {
        logger.info("Inside create logger method");

        UpdateAssetServiceConstant.logger.info("initializing gqmaps UpdateAssetServices logger..."); // ???? !!!!!!
        String logFile = logFilePrefix + logFileName;
        try {
            fileappender = new org.apache.log4j.DailyRollingFileAppender(new PatternLayout(
                    "  %-15C{1} | %d{MMM dd HH:mm} | %5p | %m%n"), logFile, LOG_FILE_NAME_DATE_PATTERN);

        }
        catch (IOException e) {
            e.printStackTrace();
            UpdateAssetServiceConstant.logger.error("************ Unable to create LOG file handle for file  "
                    + logFile + " at " + new Date() + ". TERMINATING server ??.... ************"); // ???? !!!!!!
        }

        logger.addAppender(fileappender);
        logger.info("after create logger method");
    }
}
