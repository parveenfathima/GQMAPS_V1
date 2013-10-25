/**
 * 
 */
package com.gq.meter.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.AssetLoad;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Rathish
 * 
 */
public class AssetLoadModel {

    public List<AssetLoad> mostConsumedAssets() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql1 = "SELECT value From SysProfile where keyy = 'COMPUTE_DURATION'";
            Query query1 = session.createQuery(hql1);
            CustomerServiceConstant.logger.info(" AssetLoad is executing the query");
            Collection<? extends String> keyresult = query1.list();
            // converting list to string
            ArrayList<String> list1 = new ArrayList<String>();
            list1.addAll(keyresult);
            String listString = "";
            for (String s : list1) {
                listString += s + "\t";
            }
            String val = listString;
            String hql = "SELECT c.id.assetId, a.name, AVG(c.cpuLoad) FROM CompSnpsht c, Asset a , MeterRun m WHERE c.id.assetId = a.assetId AND m.recDttm BETWEEN SUBDATE(curdate(), '"
                    + val
                    + "') AND curdate() AND m.runId = c.id.runId GROUP BY c.id.assetId ORDER BY AVG(c.cpuLoad) DESC LIMIT 5";
            Query query = session.createQuery(hql);
            CustomerServiceConstant.logger.info(" keyresult value" + keyresult);
            // query.setParameter("NAME", keyresult.get(0).getKeyy());
            List<Object[]> assetResult = query.list();
            CustomerServiceConstant.logger.info(" Maximum AssetLoad");
            List<AssetLoad> assetLoad = new ArrayList<AssetLoad>();

            for (Object[] list : assetResult) {
                AssetLoad asstLoad = new AssetLoad((String) list[0], (String) list[1], (Double) list[2]);
                assetLoad.add(asstLoad);
            }
            List finalAssetLoad = new ArrayList();
            int len = assetLoad.size();
            if (len > 4) {
                for (int i = 0; i < 5; i++) {
                    AssetLoad a = new AssetLoad();
                    a.setAssetId(((AssetLoad) assetLoad.get(i)).getAssetId());
                    a.setName(((AssetLoad) assetLoad.get(i)).getName());
                    a.setLoadAvg(((AssetLoad) assetLoad.get(i)).getLoadAvg());
                    finalAssetLoad.add(a);
                }
                CustomerServiceConstant.logger.info(" AssetLoad For Top 5 Assets is more than 5");
                return finalAssetLoad;
            }
            else {
                CustomerServiceConstant.logger.info(" AssetLoad For Top 5 Assets is less than 5");
                return assetLoad;
            }
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(" Exception occured while getting the Maximum Asset Load", e);
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

    public List<AssetLoad> leastConsumedAssets() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql1 = "SELECT value From SysProfile where keyy = 'COMPUTE_DURATION'";
            Query query1 = session.createQuery(hql1);
            Collection<? extends String> keyresult = query1.list();

            // converting list to string
            ArrayList<String> list1 = new ArrayList<String>();
            list1.addAll(keyresult);
            String listString = "";
            for (String s : list1) {
                listString += s + "\t";
            }
            String val = listString;
            String hql = "SELECT c.id.assetId, a.name, AVG(c.cpuLoad) FROM CompSnpsht c, Asset a , MeterRun m WHERE c.id.assetId = a.assetId AND m.recDttm BETWEEN SUBDATE(curdate(), '"
                    + val
                    + "') AND curdate() AND m.runId = c.id.runId GROUP BY c.id.assetId ORDER BY AVG(c.cpuLoad) ASC LIMIT 5";
            Query query = session.createQuery(hql);

            // query.setParameter("NAME", keyresult.get(0).getKeyy());
            List<Object[]> assetResult = query.list();
            CustomerServiceConstant.logger.info(" AssetLoad For Least 5 Assets is Sucessfully Executed");
            List<AssetLoad> assetLoad = new ArrayList<AssetLoad>();
            for (Object[] list : assetResult) {
                AssetLoad asstLoad = new AssetLoad((String) list[0], (String) list[1], (Double) list[2]);
                assetLoad.add(asstLoad);
            }
            List finalAssetLoad = new ArrayList();
            int len = assetLoad.size();
            if (len > 4) {
                for (int i = 0; i < 5; i++) {
                    AssetLoad a = new AssetLoad();
                    a.setAssetId(((AssetLoad) assetLoad.get(i)).getAssetId());
                    a.setName(((AssetLoad) assetLoad.get(i)).getName());
                    a.setLoadAvg(((AssetLoad) assetLoad.get(i)).getLoadAvg());
                    finalAssetLoad.add(a);
                }
                CustomerServiceConstant.logger.info(" AssetLoad For Least 5 Assets is more than 5");
                return finalAssetLoad;
            }
            else {
                CustomerServiceConstant.logger.info(" AssetLoad For Top 5 Assets is less than 5");
                return assetLoad;
            }
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(" Exception occured while getting the Maximum Asset Load", e);
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
