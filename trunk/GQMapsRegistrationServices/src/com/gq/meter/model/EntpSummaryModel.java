/**
 * 
 */
package com.gq.meter.model;

import java.util.ArrayList;
import java.util.Date;
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
            // String hql =
            // "select a.sid, a.enterpriseId, a.blCd, a.eName, a.phone, a.email, a.userId, a.passwd, a.secQtn1, a.ans1, a.secQtn2, a.ans2, a.storeFwd, a.fwdUrl, a.noOfEmpl, a.entSqft, a.entAssetCount, a.dcSqft, a.dcAssetCount, a.dcUsePctg, a.dcTemp, a.regCmplt, a.active, a.comments, a.creDttm, count(b.meterId), c.expDttm from Enterprise a, EnterpriseMeter b, GateKeeper c  where a.enterpriseId = b.enterpriseId AND a.enterpriseId = c.enterpriseId group by a.enterpriseId";
            String hql = "select e.sid, e.enterpriseId, e.blCd, e.eName, e.phone, e.email, e.userId, e.passwd, e.secQtn1, e.ans1, e.secQtn2, e.ans2, e.storeFwd, e.fwdUrl, e.noOfEmpl, e.entSqft, e.entAssetCount, e.dcSqft, e.dcAssetCount, e.dcUsePctg, e.dcTemp, e.regCmplt, e.comments, e.creDttm, (select count(em.meterId) from EnterpriseMeter em where e.enterpriseId = em.enterpriseId), (select g.expDttm from GateKeeper g where e.enterpriseId = g.enterpriseId) from Enterprise e";
            Query query = session.createQuery(hql);
            List<Object[]> summaryResult = query.list();
            System.out.println(summaryResult);

            List<EntpSummary> entpSummaryList = new ArrayList<EntpSummary>();

            for (Object[] list : summaryResult) {
                EntpSummary entpSummary = new EntpSummary();
                entpSummary.setSid((Short) list[0]);
                entpSummary.setEnterpriseId((String) list[1]);
                entpSummary.setBlCd((String) list[2].toString());
                entpSummary.seteName((String) list[3]);
                entpSummary.setPhone((String) list[4]);
                entpSummary.setEmail((String) list[5]);
                entpSummary.setUserId((String) list[6]);
                entpSummary.setPasswd((String) list[7]);
                entpSummary.setSecQtn1((Short) list[8]);
                entpSummary.setAns1((String) list[9]);
                entpSummary.setSecQtn2((Short) list[10]);
                entpSummary.setAns2((String) list[11]);
                entpSummary.setStoreFwd((Character) list[12]);
                entpSummary.setFwdUrl((String) list[13]);
                entpSummary.setNoOfEmpl((Integer) list[14]);
                entpSummary.setEntSqft((Integer) list[15]);
                entpSummary.setEntAssetCount((Integer) list[16]);
                entpSummary.setDcSqft((Integer) list[17]);
                entpSummary.setDcAssetCount((Integer) list[18]);
                entpSummary.setDcUsePctg((Integer) list[19]);
                entpSummary.setDcTemp((Integer) list[20]);
                entpSummary.setRegCmplt((Character) list[21]);
                entpSummary.setComments((String) list[22]);
                entpSummary.setCreDttm((Date) list[23]);

                entpSummary.setmCount((Long) list[24]);

                entpSummary.setExpDttm((Date) list[25]);

                entpSummaryList.add(setValues(entpSummary));
            }

            return entpSummaryList;
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

    public EntpSummary setValues(EntpSummary entp) {
        if (entp.getDcAssetCount() == null) entp.setDcAssetCount(0);
        if (entp.getDcSqft() == null) entp.setDcSqft(0);
        if (entp.getDcTemp() == null) entp.setDcTemp(0);
        if (entp.getFwdUrl() == null) entp.setFwdUrl(" ");
        return entp;

    }
}
