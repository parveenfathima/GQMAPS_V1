package com.gq.meter.model;

import org.hibernate.Session;

import com.gq.meter.util.HibernateUtil;
import com.gq.meter.util.UpdateAssetServiceConstant;
import com.gq.meter.object.Asset;

public class UpdateAssetModel {
    public void updateAssets(Asset assetObject) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            System.out.println("Started");
            System.out.println(assetObject.getAssetId());
            Asset oldAssetObject = (Asset) session.load(Asset.class, assetObject.getAssetId());

            oldAssetObject.setAssetId(assetObject.getAssetId());
            // oldAssetObject.setProtocolId(assetObject.getProtocolId());
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
            UpdateAssetServiceConstant.logger.error("Exception occured while Updating the Asset ", e);
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
