/**
 * 
 */
package com.gq.meter.model;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.GateKeeper;
import com.gq.meter.object.GateKeeperAudit;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
public class GateKeeperModel {

    public void addGateKeeperAudit(GateKeeperAudit gkAudit) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            GateKeeper gkeeper = null;
            // Check gatekeeper instance exists or not
            String hql = "FROM GateKeeper WHERE enterpriseId = :ENT_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ENT_ID", gkAudit.getEnterpriseId());
            List<GateKeeper> result = query.list();

            GQGateKeeperConstants.logger.info("Is gkeeper instance exists ? :" + gkeeper);

            if (result == null || result.size() == 0) {
                // if no gatekeeper instanace exists then create one and save it.
                gkeeper = new GateKeeper();
                gkeeper.setEnterpriseId(gkAudit.getEnterpriseId());
                gkeeper.setExpDttm(gkAudit.getExpDttm());
            }
            else {
                // if gatekeeper instance exists then only update the expiry
                // date and scan remaining
                gkeeper = result.get(0);

                Date gkAuditDate = gkAudit.getExpDttm();
                Date gkeeperDate = gkeeper.getExpDttm();

                if (gkAuditDate.after(gkeeperDate)) {
                    gkeeper.setExpDttm(gkAudit.getExpDttm());
                }

            }
            session.save(gkeeper);// saving gatekeeper
            session.save(gkAudit);// saving gatekeeperaudit
            session.getTransaction().commit();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the gatekeeper ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                throw new Exception(e);
            }
        }

    }

    /**
     * This method used to fetch all the expiry date for enterprise
     * 
     * @return
     */
    public int getExpiryDate(String entpId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "SELECT  DATEDIFF(expDttm, now()) FROM GateKeeper WHERE enterpriseId = :ENT_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ENT_ID", entpId);
            List<Integer> gkList = query.list();
            int i = gkList.get(0);

            return i;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the expirydate from gatekeeper ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }
}
