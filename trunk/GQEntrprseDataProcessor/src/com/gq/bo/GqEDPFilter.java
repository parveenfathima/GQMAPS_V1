package com.gq.bo;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.object.Computer;
import com.gq.meter.object.Meter;
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
        meterId = meterId.split("_")[1];

        int runId = gqmResponse.getRunid();
        Date recordDT = gqmResponse.getRecDttm();
        short scanned = gqmResponse.getAssetScanned();
        short discovered = gqmResponse.getAssetDiscovered();
        long runTimeMs = gqmResponse.getRunTimeMiliSeconds();

        System.out.println(" MeterID : " + meterId);
        System.out.println(" Total Asset Scanned : " + scanned);

        List<ProtocolData> pdList = gqmResponse.getAssetInformationList();

        Session session = null;
        MeterRun meterRun = null;
        Meter meter = null;
        String protocolId = "protocolid";
        String descr = "descr";
        String address = "address";
        String phone = "phone";
        char storeFwd = 'Y';
        String fwdUrl = "fwdUrl";
        Date creDttm = new Date();
        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            GQEDPConstants.logger.debug(meterId + " Transaction successfully started ");
            String hql = "FROM Meter WHERE meterId = :METER_ID";
            Query query = session.createQuery(hql);
            query.setParameter("METER_ID", meterId);
            List<?> result = query.list();

            if (result.size() == 0) {
                try {
                    meter = new Meter(meterId, protocolId, descr, address, phone, storeFwd, fwdUrl, creDttm);
                    session.save(meter);
                    GQEDPConstants.logger.info(meterId + " Data successfully saved in the meter table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the meter table ", e);
                }
            }
            // inserting runid
            // TODO : create a squence table which includes runid and assetid and generates a unique key to replace the
            // redendunt use of runid and assetid on all the tables
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
