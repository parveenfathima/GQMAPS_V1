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
import com.gq.util.HibernateUtil;

public class GqMeterComputer {

    public static void insertData(Computer computer, GQMeterResponse gqmResponse, int runId) {
        Session session = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = computer.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);
            computer.setId(cid);

            // inserting assert
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();

            if (result.size() == 0) {
                try {
                    Asset assetObj = computer.getAssetObj();
                    session.save(assetObj);
                }
                catch (Exception e) {
                    e.printStackTrace();
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
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // snapshot
            CompSnapshot compSnapshot = computer.getSnapShot();
            compSnapshot.setId(cid);
            try {
                session.save(compSnapshot);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // computer installed software
            if (computer.getCompInstSwList() != null) {
                ArrayList<CompInstSoftware> compInstSoftwareList = computer.getCompInstSwList();

                for (CompInstSoftware compInsSoftware : compInstSoftwareList) {

                    try {
                        compInsSoftware.getId().setRunId(runId);
                        session.merge(compInsSoftware);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            // computer process list
            if (computer.getCompProcList() != null) {
                ArrayList<CompProcess> compProcessList = computer.getCompProcList();
                for (CompProcess compProcess : compProcessList) {

                    try {
                        compProcess.getId().setRunId(runId);
                        session.merge(compProcess);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            // connected device
            if (computer.getCompConnDeviceSet() != null) {
                HashSet<CompConnDevice> compConnnectedDevice = computer.getCompConnDeviceSet();
                for (CompConnDevice compConnDevice : compConnnectedDevice) {

                    compConnDevice.getId().setRunId(runId);
                    try {
                        session.merge(compConnDevice);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }// for ends

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
