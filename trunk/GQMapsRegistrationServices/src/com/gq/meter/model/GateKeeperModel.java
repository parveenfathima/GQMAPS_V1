/**
 * 
 */
package com.gq.meter.model;

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
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            // Check gatekeeper instance exists or not
            GateKeeper gkeeper = (GateKeeper) session.get(gkAudit.getEnterpriseId(), GateKeeper.class);
            GQGateKeeperConstants.logger.info("Is gkeeper instance exists ? :" + gkeeper);
            if (gkeeper == null) {
                // if no gatekeeper instanace exists then create one and save it.
                gkeeper = new GateKeeper();
                gkeeper.setEnterpriseId(gkAudit.getEnterpriseId());
                gkeeper.setExpDttm(gkAudit.getExpDttm());
            }
            else {
                // if gatekeeper instance exists then only update the expiry date and scan remaining
                gkeeper.setExpDttm(gkAudit.getExpDttm());
            }

            session.save(gkeeper);// saving gatekeeper

            session.save(gkAudit);// saving gatekeeperaudit
            session.getTransaction().commit();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the enterprise ", e);
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

    public void addGateKeeper(GateKeeper gKeeper) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(gKeeper);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the enterprise ", e);
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

}
