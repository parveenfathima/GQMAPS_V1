/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.Asset;
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
            CustomerServiceConstant.logger.info("Number of asset Id in asset from database" + assetresult);
            return assetresult;
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error("Exception occured while getting the asset count", e);

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
        return null;
    }
}
