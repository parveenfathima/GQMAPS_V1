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
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.HibernateUtil;

public class MapsDomainModel {

    public MapsDomainBean getAllMapsDomainDetails(String enterpriseId) {
        Session session = null;
        MapsDomainBean mapsDomainServices = null;
        SessionFactory sessionFactory = null;
        try {
            System.out.println("Enterprise Id:" + enterpriseId);
            String dbInstanceName = "gqm" + enterpriseId;
            String url = "jdbc:mysql://192.168.1.95:3306/" + dbInstanceName + "?autoReconnect=true";
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
            CustomerServiceConstant.logger.error(
                    " [MapsDomainModel]  Exception occured while fetching the CustomerServiceDetails ", e);
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
        return mapsDomainServices;
    }
}
