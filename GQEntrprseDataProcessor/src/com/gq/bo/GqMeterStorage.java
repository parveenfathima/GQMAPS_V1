package com.gq.bo;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Storage;
import com.gq.meter.object.StorageSnpsht;
import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterStorage {

    public static void insertData(Storage storage, GQMeterResponse gqmResponse, Long runId) {

        String meterId = gqmResponse.getGqmid();
        String enterpriseId = meterId.split("_")[0];
        Session session = null;
        SessionFactory sessionFactory = null;
        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            GQEDPConstants.logger.debug("Start a process to read a HIBERNATE xml file in GQMeterStorage ");
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
            GQEDPConstants.logger.info("Session Successfully started for Storage");

            CPNId cid = storage.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);

            // inserting asset
            GQEDPConstants.logger.debug("Create a Query to inserting a values to asset table in GQMeterStorage");
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();
            GQEDPConstants.logger.debug("To check a condition for Asset Query Result in GQMeterStorage");

            if (result.size() == 0) {
                try {
                    Asset assetObj = storage.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(meterId + " Storage Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Storage Data failed to save in the Asset table ", e);
                }
            }

            // snapshot
            StorageSnpsht storageSnpsht = storage.getStorageSnpsht();
            try {
                // System.out.println("PRINTER : snap shot cpn id has been set");
                storageSnpsht.setId(cid);
                session.save(storageSnpsht);
                GQEDPConstants.logger.info(meterId + " Data successfully saved in the Storage Snapshot table ");
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(meterId + " Data failed to save in the Storage Snapshot table ", e);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Actual contact insertion will happen at this step
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
    }
}
