package com.gq.meter.model;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.MapsDomainBean;
import com.gq.meter.object.DevCtlg;
import com.gq.meter.object.SrvrAppType;
import com.gq.meter.object.CompType;
import com.gq.meter.object.AssetImp;
import com.gq.meter.util.MapsDomainServiceConstant;
import com.gq.meter.util.HibernateUtil;

public class MapsDomainModel {

    public MapsDomainBean getAllMapsDomainDetails() {
        Session session = null;
        MapsDomainBean mapsDomainServices = null;
        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String devCtlg = "FROM DevCtlg";
            Query devCtlgQuery = session.createQuery(devCtlg);
            List<DevCtlg> devCtlgResult = devCtlgQuery.list();

            String srvrAppType = "FROM SrvrAppType";
            Query srvrAppTypeQuery = session.createQuery(srvrAppType);
            List<SrvrAppType> srvrAppTypeResult = srvrAppTypeQuery.list();

            String compType = "FROM CompType";
            Query compTypeQuery = session.createQuery(compType);
            List<CompType> compTypeResult = compTypeQuery.list();

            String assetImp = "FROM AssetImp";
            Query assetImpQuery = session.createQuery(assetImp);
            List<AssetImp> assetImpResult = assetImpQuery.list();

            mapsDomainServices = new MapsDomainBean(devCtlgResult, srvrAppTypeResult, compTypeResult, assetImpResult);

        }
        catch (Exception e) {
            MapsDomainServiceConstant.logger.error("Exception occured while fetching the CustomerServiceDetails ", e);
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
