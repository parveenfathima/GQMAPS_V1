package com.gq.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

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

    public static void insertData(Computer computer, GQMeterResponse gqmResponse, int runId) {

        GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                + " Data is ready to save in the computer asset ");

        Session session = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = computer.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);
            computer.setId(cid);

            // inserting asset
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();

            if (result.size() == 0) {
                try {
                    Asset assetObj = computer.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Computer Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Computer Data failed to save in the Asset table " + e.getMessage());
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
                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Computer Data successfully saved in the OS table ");
                }

            }
            catch (Exception e) {
                GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Computer Data failed to save in the OS table " + e.getMessage());
            }

            // snapshot
            CompSnapshot compSnapshot = computer.getSnapShot();
            compSnapshot.setId(cid);
            try {
                session.save(compSnapshot);
                GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Data successfully saved in the Computer Snapshot table ");
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                        + " Data failed to save in the Computer Snapshot table " + e.getMessage());
            }

            // computer installed software
            if (computer.getCompInstSwList() != null) {
                ArrayList<CompInstSoftware> compInstSoftwareList = computer.getCompInstSwList();

                try {

                    for (CompInstSoftware compInsSoftware : compInstSoftwareList) {
                        compInsSoftware.getId().setRunId(runId);
                        session.merge(compInsSoftware);
                    }
                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data successfully saved in the Computer Installed software table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data failed to save in the Computer Installed software table " + e.getMessage());
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

                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data successfully saved in the Computer process table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data failed to save in the Computer process table " + e.getMessage());
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

                    GQEDPConstants.logger.info(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data successfully saved in the Computer Connected devices table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(GqEDPFilter.enterpriseId + "-" + GqEDPFilter.meterId
                            + " Data failed to save in the Computer Connected devices table " + e.getMessage());
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
