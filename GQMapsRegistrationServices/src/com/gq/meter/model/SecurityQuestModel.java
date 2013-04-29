/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.Protocol;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
public class SecurityQuestModel {

    /**
     * This method used to fetch all the security questions
     * 
     * @return
     * @throws Exception
     */
    public List<Protocol> getAllSecQuestions() throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM SecurityQuestion";
            Query query = session.createQuery(hql);
            List<Protocol> protoResult = query.list();
            return protoResult;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the Security Questions ", e);
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
