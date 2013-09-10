package com.gq.bo;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Printer;
import com.gq.meter.object.PrinterConnDevice;
import com.gq.meter.object.PrinterSnapshot;

import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterPrinter {

    public static void insertData(Printer printer, GQMeterResponse gqmResponse, Long runId) {
        String meterId = gqmResponse.getGqmid();
        String enterpriseId = meterId.split("_")[0];
        Session session = null;
        SessionFactory sessionFactory = null;
        try {
            GQEDPConstants.logger.debug("Start a process to read a HIBERNATE xml file in GQMeterPrinter ");
            String dbInstanceName = "gqm" + enterpriseId;
            String url = "jdbc:mysql://localhost:3306/" + dbInstanceName + "?autoReconnect=true";
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
            GQEDPConstants.logger.info("Session Successfully started for Printer");

            CPNId cid = printer.getId();
            String assetId = cid.getAssetId();
            cid.setRunId(runId);
            // inserting asset
            GQEDPConstants.logger.debug("Create a Query to inserting a values to asset table in GQMeterPrinter");
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();
            GQEDPConstants.logger.debug("Asset Id:" + assetId + "\nResult Size:" + result.size());
            GQEDPConstants.logger.debug("To check a condition for Asset Query Result in GQMeterPrinter");
            if (result.size() == 0) {
                try {
                    Asset assetObj = printer.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(meterId + " Printer Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Printer Data failed to save in the Asset table ", e);
                }
            }

            // snapshot
            PrinterSnapshot printerSnapshot = printer.getPrinterSnapShot();
            if (printerSnapshot.getIpAddr() != null) {
                try {
                    printerSnapshot.setId(cid);
                    session.save(printerSnapshot);
                    GQEDPConstants.logger.info(meterId + " Data successfully saved in the Printer Snapshot table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the Printer Snapshot table ", e);
                }
            }

            // connected device
            if (printer.getPrinterConnectedDevice() != null) {
                HashSet<PrinterConnDevice> printerConnectedDevice = printer.getPrinterConnectedDevice();
                try {
                    for (PrinterConnDevice printerConnDevice : printerConnectedDevice) {
                        printerConnDevice.getId().setRunId(runId);
                        session.merge(printerConnDevice);
                    }
                    GQEDPConstants.logger.info(meterId
                            + " Data successfully saved in the Printer Connected devices table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId
                            + " Data failed to save in the Printer Connected devices table  ", e);
                }
            }
            // Actual insertion will happen at this step
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
    }// method ends
}// class ends
