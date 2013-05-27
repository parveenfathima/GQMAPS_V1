/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.EnterpriseMeter;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author GQ
 * 
 */
public class EnterpriseMeterModel {

    /**
     * This method used to fetch all the enterprises
     * 
     * @return
     */
    public List<EnterpriseMeter> getEnterpriseMeters(String entpId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM EnterpriseMeter WHERE enterpriseId = :ENT_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ENT_ID", entpId);
            List<EnterpriseMeter> entMeterResult = query.list();
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
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * This method is used to add new enterprise meter
     * 
     * @param entMeterObject
     * @throws Exception
     */
    public void addEnterpriseMeter(EnterpriseMeter entMeterObject) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(entMeterObject);
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
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

}
