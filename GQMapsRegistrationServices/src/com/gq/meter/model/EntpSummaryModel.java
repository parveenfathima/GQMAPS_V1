/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.EntpSummary;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author GQ
 * 
 */
public class EntpSummaryModel {

    public List<EntpSummary> getEntpSummary(String entpId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "select a.enterpriseId, a.eName, count(b.meterId), c.expDttm from Enterprise a, EnterpriseMeter b, GateKeeper c  where a.enterpriseId = b.enterpriseId AND a.enterpriseId = c.enterpriseId AND a.enterpriseId=:ENT_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ENT_ID", entpId);
            List<EntpSummary> result = query.list();
            return result;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterpriseid ", e);
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
