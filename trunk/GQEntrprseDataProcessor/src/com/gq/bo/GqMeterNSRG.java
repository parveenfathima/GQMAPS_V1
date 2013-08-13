package com.gq.bo;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.NSRGConnDevice;
import com.gq.meter.object.NSRGSnapshot;
import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterNSRG {

    public static void insertData(NSRG nsrg, GQMeterResponse gqmResponse, Long runId) {
        Session session = null;
        SessionFactory sessionFactory = null;
        String meterId = gqmResponse.getGqmid();
        // meterId = meterId.split("_")[1];
        String enterpriseId = meterId.split("_")[0];
        GQEDPConstants.logger.debug("Meter Id from NSRG:" + meterId);
        GQEDPConstants.logger.debug("Enterprise Id from NSRG:" + enterpriseId);
        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            GQEDPConstants.logger.debug("Start a process to read a HIBERNATE xml file in GQMeterNSRG ");
            String url = "jdbc:mysql://192.168.1.95:3306/gqm" + enterpriseId + "?autoReconnect=true";

            if (HibernateUtil.SessionFactoryListMap.containsKey(enterpriseId)) {
                if (HibernateUtil.SessionFactoryListMap.get(enterpriseId) == null) {
                    sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                    HibernateUtil.SessionFactoryListMap.put(enterpriseId, sessionFactory);
                }
                else {
                    sessionFactory = HibernateUtil.SessionFactoryListMap.get(enterpriseId);
                }
            }
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            GQEDPConstants.logger.info("Session Successfully started for NSRG");

            CPNId cid = nsrg.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);

            // inserting asset
            GQEDPConstants.logger.debug("Create a Query to inserting a values to asset table in GQMeterNSRG");
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();
            GQEDPConstants.logger.debug("To check a condition for Asset Query Result in GQMeterNSRG");

            if (result.size() == 0) {
                try {
                    Asset assetObj = nsrg.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(meterId + " NSRG Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " NSRG Data failed to save in the Asset table ", e);
                }
            }

            // snapshot
            NSRGSnapshot nsrgSnapshot = nsrg.getNsrgSnapShot();
            if (nsrgSnapshot.getIpAddr() != null) {
                try {
                    nsrgSnapshot.setId(cid);
                    session.save(nsrgSnapshot);
                    GQEDPConstants.logger.info(meterId + " Data  successfully saved in the NSRG Snapshot table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the NSRG Snapshot table ", e);
                }
            }
            // connected device
            if (nsrg.getNsrgConnectedDevices() != null) {
                HashSet<NSRGConnDevice> nsrgConnectedDevices = nsrg.getNsrgConnectedDevices();

                try {
                    for (NSRGConnDevice nsrgConnDevice : nsrgConnectedDevices) {
                        nsrgConnDevice.getId().setRunId(runId);
                        session.merge(nsrgConnDevice);
                    }// for ends

                    GQEDPConstants.logger.info(meterId
                            + " Data successfully saved in the NSRG Connected devices table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the NSRG Connected devices table ",
                            e);
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
                    sessionFactory.close();
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
}
