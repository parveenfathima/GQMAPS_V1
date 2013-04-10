package com.gq.bo;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Printer;
import com.gq.meter.object.PrinterConnDevice;
import com.gq.meter.object.PrinterSnapshot;
import com.gq.util.GQEDPConstants;
import com.gq.util.HibernateUtil;

public class GqMeterPrinter {

    public static void insertData(Printer printer, GQMeterResponse gqmResponse, int runId) {
        String meterId = gqmResponse.getGqmid();

        Session session = null;

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = printer.getId();
            String assetId = cid.getAssetId();

            cid.setRunId(runId);

            // System.out.println("PRINTER : runnid set into the cpn id : " + cid.getRunId());

            // inserting asset
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();

            if (result.size() == 0) {
                try {
                    Asset assetObj = printer.getAssetObj();
                    session.save(assetObj);
                    GQEDPConstants.logger.info(meterId + " Printer Data successfully saved in the Asset table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Printer Data failed to save in the Asset table ", e);
                }
            }

            // snapshot
            PrinterSnapshot printerSnapshot = printer.getPrinterSnapShot();
            try {
                // System.out.println("PRINTER : snap shot cpn id has been set");
                printerSnapshot.setId(cid);
                session.save(printerSnapshot);
                GQEDPConstants.logger.info(meterId + " Data successfully saved in the Printer Snapshot table ");
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(meterId + " Data failed to save in the Printer Snapshot table ", e);
            }

            // connected device
            if (printer.getPrinterConnectedDevice() != null) {
                HashSet<PrinterConnDevice> printerConnectedDevice = printer.getPrinterConnectedDevice();

                try {
                    for (PrinterConnDevice printerConnDevice : printerConnectedDevice) {
                        printerConnDevice.getId().setRunId(runId);
                        session.merge(printerConnDevice);
                    }

                    GQEDPConstants.logger.info(meterId
                            + " Data successfully saved in the Printer Connected devices table ");
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId
                            + " Data failed to save in the Printer Connected devices table  ", e);
                }

            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Actual contact insertion will happen at this step
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
