package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.MapsDomainBean;
import com.gq.meter.object.DevCtlg;
import com.gq.meter.object.SrvrAppType;
import com.gq.meter.object.CompType;
import com.gq.meter.object.AssetImp;
import com.gq.meter.util.CustomerServiceConstant;
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
            CustomerServiceConstant.logger
                    .info("[MAPSDOMAINMODEL]  MapsDomain is Sucessfully Executed the DeviceCatalogue");
            String srvrAppType = "FROM SrvrAppType";
            Query srvrAppTypeQuery = session.createQuery(srvrAppType);
            List<SrvrAppType> srvrAppTypeResult = srvrAppTypeQuery.list();
            CustomerServiceConstant.logger
                    .info("[MAPSDOMAINMODEL]  MapsDomain is Sucessfully Executed the Server AppType");
            String compType = "FROM CompType";
            Query compTypeQuery = session.createQuery(compType);
            List<CompType> compTypeResult = compTypeQuery.list();
            CustomerServiceConstant.logger.info("[MAPSDOMAINMODEL]  MapsDomain is Sucessfully Executed the Comp Type");
            String assetImp = "FROM AssetImp";
            Query assetImpQuery = session.createQuery(assetImp);
            List<AssetImp> assetImpResult = assetImpQuery.list();
            CustomerServiceConstant.logger.info("[MAPSDOMAINMODEL]  MapsDomain is Sucessfully Executed the Asset Impl");
            mapsDomainServices = new MapsDomainBean(devCtlgResult, srvrAppTypeResult, compTypeResult, assetImpResult);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(
                    "[MAPSDOMAINMODEL]  Exception occured while fetching the CustomerServiceDetails ", e);
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
        CustomerServiceConstant.logger.info("[MAPSDOMAINMODEL]  MapsDomain is Sucessfully Executed");
        return mapsDomainServices;
    }
}
