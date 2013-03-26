package com.gq.meter.xchange.filter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.xchange.controller.GQDataXchangeController;
import com.gq.meter.xchange.object.MeterRun;
import com.gq.meter.xchange.object.EnterpriseMeter;
import com.gq.meter.xchange.object.GateKeeper;
import com.gq.meter.xchange.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
// this class is takes care of validating and distributing incoming requests
public class GateKeeperFilter {

    public void process(GQMeterResponse gqmResponse) {

        Session session = null;
        MeterRun meterRun = null;

        // parse the object and get the protocols and save them for now..... ss , feb 19 , 2013
        System.out.println("ready to parse and save....");

        String meterId = gqmResponse.getGqmid();
        gqmResponse.setGqmid(meterId);
        String fwdUrl = null;
        String protocolId = null;
        Date recordDT = gqmResponse.getRecDttm();
        short scanned = gqmResponse.getAssetScanned();
        short discovered = gqmResponse.getAssetDiscovered();
        long runTimeMs = gqmResponse.getRunTimeMiliSeconds();

        System.out.println(" MeterID : " + meterId);
        System.out.println(" Total Asset Scanned : " + scanned);

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            // ---------------------------------------------------------------------------------------------------------//
            // checking meterid from the enterprisemeter table
            String hql = "FROM EnterpriseMeter WHERE meter_id = :METER_ID";
            Query query = session.createQuery(hql);
            // TODO :meterid will come along with JSON, r8 now am hard coding it.
            query.setParameter("METER_ID", meterId);
            List<EnterpriseMeter> entMeterResult = query.list();// result size cannot be more than 1
            // TODO: what is the result size is more than 1
            if (entMeterResult.size() == 0) {
                System.out.println("The meterid from the JSON != with the database value, Data insertion restricted");
                session.close();
                return;
            }

            String enterpriseId = entMeterResult.get(0).getEnterpriseId();
            String gqmId = gqmResponse.getGqmid();
            gqmId = enterpriseId + "_" + gqmId;
            gqmResponse.setGqmid(gqmId);

            // Check whether to store or forward the data to GQEDP
            char storeOrForward = entMeterResult.get(0).getStoreFwd();

            protocolId = entMeterResult.get(0).getProtocolId();

            if (storeOrForward == 'f') {
                fwdUrl = entMeterResult.get(0).getFwdUrl();// If fwd then get the fwd URL and pass the json to this
            }
            // ---------------------------------------------------------------------------------------------------------//
            // checking meterid from the GateKeeper table
            hql = "FROM GateKeeper WHERE meter_id = :METER_ID";
            query = session.createQuery(hql);
            // TODO :meterid will come along with JSON, r8 now am hard coding it.
            query.setParameter("METER_ID", meterId);
            List<GateKeeper> auditResult = query.list();// result size cannot be more than 1
            // TODO: what if the result size is more than 1
            if (auditResult == null) {
                System.out
                        .println("The meterid from the JSON != with the database GateKeeper value, Data insertion restricted");
                session.close();
                return;
            }

            // ---------------------------------------------------------------------------------------------------------//
            // Compare today date and expired date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String expirydDate = sdf.format(auditResult.get(0).getExpDttm());
            String currDate = sdf.format(new Date());
            System.out.println("Curr date : " + currDate + " expirydate : " + expirydDate);
            int dateValue = expirydDate.compareTo(currDate);

            System.out.println("==============  " + dateValue);
            if (dateValue == -1) {
                System.out.println("Expired");
                System.out.println("The date is expired please renewal the license, Data insertion restricted");
                session.close();
                return;
            }
            else if (dateValue == 0) {
                System.out.println("Today license is going to expire : " + expirydDate);
            }
            else if (dateValue == 1) {
                System.out.println("The license wil expiry on : " + expirydDate);
            }

            // ---------------------------------------------------------------------------------------------------------//
            char checkCondition = auditResult.get(0).getChkCndtn();
            if (checkCondition == 'y') {
                int scanRemaining = auditResult.get(0).getScnRmng();
                // Check total asset scanned allowed
                if (scanRemaining <= 0) {
                    System.out.println("Scanning is not allowed, exceeds the license limit");
                    session.close();
                    return;
                }
                scanRemaining = scanRemaining - discovered;
                System.out.println("Scan remain : " + scanRemaining);
                // update gatekeeper table once decremented the count

                GateKeeper gkAudit = (GateKeeper) session.load(GateKeeper.class, auditResult.get(0).getMeterId());

                gkAudit.setScnRmng(scanRemaining);
            }

            // ---------------------------------------------------------------------------------------------------------//
            // inserting runid - auto incremented
            meterRun = new MeterRun(meterId, recordDT, scanned, discovered, runTimeMs);
            Integer runid = (Integer) session.save(meterRun);
            session.flush();

            gqmResponse.setRunid(runid);

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
        if (protocolId != null) {
            GQDataXchangeController.sendToEntDataProcessor(fwdUrl, protocolId, gqmResponse);
        }
    }
}
