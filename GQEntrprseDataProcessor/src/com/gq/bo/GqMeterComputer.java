package com.gq.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.CompConnDevice;
import com.gq.meter.object.CompInstSoftware;
import com.gq.meter.object.CompProcess;
import com.gq.meter.object.CompSnapshot;
import com.gq.meter.object.Computer;
import com.gq.meter.object.OsType;

import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterComputer {

    public static void insertData(Computer computer, GQMeterResponse gqmResponse, Long runId) {

        String meterId = gqmResponse.getGqmid();
        String enterpriseId = meterId.split("_")[0];
        Session session = null;
        SessionFactory sessionFactory = null;
        try {
            GQEDPConstants.logger.debug("Start a process to read a HIBERNATE xml file in GQMeterComputer ");
            String dbInstanceName = "gqm" + enterpriseId;
            String url = "jdbc:mysql://localhost:3306/" + dbInstanceName + "?autoReconnect=true";
            // This step will read hibernate.cfg.xml and prepare hibernate for use
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
            GQEDPConstants.logger.info(" Session successfully started for GQMeterComputer");

            CPNId cid = computer.getId();
            String assetId = cid.getAssetId();
            cid.setRunId(runId);
            computer.setId(cid);
            // inserting asset
            GQEDPConstants.logger.debug("Create a Query to inserting a values to asset table in GQMeterComputer");
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();
            GQEDPConstants.logger.debug("Asset Id:" + assetId + "\nResult Size:" + result.size());
            GQEDPConstants.logger.debug("To check a condition for Asset Query Result");
            if (result.size() == 0) {
                try {
                    Asset assetObj = computer.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(meterId + " Computer Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Computer Data failed to save in the Asset table ", e);
                }
            }

            // ostype
            String osname = computer.getOsTypeObj().getOsId();
            String oshql = "FROM OsType WHERE osId = :OS_Id";
            Query osQuery = session.createQuery(oshql);
            osQuery.setParameter("OS_Id", osname);
            List<?> osresult = osQuery.list();
            try {
                if (osresult.size() == 0) {
                    OsType os = computer.getOsTypeObj();
                    session.save(os);
                    GQEDPConstants.logger.info(meterId + " Computer Data successfully saved in the OS table ");
                }
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(meterId + " Computer Data failed to save in the OS table ", e);
            }

            // snapshot
            CompSnapshot compSnapshot = computer.getSnapShot();
            if (compSnapshot.getIpAddr() != null) {
                compSnapshot.setId(cid);
                try {
                    session.save(compSnapshot);
                    GQEDPConstants.logger.info(meterId + " Data successfully saved in the Computer Snapshot table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the Computer Snapshot table ", e);
                }
            }

            // computer installed software
            if (computer.getCompInstSwList() != null) {
                ArrayList<CompInstSoftware> compInstSoftwareList = computer.getCompInstSwList();
                try {
                    for (CompInstSoftware compInsSoftware : compInstSoftwareList) {
                        compInsSoftware.getId().setRunId(runId);
                        session.merge(compInsSoftware);
                    }
                    GQEDPConstants.logger.info(meterId
                            + " Data successfully saved in the Computer Installed software table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId
                            + " Data failed to save in the Computer Installed software table ", e);
                }
            }

            // computer process list
            if (computer.getCompProcList() != null) {
                ArrayList<CompProcess> compProcessList = computer.getCompProcList();
                try {
                    for (CompProcess compProcess : compProcessList) {
                        compProcess.getId().setRunId(runId);
                        session.merge(compProcess);
                    }
                    GQEDPConstants.logger.info(meterId + " Data successfully saved in the Computer process table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the Computer process table ", e);
                }
            }

            // connected device
            if (computer.getCompConnDeviceSet() != null) {
                HashSet<CompConnDevice> compConnnectedDevice = computer.getCompConnDeviceSet();
                try {
                    for (CompConnDevice compConnDevice : compConnnectedDevice) {
                        compConnDevice.getId().setRunId(runId);
                        session.merge(compConnDevice);
                    }// for ends
                    GQEDPConstants.logger.info(meterId
                            + " Data successfully saved in the Computer Connected devices table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId
                            + " Data failed to save in the Computer Connected devices table ", e);
                }
            }
            // Actual insertion will happen at this step
            session.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                    session.clear();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }// finally ends
    }// method ends
}// class ends
