package com.gq.meter.model;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.gq.meter.util.HibernateUtil;
import com.gq.meter.util.CustomerServiceConstant;

import com.gq.meter.object.Asset;
import com.gq.meter.object.GetAsset;

import java.util.Date;
import java.util.List;

public class AssetEditModel {
    Session session = null;
    GetAsset getAssetServices = null;
    SessionFactory sessionFactory = null;

    public GetAsset getAssetDetails(String enterpriseId) {

        try {
            System.out.println("Enterprise Id:" + enterpriseId);
            String dbInstanceName = "gqm" + enterpriseId;
            String url = "jdbc:mysql://localhost:3306/" + dbInstanceName + "?autoReconnect=true";
            if (HibernateUtil.SessionFactoryListMap.containsKey(dbInstanceName)) {
                sessionFactory = HibernateUtil.SessionFactoryListMap.get(dbInstanceName);
                if (sessionFactory == null) {
                    System.out.println("null");
                    sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                    HibernateUtil.SessionFactoryListMap.put(dbInstanceName, sessionFactory);
                }
            }
            else {
                sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                HibernateUtil.SessionFactoryListMap.put(dbInstanceName, sessionFactory);
            }
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            String asset = "FROM Asset";
            Query assetQuery = session.createQuery(asset);
            List<Asset> assetResult = assetQuery.list();
            getAssetServices = new GetAsset(assetResult);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(
                    " [AssetEditModel]  Exception occured while fetching the CustomerServiceDetails ", e);
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

    public void updateAssets(String enterpriseId, Asset assetObject) throws Exception {
        Date currentDate = null;
        try {
            String dbInstanceName = "gqm" + enterpriseId;
            String url = "jdbc:mysql://localhost:3306/" + dbInstanceName + "?autoReconnect=true";
            if (HibernateUtil.SessionFactoryListMap.containsKey(dbInstanceName)) {
                sessionFactory = HibernateUtil.SessionFactoryListMap.get(dbInstanceName);
                if (sessionFactory == null) {
                    sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                    HibernateUtil.SessionFactoryListMap.put(dbInstanceName, sessionFactory);
                }
            }
            else {
                sessionFactory = new HibernateUtil().dynamicSessionFactory(url);
                HibernateUtil.SessionFactoryListMap.put(dbInstanceName, sessionFactory);
            }
            session = sessionFactory.getCurrentSession();
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
            if (assetObject.getActive() == 'n') {
                oldAssetObject.setInactiveDttm(new Date());
            }
            else {
                oldAssetObject.setInactiveDttm(currentDate);
            }
            oldAssetObject.setTypeId(assetObject.getTypeId());
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.out.println(e);
            CustomerServiceConstant.logger.error(" [AssetEditModel]  Exception occured while Updating the Asset ", e);
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