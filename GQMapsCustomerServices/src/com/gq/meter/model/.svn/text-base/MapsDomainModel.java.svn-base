package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.gq.meter.object.MapsDomainBean;
import com.gq.meter.object.DevCtlg;
import com.gq.meter.object.SrvrAppType;
import com.gq.meter.object.CompType;
import com.gq.meter.object.AssetImp;

import com.gq.meter.util.CustomerServiceUtils;
import com.gq.meter.util.DynamicSessionUtil;

/**
 * @author parveen
 * 
 */
// This class is used to getting domain data's for requesting enterpriseId
public class MapsDomainModel {
    // Getting all domain details
    public MapsDomainBean getAllMapsDomainDetails(String enterpriseId) {

        Session session = null;
        MapsDomainBean mapsDomainServices = null;
        SessionFactory sessionFactory = null;

        try {
            CustomerServiceUtils.logger.debug("Enterprise Id:" + enterpriseId);
            String dbInstanceName = "gqm" + enterpriseId;

            // Create a session factory for requesting enterprise
            sessionFactory = DynamicSessionUtil.getSessionFactory(dbInstanceName);

            // create a session to start a transaction
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            // Getting Device Catalog details
            String devCtlg = "FROM DevCtlg";
            Query devCtlgQuery = session.createQuery(devCtlg);
            List<DevCtlg> devCtlgResult = devCtlgQuery.list();

            // Getting ServerApplciation Type
            String srvrAppType = "FROM SrvrAppType";
            Query srvrAppTypeQuery = session.createQuery(srvrAppType);
            List<SrvrAppType> srvrAppTypeResult = srvrAppTypeQuery.list();

            // Getting Computer Type
            String compType = "FROM CompType";
            Query compTypeQuery = session.createQuery(compType);
            List<CompType> compTypeResult = compTypeQuery.list();

            // Getting Asset Imp Levels
            String assetImp = "FROM AssetImp";
            Query assetImpQuery = session.createQuery(assetImp);
            List<AssetImp> assetImpResult = assetImpQuery.list();

            mapsDomainServices = new MapsDomainBean(devCtlgResult, srvrAppTypeResult, compTypeResult, assetImpResult);
        }
        catch (Exception e) {
            CustomerServiceUtils.logger.error(" Exception occured while fetching the CustomerServiceDetails ", e);
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
        // To return the domain service
        return mapsDomainServices;
    }
}
