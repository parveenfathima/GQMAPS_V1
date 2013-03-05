package com.gq.bo;

import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.object.Computer;
import com.gq.meter.object.MeterRun;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.Printer;
import com.gq.util.HibernateUtil;

// this class is takes care of validating and distributing incoming requests
public class GateKeeperFilter {

    public void process(GQMeterResponse gqmResponse) {

        // parse the object and get the protocols and save them for now..... ss , feb 19 , 2013
        System.out.println("ready to parse and save....");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String meterId = gqmResponse.getGqmid();
        Date recordDT = gqmResponse.getRecDttm();
        short scanned = gqmResponse.getAssetScanned();
        short discovered = gqmResponse.getAssetDiscovered();
        long runTimeMs = gqmResponse.getRunTimeMiliSeconds();

        System.out.println(" MeterID : " + meterId);
        System.out.println(" Total Asset Scanned : " + scanned);

        List<ProtocolData> pdList = gqmResponse.getAssetInformationList();

        List<GQErrorInformation> gqerrList = gqmResponse.getErrorInformationList();

        Session session = null;
        MeterRun meterRun = null;
        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            // inserting runid
            // TODO : create a squence table which includes runid and assetid and generates a unique key to replace the
            // redendunt use of runid and assetid on all the tables
            meterRun = new MeterRun(null, "meter_id", recordDT, scanned, discovered, runTimeMs);
            session.save(meterRun);

            session.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
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

        GqMeterErrorInfo.insertErrorInfo(gqerrList, meterRun);

        for (ProtocolData pdData : pdList) {

            switch (pdData.getProtocol()) {

            case COMPUTER:
                Computer computerObj = gson.fromJson(pdData.getData(), Computer.class);
                GqMeterComputer.insertData(computerObj, gqmResponse, meterRun);
                break;

            case PRINTER:
                Printer printerData = gson.fromJson(pdData.getData(), Printer.class);
                GqMeterPrinter.insertData(printerData, gqmResponse, meterRun);
                break;

            case NSRG:
                NSRG isrData = gson.fromJson(pdData.getData(), NSRG.class);
                GqMeterNSRG.insertData(isrData, gqmResponse, meterRun);
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
    }
}
