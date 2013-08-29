/**
 * 
 */
package com.gq.meter.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.Asset;
import com.gq.meter.object.ProtocolCount;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Rathish
 * 
 */
public class AssetModel {

    public List<Asset> getAssetCount() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "select count(assetId) FROM Asset";
            Query query = session.createQuery(hql);
            List<Asset> assetresult = query.list();
            CustomerServiceConstant.logger.info("[ASSETMODEL]   Asset Count sucessfully Executed");
            return assetresult;
        }
        catch (Exception e) {
            CustomerServiceConstant.logger
                    .error("[ASSETLOADMODEL]  Exception occured while getting the asset count", e);
            return null;
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<ProtocolCount> getEntpProtocolCount() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "select protocolId, count(protocolId) FROM Asset group by protocolId";
            Query query = session.createQuery(hql);
            // List<ProtocolCount> assetResult = query.list();

            List<Object[]> queryresult = query.list();
            CustomerServiceConstant.logger
                    .info("[ASSETMODEL]  Getting the ProtocolCount and ProtocolId is sucessfully Executed");
            List<ProtocolCount> protocolresult = new ArrayList<ProtocolCount>();
            for (Object[] list : queryresult) {
                ProtocolCount assetlist = new ProtocolCount();
                assetlist.setProtocolId((String) list[0]);
                assetlist.setPcount((Long) list[1]);
                protocolresult.add(assetlist);
            }
            CustomerServiceConstant.logger.info("[ASSETMODEL]   Protocol Count is sucessfully returned");
            return protocolresult;
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(
                    "[ASSETLOADMODEL]  Exception occured while getting the protocol count", e);
            return null;
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
