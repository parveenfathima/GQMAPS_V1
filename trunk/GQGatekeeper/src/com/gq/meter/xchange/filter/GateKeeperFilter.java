package com.gq.meter.xchange.filter;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.object.AssetErr;
import com.gq.meter.object.MeterRun;
import com.gq.meter.xchange.object.EnterpriseMeter;
import com.gq.meter.xchange.object.GateKeeper;
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
        AssetErr assetErr = null;
        List<GQErrorInformation> gqerrList = gqmResponse.getErrorInformationList();

        Session session = null;
        MeterRun meterRun = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM EnterpriseMeter WHERE meter_id = :METER_ID";
            Query query = session.createQuery(hql);
            // TODO :meterid will come along with JSON, r8 now am hard coding it.
            query.setParameter("METER_ID", "meterid");
            List<EnterpriseMeter> result = query.list();// result size cannot be more than 1

            // TODO: what is the result size is more than 1
            if (result == null) {
                System.out.println("The meterid from the JSON != with the database value, Data insertion restricted");
                session.close();
                session.clear();
                return;
            }

            hql = "FROM GateKeeper WHERE meter_id = :METER_ID";
            query = session.createQuery(hql);
            // TODO :meterid will come along with JSON, r8 now am hard coding it.
            query.setParameter("METER_ID", "meterid");
            List<GateKeeper> auditResult = query.list();// result size cannot be more than 1
            // TODO: what is the result size is more than 1
            if (auditResult == null) {
                System.out
                        .println("The meterid from the JSON != with the database GateKeeper value, Data insertion restricted");
                session.close();
                session.clear();
                return;
            }

            // Compare created and expired datetime
            Date expirydDate = auditResult.get(0).getExpDttm();
            Date currDate = new Date();

            if (currDate.after(expirydDate)) {
                System.out.println("Expired");
                System.out.println("The date is expired please renewal the license, Data insertion restricted");
                session.close();
                session.clear();
                return;
            }
            else if (currDate.equals(expirydDate)) {
                System.out.println("Today its going to expire : " + expirydDate);
            }
            else if (currDate.before(expirydDate)) {
                System.out.println("The license wil expiry on : " + expirydDate);
            }

            // Check total asset scanned allowd
            if (auditResult.get(0).getScnRmng() == 0) {
                System.out.println("Scanning is not allowed, exceeds the license limit");
                session.close();
                session.clear();
                return;
            }

            // inserting runid - auto incremented
            // TODO : meterid will come along with JSON, r8 now am hard coding it.
            meterRun = new MeterRun(null, "meterid", recordDT, scanned, discovered, runTimeMs);
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

        int runId = meterRun.getRunId();
        System.out.println(" ###########" + runId);

        // for (ProtocolData pdData : pdList) {
        //
        // switch (pdData.getProtocol()) {
        //
        // case COMPUTER:
        // Computer computerObj = gson.fromJson(pdData.getData(), Computer.class);
        // GqMeterComputer.insertData(computerObj, gqmResponse, meterRun);
        // break;
        //
        // case PRINTER:
        // Printer printerData = gson.fromJson(pdData.getData(), Printer.class);
        // GqMeterPrinter.insertData(printerData, gqmResponse, meterRun);
        // break;
        //
        // case NSRG:
        // NSRG isrData = gson.fromJson(pdData.getData(), NSRG.class);
        // GqMeterNSRG.insertData(isrData, gqmResponse, meterRun);
        // break;
        //
        // case AIR:
        // break;
        //
        // case POWER:
        // break;
        //
        // case UNKNOWN:
        // break;
        //
        // case WATER:
        // break;
        //
        // default:
        // break;
        // }
        //
        // }
    }
}
