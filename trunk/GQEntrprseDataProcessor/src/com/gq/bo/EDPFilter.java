package com.gq.bo;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;

import com.gq.meter.object.Computer;
import com.gq.meter.object.MeterRun;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.Printer;
import com.gq.meter.object.SpeedTestSnpsht;

import com.gq.util.DynamicSessionUtil;
import com.gq.util.GQEDPConstants;

/**
 * @author chandru
 * @change parveen,sriram
 */

// this class takes care of validating and distributing incoming requests
public final class EDPFilter {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void process(GQMeterResponse gqmResponse) {

        // extract response
        String meterId = gqmResponse.getGqmid();
        String enterpriseId = meterId.split("_")[0];
        Long runId = gqmResponse.getRunid();
        Date recordDT = gqmResponse.getRecDttm();
        short scanned = gqmResponse.getAssetScanned();
        short discovered = gqmResponse.getAssetDiscovered();
        long runTimeMs = gqmResponse.getRunTimeMiliSeconds();
        List<ProtocolData> pdList = gqmResponse.getAssetInformationList();

        Session session = null;
        MeterRun meterRun = null;
        SessionFactory sessionFactory = null;
        Transaction tx = null;

        GQEDPConstants.logger.debug("************* EDP STARTED - BEGINNING RUN " + runId + " ***********");

        meterId = meterId.split("_")[1];

        try {
            String dbInstanceName = "gqm" + enterpriseId;
            GQEDPConstants.logger.debug("Enterprise id : <" + enterpriseId + "> , Meter id : <" + meterId
                    + "> dbInstanceName : <" + dbInstanceName + ">");

            // This step will read hibernate.cfg.xml and prepare hibernate for use
            sessionFactory = DynamicSessionUtil.getSessionFactory(dbInstanceName);
            GQEDPConstants.logger.debug("Start to create a Session for requested Enterprise in EDPFilter");
            session = sessionFactory.getCurrentSession();
            GQEDPConstants.logger.debug("Session Created Successfully for EDPFilter");

            tx = session.beginTransaction();

            GQEDPConstants.logger.debug(meterId + " Transaction successfully started ");
            // TODO : create a sequence table which includes runid and assetid and generates a unique key to replace the
            // redundant use of runid and assetid on all the tables
            GQEDPConstants.logger.debug("Ready to store the Data in Meter Run Table");
            GQEDPConstants.logger.debug("Details are" + runId + "\n" + meterId + "\n" + recordDT + "\n" + scanned
                    + "\n" + discovered + "\n" + runTimeMs);

            meterRun = new MeterRun(runId, meterId, recordDT, scanned, discovered, runTimeMs);
            session.save(meterRun);

            GQEDPConstants.logger.info(meterId + " Data successfully saved in the meterRun table ");

            for (ProtocolData pdData : pdList) {

                switch (pdData.getProtocol()) {

                case COMPUTER:
                    Computer computerObj = gson.fromJson(pdData.getData(), Computer.class);
                    new GqMeterComputer().insertData(computerObj, gqmResponse, meterRun.getRunId(), session);
                    break;

                case PRINTER:
                    Printer printerData = gson.fromJson(pdData.getData(), Printer.class);
                    new GqMeterPrinter().insertData(printerData, gqmResponse, meterRun.getRunId(), session);
                    break;

                case NSRG:
                    NSRG nsrgData = gson.fromJson(pdData.getData(), NSRG.class);
                    new GqMeterNSRG().insertData(nsrgData, gqmResponse, meterRun.getRunId(), session);
                    break;

                case STORAGE:
                    // Storage storageData = gson.fromJson(pdData.getData(), Storage.class);
                    // GqMeterStorage.insertData(storageData, gqmResponse, meterRun.getRunId());
                    break;
                
                case SPEEDTEST:
                	SpeedTestSnpsht speedTestData = gson.fromJson(pdData.getData(), SpeedTestSnpsht.class);
                    new GqMeterSpeedTest().insertData(speedTestData, gqmResponse, meterRun.getRunId(), session);
                	break;

                case AIR:
                case POWER:
                case UNKNOWN:
                case WATER:
                default:
                    break;
                }
            }
            // commit the unit of work , meters will have flush only
            tx.commit();

            GQEDPConstants.logger.debug("************* DATA SUCCESSFULLY SAVED - END OF RUN  ***********");
        }
        catch (Exception e) {

            GQEDPConstants.logger.error("EDPFilter exception " + e.getMessage() + "for meter " + meterId);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        finally {
            // i dont believe the following lines are read since current session will hold one for the thread and need
            // not be closed
            // sriram , oct 3, 2013

            // try {
            // if (session.isOpen()) {
            // session.flush();
            // session.clear();
            // session.close();
            // }
            // }
            // catch (Exception e) {
            // e.printStackTrace();
            // }
        }// finally ends
    } // method ends
} // class ends

