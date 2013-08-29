package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.AllCustomerServices;
import com.gq.meter.object.DevCtlg;
import com.gq.meter.object.Protocol;
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

            String protocol = "FROM Protocol";
            Query protocolQuery = session.createQuery(protocol);
            List<Protocol> protocolResult = protocolQuery.list();
            CustomerServiceConstant.logger.info("[CUSTOMERSERVICEMODEL]  protocolResult : " + protocolResult);

            String srvrAppType = "FROM SrvrAppType";
            Query srvrAppTypeQuery = session.createQuery(srvrAppType);
            List<SrvrAppType> srvrAppTypeResult = srvrAppTypeQuery.list();
            CustomerServiceConstant.logger.info("[CUSTOMERSERVICEMODEL]  srvrAppTypeResult : " + srvrAppTypeResult);

            String deviceCatalog = "FROM DeviceCatalog";
            Query deviceCatalogQuery = session.createQuery(deviceCatalog);
            List<DevCtlg> deviceCatalogResult = deviceCatalogQuery.list();
            CustomerServiceConstant.logger.info("[CUSTOMERSERVICEMODEL]  deviceCatalogResult : " + deviceCatalogResult);

            allCustomerServices = new AllCustomerServices(protocolResult, srvrAppTypeResult, deviceCatalogResult);

        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(
                    "[CUSTOMERSERVICEMODEL]  Exception occured while fetching the CustomerServiceDetails ", e);
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
        CustomerServiceConstant.logger.info("[CUSTOMERSERVICEMODEL]  Sucessfully returned the CusomerDetails");
        return allCustomerServices;
    }
}
