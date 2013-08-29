package com.gq.meter.model;

import org.hibernate.Session;
import org.hibernate.Query;

import com.gq.meter.util.HibernateUtil;
import com.gq.meter.util.CustomerServiceConstant;

import com.gq.meter.object.Asset;
import com.gq.meter.object.GetAsset;

import java.util.List;

public class AssetEditModel {

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
            CustomerServiceConstant.logger.error(
                    " [ASSETEDITMODEL]  Exception occured while fetching the CustomerServiceDetails ", e);
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

    public void updateAssets(Asset assetObject) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Asset oldAssetObject = (Asset) session.load(Asset.class, assetObject.getAssetId());
            oldAssetObject.setAssetId(assetObject.getAssetId());
            oldAssetObject.setName(assetObject.getName());
            oldAssetObject.setDescr(assetObject.getDescr());
            oldAssetObject.setIpAddr(assetObject.getIpAddr());
            oldAssetObject.setContact(assetObject.getContact());
            oldAssetObject.setLocation(assetObject.getLocation());
            oldAssetObject.setCtlgId(assetObject.getCtlgId());
            oldAssetObject.setSrvrAppId(assetObject.getSrvrAppId());
            oldAssetObject.setAssetUsg(assetObject.getAssetUsg());
            oldAssetObject.setImpLvl(assetObject.getImpLvl());
            oldAssetObject.setOwnership(assetObject.getOwnership());
            oldAssetObject.setDcEnt(assetObject.getDcEnt());
            oldAssetObject.setActive(assetObject.getActive());
            oldAssetObject.setInactiveDttm(assetObject.getInactiveDttm());
            oldAssetObject.setTypeId(assetObject.getTypeId());
            session.getTransaction().commit();
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(" [ASSETEDITMODEL]  Exception occured while Updating the Asset ", e);
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
