/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.MeterRun;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Rathish
 * @param <MeterRun>
 * 
 */
public class MeterRunModel {

    public List<MeterRun> getMeterRun(String entpId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "SELECT COUNT(meterId) FROM EnterpriseMeter WHERE enterpriseId = :ENT_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ENT_ID", entpId);
            List<MeterRun> meterResult = query.list();
            return meterResult;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the number of meters ", e);
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
