package com.gq.bo;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Storage;
import com.gq.meter.object.StorageSnpsht;

import com.gq.util.DynamicSessionUtil;
import com.gq.util.GQEDPConstants;

/**
 * @author parveen
 */

public class GqMeterStorage {

    public static void insertData(Storage storage, GQMeterResponse gqmResponse, Long runId) {

        String meterId = gqmResponse.getGqmid();
        String enterpriseId = meterId.split("_")[0];
        Session session = null;
        SessionFactory sessionFactory = null;

        try {

            GQEDPConstants.logger.debug("Start a process to read a HIBERNATE xml file in GQMeterStorage ");
            String dbInstanceName = "gqm" + enterpriseId;

            // This step will read hibernate.cfg.xml and prepare hibernate for use
            sessionFactory = DynamicSessionUtil.getSessionFactory(dbInstanceName);
            GQEDPConstants.logger.info("Session Factory for GQMeterStorage created Successfully");
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
            GQEDPConstants.logger.debug("Asset Id:" + assetId + "\nResult Size:" + result.size());
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
                storageSnpsht.setId(cid);
                session.save(storageSnpsht);
                GQEDPConstants.logger.info(meterId + " Data successfully saved in the Storage Snapshot table ");
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(meterId + " Data failed to save in the Storage Snapshot table ", e);
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
                    session.clear();
                    session.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }// finally ends
    }// method ends
}// class enda
