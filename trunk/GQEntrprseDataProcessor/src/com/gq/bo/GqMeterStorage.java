package com.gq.bo;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Storage;
import com.gq.meter.object.StorageSnpsht;
import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterStorage {

    public static void insertData(Storage storage, GQMeterResponse gqmResponse, int runId) {
        Session session = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = storage.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);

            // System.out.println("PRINTER : runnid set into the cpn id : " + cid.getRunId());

            // inserting asset
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();

            if (result.size() == 0) {
                try {
                    Asset assetObj = storage.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Storage Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Storage Data failed to save in the Asset table " + e.getMessage());
                }
            }

            // snapshot
            StorageSnpsht storageSnpsht = storage.getStorageSnpsht();
            try {
                // System.out.println("PRINTER : snap shot cpn id has been set");
                storageSnpsht.setId(cid);
                session.save(storageSnpsht);
                GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Data successfully saved in the Storage Snapshot table ");
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Data failed to save in the Storage Snapshot table " + e.getMessage());
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
