package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.AllCustomerServices;
import com.gq.meter.object.DevCtlg;
import com.gq.meter.object.LocationMaster;
import com.gq.meter.object.OsType;
import com.gq.meter.object.Protocol;
import com.gq.meter.object.PwrSlab;
import com.gq.meter.object.Rcmndtn;
import com.gq.meter.object.SrvrAppType;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.HibernateUtil;

public class CustomerServiceModel {

    // public static void main(String[] args) {
    // getAllCustomerServiceDetails();
    // }

    public AllCustomerServices getAllCustomerServiceDetails() {
        Session session = null;
        AllCustomerServices allCustomerServices = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String locMastr = "FROM LocationMaster";
            Query locMastrQuery = session.createQuery(locMastr);
            List<LocationMaster> locationMasterResult = locMastrQuery.list();
            // CustomerServiceConstant.logger.info("locationMasterResult : " + locationMasterResult);

            String osType = "FROM OsType";
            Query osTypeQuery = session.createQuery(osType);
            List<OsType> ostypeResult = osTypeQuery.list();
            // CustomerServiceConstant.logger.info("ostypeResult : " + ostypeResult);

            String pwrSlab = "FROM PwrSlab";
            Query pwrSlabQuery = session.createQuery(pwrSlab);
            List<PwrSlab> pwrSlabResult = pwrSlabQuery.list();
            // CustomerServiceConstant.logger.info("pwrSlabResult : " + pwrSlabResult);

            String protocol = "FROM Protocol";
            Query protocolQuery = session.createQuery(protocol);
            List<Protocol> protocolResult = protocolQuery.list();
            // CustomerServiceConstant.logger.info("protocolResult : " + protocolResult);

            String rcmndtn = "FROM Rcmndtn";
            Query rcmndtnQuery = session.createQuery(rcmndtn);
            List<Rcmndtn> rcmndtnResult = rcmndtnQuery.list();
            // CustomerServiceConstant.logger.info("rcmndtnResult : " + rcmndtnResult);

            String srvrAppType = "FROM SrvrAppType";
            Query srvrAppTypeQuery = session.createQuery(srvrAppType);
            List<SrvrAppType> srvrAppTypeResult = srvrAppTypeQuery.list();
            // CustomerServiceConstant.logger.info("srvrAppTypeResult : " + srvrAppTypeResult);

            String deviceCatalog = "FROM DeviceCatalog";
            Query deviceCatalogQuery = session.createQuery(deviceCatalog);
            List<DevCtlg> deviceCatalogResult = deviceCatalogQuery.list();
            // CustomerServiceConstant.logger.info("deviceCatalogResult : " + deviceCatalogResult);

            allCustomerServices = new AllCustomerServices(locationMasterResult, ostypeResult, pwrSlabResult,
                    protocolResult, rcmndtnResult, srvrAppTypeResult, deviceCatalogResult);

        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error("Exception occured while fetching the CustomerServiceDetails ", e);
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
        return allCustomerServices;
    }
}
