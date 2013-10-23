package com.gq.meter.model;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gq.meter.util.DynamicSessionUtil;
import com.gq.meter.util.CustomerServiceConstant;

import com.gq.meter.object.Asset;
import com.gq.meter.object.GetAsset;

import java.util.Date;
import java.util.List;

/**
 * @author parveen
 * 
 */

// This class is used to getting and updating asset(computer,printer,nsrg,storage) data's.
public class AssetEditModel {

    Session session = null;
    GetAsset getAssetServices = null;
    SessionFactory sessionFactory = null;

    // Getting asset details
    public GetAsset getAssetDetails(String enterpriseId) {
        Transaction tx = null;

        try {
        	CustomerServiceConstant.logger.debug("Enterprise Id:" + enterpriseId);
            String dbInstanceName = "gqm" + enterpriseId;

            // Create a session factory for requesting enterprise
            sessionFactory = DynamicSessionUtil.getSessionFactory(dbInstanceName);

            // create a session to start a transaction
            session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();

            String asset = "FROM Asset";
            Query assetQuery = session.createQuery(asset);
            List<Asset> assetResult = (List<Asset>)assetQuery.list();
            getAssetServices = new GetAsset(assetResult);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error("Exception occured while fetching the CustomerServiceDetails ", e);
        }
        finally {
            try {
            	// commit the unit of work , meters will have flush only
                tx.commit();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        // To return the asset service
        return getAssetServices;
    }

    // updating assets
    public void updateAssets(String enterpriseId, Asset assetObject) throws Exception {
        Date currentDate = null;
        Transaction tx = null;

        try {
            String dbInstanceName = "gqm" + enterpriseId;

            // Create a session factory for requesting enterprise
            sessionFactory = DynamicSessionUtil.getSessionFactory(dbInstanceName);

            // create a session to start a transaction
            session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();

            Asset oldAssetObject = (Asset) session.load(Asset.class, assetObject.getAssetId());
            oldAssetObject.setIpAddr(assetObject.getIpAddr());
            oldAssetObject.setAssetId(assetObject.getAssetId());
            oldAssetObject.setCtlgId(assetObject.getCtlgId());
            oldAssetObject.setSrvrAppId(assetObject.getSrvrAppId());
            oldAssetObject.setAssetUsg(assetObject.getAssetUsg());
            oldAssetObject.setImpLvl(assetObject.getImpLvl());
            oldAssetObject.setOwnership(assetObject.getOwnership());
            oldAssetObject.setDcEnt(assetObject.getDcEnt());
            oldAssetObject.setActive(assetObject.getActive());
            // If active value 'n' means set inactivated date and time,'y' means set null value.
            if (assetObject.getActive() == 'n') {
                oldAssetObject.setInactiveDttm(new Date());
            }
            else {
                oldAssetObject.setInactiveDttm(currentDate);
            }
            oldAssetObject.setTypeId(assetObject.getTypeId());
            CustomerServiceConstant.logger.debug("assset Object updated Successfully");
            session.flush();
            
        }
        catch (Exception e) {
        	CustomerServiceConstant.logger.debug(e);
            CustomerServiceConstant.logger.error("Exception occured while Updating the Asset ", e);
            throw e;
        }
        finally {
            tx.commit();
        }
    }
}
