package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.GetAsset;
import com.gq.meter.object.Asset;
import com.gq.meter.util.HibernateUtil;
import com.gq.meter.util.GetAssetServiceConstant;

public class GetAssetModel {
    public GetAsset getAssetDetails() {
        Session session = null;
        GetAsset getAssetServices = null;
        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String asset = "FROM Asset";
            Query assetQuery = session.createQuery(asset);
            List<Asset> assetResult = assetQuery.list();

            getAssetServices = new GetAsset(assetResult);

        }
        catch (Exception e) {
            GetAssetServiceConstant.logger.error("Exception occured while fetching the CustomerServiceDetails ", e);
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
        return getAssetServices;
    }
}
