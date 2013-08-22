package com.gq.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterData;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.object.CompSnapshot;
import com.gq.meter.object.Computer;
import com.gq.meter.object.MeterRun;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.Printer;
import com.gq.meter.object.Storage;
import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

// this class takes care of validating and distributing incoming requests
public class GqEDPFilter {

    public void process(GQMeterResponse gqmResponse) {

        GQEDPConstants.logger.info("Enterprise data processor is ready to parse and save....");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String meterId = gqmResponse.getGqmid();
        String enterpriseId = meterId.split("_")[0];
        GQEDPConstants.logger.debug("Enterpriseid from GQEDPFilter: " + enterpriseId);
        meterId = meterId.split("_")[1];
        GQEDPConstants.logger.debug("Meterid from GQEDPFilter: " + meterId);
        Long runId = gqmResponse.getRunid();
        Date recordDT = gqmResponse.getRecDttm();
        short scanned = gqmResponse.getAssetScanned();
        short discovered = gqmResponse.getAssetDiscovered();
        long runTimeMs = gqmResponse.getRunTimeMiliSeconds();

        System.out.println(" MeterID : " + meterId);
        System.out.println(" Total Asset Scanned : " + scanned);

        List<ProtocolData> pdList = gqmResponse.getAssetInformationList();
        Session session = null;
        MeterRun meterRun = null;
        SessionFactory sessionFactory = null;

        try {

            GQEDPConstants.logger.debug("Start to read a hibernate file for GQEDPFilter");
            String dbInstanceName = "gqm" + enterpriseId;

            String url = "jdbc:mysql://192.168.1.95:3306/" + dbInstanceName + "?autoReconnect=true";
            // This step will read hibernate.cfg.xml and prepare hibernate for use

            if (HibernateUtil.SessionFactoryListMap.containsKey(dbInstanceName)) {
                sessionFactory = HibernateUtil.SessionFactoryListMap.get(dbInstanceName);
                if (sessionFactory == null) {
                    sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                    HibernateUtil.SessionFactoryListMap.put(dbInstanceName, sessionFactory);
                }
            }
            else {
                sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                HibernateUtil.SessionFactoryListMap.put(dbInstanceName, sessionFactory);
            }
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            GQEDPConstants.logger.debug(meterId + " Transaction successfully started ");
            // TODO : create a sequence table which includes runid and assetid and generates a unique key to replace the
            // redendunt use of runid and assetid on all the tables
            GQEDPConstants.logger.debug("Ready to store the Data in Meter Run Table");
            GQEDPConstants.logger.debug("Details are" + runId + "\n" + meterId + "\n" + recordDT + "\n" + scanned
                    + "\n" + discovered + "\n" + runTimeMs);
            meterRun = new MeterRun(runId, meterId, recordDT, scanned, discovered, runTimeMs);
            session.save(meterRun);
            GQEDPConstants.logger.info(meterId + " Data successfully saved in the meterRun table ");
            session.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            GQEDPConstants.logger.error(meterId + " Transaction failed to start");
        }
        finally {
            try {
                if (session.isOpen()) {
                    // sessionFactory.close();
                    session.flush();
                    session.close();
                    session.clear();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }// finally ends

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
        System.out.println("************* DATA SUCCESSFULLY SAVED ***********");
    }
}
