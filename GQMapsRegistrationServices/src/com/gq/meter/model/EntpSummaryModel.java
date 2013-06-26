/**
 * 
 */
package com.gq.meter.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.EntpSummary;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Rathish
 * 
 */
public class EntpSummaryModel {
    // TODO: combine the two methods

    public List<EntpSummary> getEntpSummary(String entpId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "select a.sid, a.enterpriseId, a.eName, count(b.meterId), c.expDttm from Enterprise a, EnterpriseMeter b, GateKeeper c  where a.enterpriseId = b.enterpriseId AND a.enterpriseId = c.enterpriseId AND a.enterpriseId=:ENT_ID";
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

    public List<EntpSummary> getEntpSummaryList() throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "select a.sid, a.enterpriseId, a.blCd, a.eName, a.phone, a.email, a.userId, a.passwd, a.secQtn1, a.ans1, a.secQtn2, a.ans2, a.storeFwd, a.fwdUrl, a.noOfEmpl, a.entSqft, a.entAssetCount, a.dcSqft, a.dcAssetCount, a.dcUsePctg, a.dcTemp, a.regCmplt, a.active, a.comments, a.creDttm, count(b.meterId), c.expDttm from Enterprise a, EnterpriseMeter b, GateKeeper c  where a.enterpriseId = b.enterpriseId AND a.enterpriseId = c.enterpriseId group by a.enterpriseId";
            
            Query query = session.createQuery(hql);
            List<Object[]> summaryResult = query.list();
            System.out.println(summaryResult);
            
            List<EntpSummary> entpSummaryList = new ArrayList<EntpSummary>();
            
            for(Object[] list: summaryResult){
            	EntpSummary entpSummary = new EntpSummary();
            	entpSummary.setSid((Short) list[0]);            	
            	entpSummary.setEnterpriseId((String) list[1]);
            	//TODO: set all the fields
            	

            	entpSummaryList.add(entpSummary);
            }
			
			
            return entpSummaryList;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterpriseid ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) 
                {
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
