package com.gq.meter.xchange.filter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.xchange.controller.GQDataXchangeController;
import com.gq.meter.xchange.model.GqMeterErrorInfo;
import com.gq.meter.xchange.object.Enterprise;
import com.gq.meter.xchange.object.EnterpriseMeter;
import com.gq.meter.xchange.object.GateKeeper;
import com.gq.meter.xchange.object.MeterRun;
import com.gq.meter.xchange.util.GQGateKeeperConstants;
import com.gq.meter.xchange.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
// this class is takes care of validating and distributing incoming requests
public class GateKeeperFilter {

    /**
     * This method used to validate the un marshalled GQMeterResponse data
     * 
     * @param gqmResponse
     */
    public void process(GQMeterResponse gqmResponse) {
        // TODO : validate all the failure scenarios
        Session session = null;
        MeterRun meterRun = null;

        // parse the object and get the protocols and save them for now.....
        GQGateKeeperConstants.logger.info("Validating the unmarshlled GQMeterResponse data");

        String fwdUrl = null;
        String protocolId = null;
        String meterId = gqmResponse.getGqmid();

        Date recordDT = gqmResponse.getRecDttm();

        short scanned = gqmResponse.getAssetScanned();// total asset scanned from the input file
        short discovered = gqmResponse.getAssetDiscovered();// total asset actually discovered using GQMeter

        long runTimeMs = gqmResponse.getRunTimeMiliSeconds();

        GQGateKeeperConstants.logger.info(" GQMeter ID : " + meterId);
        GQGateKeeperConstants.logger.info(" Total asset scanned from the input file : " + scanned);
        GQGateKeeperConstants.logger.info(" Total asset actually discovered using GQMeter : " + discovered);

        // TODO : Analyze the scenario in which the assets discovered value may become 0.
        // TODO : Sort out the scenarios and decide whether to consider it as a run or not.

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            // ---------------------------------------------------------------------------------------------------------//
            GQGateKeeperConstants.logger.info("ENTERPRISEMETER");
            // checking meterid from the enterprisemeter table
            String hql = "FROM EnterpriseMeter WHERE meter_id = :METER_ID";
            Query query = session.createQuery(hql);
            query.setParameter("METER_ID", meterId);
            List<EnterpriseMeter> entMeterResult = query.list();

            // what if the entMeterResult size is more than 1
            // entMeterResult size cannot be more than 1, meter_id is always unique
            if (entMeterResult.size() == 0) {
                GQGateKeeperConstants.logger
                        .info("The meterid from the JSON != with the database value, Data insertion restricted");
                session.close();
                return;
            }

            String enterpriseId = entMeterResult.get(0).getEnterpriseId();
            String gqmId = gqmResponse.getGqmid();
            // Concatenating the enterpriseId with the meterId
            // The purpose of doing this is to invoke enterprise specific DB
            // instance on the GQEDProcessor application - tenant_identifier
            gqmId = enterpriseId + "_" + gqmId;
            gqmResponse.setGqmid(gqmId);
            // ---------------------------------------------------------------------------------------------------------//
            // protocol should be entered into DB in lower case only - Type safety
            protocolId = entMeterResult.get(0).getProtocolId();

            List<ProtocolData> pdList = gqmResponse.getAssetInformationList();
            List<ProtocolData> pdValidList = new LinkedList<ProtocolData>();

            if (protocolId != GQGateKeeperConstants.PROTOCOL_IT) {

                for (ProtocolData pdData : pdList) {
                    GQGateKeeperConstants.logger.info("protocolId : " + protocolId + " from JSON : "
                            + pdData.getProtocol().toString());
                    if (pdData.getProtocol().toString().toLowerCase().trim().equals(protocolId)) {
                        pdValidList.add(pdData);
                        GQGateKeeperConstants.logger.info("valid meter data has been added into pdValidList");
                    }
                }// for loop ends

                // Actual number of assets that are matches with the type of meter
                // for which the enterprise is registered for.
            }
            else {
                pdValidList.addAll(pdList);
            }
            discovered = (short) pdValidList.size();
            GQGateKeeperConstants.logger.info(" Total number of assets after meter validation***  : " + discovered
                    + " : " + pdList.size());

            // ---------------------------------------------------------------------------------------------------------//
            // checking meterid from the GateKeeper table
            hql = "FROM GateKeeper WHERE enterprise_id = :ENTERPRISE_ID";// prepared stmt
            GQGateKeeperConstants.logger.info("GATEKEEPER ::: " + hql);
            query = session.createQuery(hql);
            query.setParameter("ENTERPRISE_ID", enterpriseId);
            List<GateKeeper> gatekeeperResult = query.list();
            GQGateKeeperConstants.logger.info("gatekeeperResult : : : " + gatekeeperResult.toString());
            // what if the gatekeeperResult size is more than 1
            // gatekeeperResult size cannot be more than 1, meter_id is always unique
            if (gatekeeperResult == null) {
                System.out
                        .println("The meterid from the JSON != with the database GateKeeper value, Data insertion restricted");
                session.close();
                return;
            }

            // char checkCondition = gatekeeperResult.get(0).getChkCndtn();
            // int scanRemaining = gatekeeperResult.get(0).getScnRmng();
            // if (checkCondition == 'c') {
            // GQGateKeeperConstants.logger.info("Validating the license based on scan reamining");
            // // Check total asset scanned allowed
            // if (scanRemaining < discovered || scanRemaining <= 0) {
            // GQGateKeeperConstants.logger.info("Scanning is not allowed, exceeds the license limit");
            // session.close();
            // return;
            // }
            // }
            // else if (checkCondition == 'e') {
            GQGateKeeperConstants.logger.info("Validating the license based expiry date");
            // Compare today date and expired date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String expirydDate = sdf.format(gatekeeperResult.get(0).getExpDttm());
            String currDate = sdf.format(new Date());
            GQGateKeeperConstants.logger.info("Curr date : " + currDate + " expirydate : " + expirydDate);
            int dateValue = expirydDate.compareTo(currDate);

            GQGateKeeperConstants.logger.info("==============  " + dateValue);
            if (dateValue == -1) {
                GQGateKeeperConstants.logger.info("License is expired");
                GQGateKeeperConstants.logger
                        .info("The date is expired please renewal the license, Data insertion restricted");
                session.close();
                return;
            }
            else if (dateValue == 0) {
                GQGateKeeperConstants.logger.info("Today license is going to expire : " + expirydDate);
            }
            else if (dateValue == 1) {
                GQGateKeeperConstants.logger.info("The license wil expiry on : " + expirydDate);
            }
            // }

            // scanRemaining = scanRemaining - discovered;
            // GQGateKeeperConstants.logger.info("Scan remain : " + scanRemaining);
            // update gatekeeper table once decremented the count

            GateKeeper gateKeeper = (GateKeeper) session.load(GateKeeper.class, gatekeeperResult.get(0)
                    .getEnterpriseId());
            // gateKeeper.setScnRmng(scanRemaining);

            // ---------------------------------------------------------------------------------------------------------//
            GQGateKeeperConstants.logger.info("ENTERPRISE" + enterpriseId.trim());
            String enthql = "FROM Enterprise WHERE enterprise_id = :ENTERPRISE_ID";

            GQGateKeeperConstants.logger.info("ENTERPRISE ::: " + enthql);
            Query entQuery = session.createQuery(enthql);
            entQuery.setParameter("ENTERPRISE_ID", enterpriseId.trim());
            List<Enterprise> entResult = entQuery.list();
            GQGateKeeperConstants.logger.info("GATEKEEPER*****************************" + entResult.get(0).getAns1());
            // what if the entMeterResult size is more than 1
            // entMeterResult size cannot be more than 1, meter_id is always unique
            if (entResult.size() == 0) {
                GQGateKeeperConstants.logger
                        .info("The meterid from the JSON != with the database value, Data insertion restricted");
                session.close();
                return;
            }

            // Check whether to store or forward the data to GQEDP
            char storeOrForward = entResult.get(0).getStoreFwd();
            // If fwd then get the fwd URL and pass the json to this
            if (storeOrForward == 'f') {
                fwdUrl = entResult.get(0).getFwdUrl();
                if (fwdUrl == null || fwdUrl == "") {
                    GQGateKeeperConstants.logger.info("Forward URL is null/empty in the DB");
                    session.close();
                    return;
                }
            }
            else {
                // Local GQEDP application URL
                fwdUrl = GQGateKeeperConstants.GQEDP_URL;
            }

            // ---------------------------------------------------------------------------------------------------------//
            // inserting runid - auto incremented
            meterRun = new MeterRun(meterId, recordDT, scanned, discovered, runTimeMs);
            Integer runid = (Integer) session.save(meterRun);
            session.getTransaction().commit();

            gqmResponse.setRunid(runid);
            // TODO: solve the error which occured while saving the data into asset_err table
            // Inserting error information into asset_err table
            GqMeterErrorInfo.insertErrorInfo(gqmResponse.getErrorInformationList(), gqmResponse.getRunid());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }// finally ends
        if (protocolId != null) {
            GQDataXchangeController xChange = new GQDataXchangeController();
            xChange.sendToEntDataProcessor(fwdUrl, protocolId, gqmResponse);
        }
    }
}
