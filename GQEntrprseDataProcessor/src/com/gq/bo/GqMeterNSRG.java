package com.gq.bo;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.MeterRun;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.NSRGConnDevice;
import com.gq.meter.object.NSRGSnapshot;
import com.gq.util.HibernateUtil;

public class GqMeterNSRG {

    public static void insertData(NSRG nsrg, GQMeterResponse gqmResponse, MeterRun meterRun) {
        Session session = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = nsrg.getId();
            String assetId = cid.getAssetId();

            int runId = meterRun.getRunId();
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
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // snapshot
            NSRGSnapshot nsrgSnapshot = nsrg.getNsrgSnapShot();
            try {
                nsrgSnapshot.setId(cid);
                session.save(nsrgSnapshot);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // connected device
            if (nsrg.getNsrgConnectedDevices() != null) {
                HashSet<NSRGConnDevice> nsrgConnectedDevices = nsrg.getNsrgConnectedDevices();
                for (NSRGConnDevice nsrgConnDevice : nsrgConnectedDevices) {

                    try {
                        nsrgConnDevice.getId().setRunId(runId);
                        session.merge(nsrgConnDevice);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }// for ends
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
