package com.gq.meter.xchange.model;

import java.util.List;

import org.hibernate.Session;

import com.gq.meter.GQErrorInformation;
import com.gq.meter.object.AssetErr;
import com.gq.meter.xchange.util.HibernateUtil;

public class GqMeterErrorInfo {

    public static void insertErrorInfo(List<GQErrorInformation> gqerrList, int runId) {
        Session session = null;

        try {

            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            long sid = 0L;

            for (GQErrorInformation errInfo : gqerrList) {
                for (String error : errInfo.getErrorList()) {
                    AssetErr assetErr = new AssetErr(sid, runId, errInfo.getAssetDescr(), error);
                    session.save(assetErr);
                }
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
    }// method ends
}
