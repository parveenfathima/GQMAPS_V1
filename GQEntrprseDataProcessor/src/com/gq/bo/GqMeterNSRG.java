package com.gq.bo;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.NSRGConnDevice;
import com.gq.meter.object.NSRGSnapshot;
import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterNSRG {

    public static void insertData(NSRG nsrg, GQMeterResponse gqmResponse, int runId) {
        Session session = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = nsrg.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);

            // System.out.println("NSRG : runnid set into the cpn id : " + cid.getRunId());

            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();

            // inserting asset
            if (result.size() == 0) {
                try {
                    Asset assetObj = nsrg.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " NSRG Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " NSRG Data failed to save in the Asset table " + e.getMessage());
                }
            }

            // snapshot
            NSRGSnapshot nsrgSnapshot = nsrg.getNsrgSnapShot();
            try {
                nsrgSnapshot.setId(cid);
                session.save(nsrgSnapshot);
                GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Data  successfully saved in the NSRG Snapshot table ");
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Data failed to save in the NSRG Snapshot table " + e.getMessage());
            }

            // connected device
            if (nsrg.getNsrgConnectedDevices() != null) {
                HashSet<NSRGConnDevice> nsrgConnectedDevices = nsrg.getNsrgConnectedDevices();

                try {
                    for (NSRGConnDevice nsrgConnDevice : nsrgConnectedDevices) {
                        nsrgConnDevice.getId().setRunId(runId);
                        session.merge(nsrgConnDevice);
                    }// for ends

                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data successfully saved in the NSRG Connected devices table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data failed to save in the NSRG Connected devices table " + e.getMessage());
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
}
