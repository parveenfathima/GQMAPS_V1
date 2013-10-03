package com.gq.bo;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;

import com.gq.meter.object.Computer;
import com.gq.meter.object.MeterRun;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.Printer;
import com.gq.meter.object.Storage;

import com.gq.util.DynamicSessionUtil;
import com.gq.util.GQEDPConstants;

/**
 * @author chandru
 * @change parveen
 */

// this class takes care of validating and distributing incoming requests
public final class GqEDPFilter {

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

        System.out.println("************* EDP STARTED - BEGINNING RUN " + runId + " ***********");

        meterId = meterId.split("_")[1];

        GQEDPConstants.logger.debug("Enterprise id : " + enterpriseId + " , Meter id : " + meterId);

        try {
            String dbInstanceName = "gqm" + enterpriseId;

            // This step will read hibernate.cfg.xml and prepare hibernate for use
            sessionFactory = DynamicSessionUtil.getSessionFactory(dbInstanceName);
            GQEDPConstants.logger.debug("Start to create a Session for requested Enterprise in GQEDPFilter");
            session = sessionFactory.getCurrentSession();
            GQEDPConstants.logger.debug("Session Created Successfully for GQEDPFilter");

            session.beginTransaction();

            GQEDPConstants.logger.debug(meterId + " Transaction successfully started ");
            // TODO : create a sequence table which includes runid and assetid and generates a unique key to replace the
            // redundant use of runid and assetid on all the tables
            GQEDPConstants.logger.debug("Ready to store the Data in Meter Run Table");
            GQEDPConstants.logger.debug("Details are" + runId + "\n" + meterId + "\n" + recordDT + "\n" + scanned
                    + "\n" + discovered + "\n" + runTimeMs);

            meterRun = new MeterRun(runId, meterId, recordDT, scanned, discovered, runTimeMs);
            session.save(meterRun);

            GQEDPConstants.logger.info(meterId + " Data successfully saved in the meterRun table ");
            session.getTransaction().commit();

            for (ProtocolData pdData : pdList) {

                switch (pdData.getProtocol()) {

                case COMPUTER:
                    Computer computerObj = gson.fromJson(pdData.getData(), Computer.class);
                    GqMeterComputer.insertData(computerObj, gqmResponse, meterRun.getRunId());
                    break;

                case PRINTER:
                    Printer printerData = gson.fromJson(pdData.getData(), Printer.class);
                    GqMeterPrinter.insertData(printerData, gqmResponse, meterRun.getRunId());
                    break;

                case NSRG:
                    NSRG nsrgData = gson.fromJson(pdData.getData(), NSRG.class);
                    GqMeterNSRG.insertData(nsrgData, gqmResponse, meterRun.getRunId());
                    break;

                case STORAGE:
                    Storage storageData = gson.fromJson(pdData.getData(), Storage.class);
                    GqMeterStorage.insertData(storageData, gqmResponse, meterRun.getRunId());
                    break;

                case AIR:
                    break;

                case POWER:
                    break;

                case UNKNOWN:
                    break;

                case WATER:
                    break;

                default:
                    break;
                }

            }
            System.out.println("************* DATA SUCCESSFULLY SAVED - END OF RUN  ***********");
        }
        catch (Exception e) {
            e.printStackTrace();
            GQEDPConstants.logger.error(meterId + " Transaction failed to start");
        }
        finally {
            try {

                if (session.isOpen()) {
                    session.flush();
                    session.clear();
                    session.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }// finally ends
    }
}
