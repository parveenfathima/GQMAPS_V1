/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.Enterprise;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
public class EnterpriseModel {

    /**
     * This method used to authenticate the user
     * 
     * @param authObject
     * @return
     * @throws Exception
     */
    public boolean authenticate(Enterprise authObject) throws Exception {
        boolean authValue = false;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM Enterprise WHERE user_id = :USER_ID and passwd = :PASSWORD";
            Query query = session.createQuery(hql);
            query.setParameter("USER_ID", authObject.getUserId());
            query.setParameter("PASSWORD", authObject.getPasswd());
            List<Enterprise> entAuthResult = query.list();
            if (entAuthResult.size() != 0) {
                authValue = true;
            }
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises ", e);
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
        return authValue;
    }

    /**
     * This method used to fetch all the enterprise registered for GQMaps
     * 
     * @return
     * @throws Exception
     */
    public List<Enterprise> getAllEnterprises() throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM Enterprise";
            Query query = session.createQuery(hql);
            List<Enterprise> entMeterResult = query.list();
            return entMeterResult;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises ", e);
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
     * This method used to create the new enterprise
     * 
     * @param entObject
     * @throws Exception
     */
    public void addEnterprise(Enterprise entObject) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(entObject);
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

    /**
     * ON HOLD R8 NOW... WAITING FOR DB CHANGES
     * 
     * @param entObject
     * @throws Exception
     */
    public void updateEnterprise(Enterprise entObject) throws Exception {
        Session session = null;
        try {
            GQGateKeeperConstants.logger.info("###########################################");
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Enterprise oldEntObject = (Enterprise) session.load(Enterprise.class, entObject.getEnterpriseId());
            // only update the changed values
            oldEntObject.setEnterpriseId(entObject.getEnterpriseId());
            oldEntObject.setUserId(entObject.getUserId());
            oldEntObject.setPasswd(entObject.getPasswd());
            oldEntObject.setStoreFwd(entObject.getStoreFwd());
            oldEntObject.setFwdUrl(entObject.getFwdUrl());
            session.getTransaction().commit();
            GQGateKeeperConstants.logger.info("###########################################");
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
