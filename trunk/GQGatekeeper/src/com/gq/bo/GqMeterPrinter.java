package com.gq.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.MeterRun;
import com.gq.meter.object.Printer;
import com.gq.meter.object.PrinterConnDevice;
import com.gq.meter.object.PrinterSnapshot;
import com.gq.util.HibernateUtil;

public class GqMeterPrinter {

    public static void insertData(Printer printer, GQMeterResponse gqmResponse) {
        Session session = null;

        try {
            Date recordDT = gqmResponse.getRecDttm();
            short scanned = gqmResponse.getAssetScanned();
            System.out.println();
            short discovered = gqmResponse.getAssetDiscovered();
            long runTimeMs = gqmResponse.getRunTimeMiliSeconds();

            // This step will read hibernate.cfg.xml and prepare hibernate for use
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            CPNId cid = printer.getId();
            int runId = cid.getRunId();
            String assetId = cid.getAssetId();

            // inserting asset
            String hql = "FROM Asset WHERE assetId = :ASSET_ID";
            Query query = session.createQuery(hql);
            query.setParameter("ASSET_ID", assetId);
            List<?> result = query.list();

            if (result.size() == 0) {
                try {
                    Asset assetObj = printer.getAssetObj();
                    session.save(assetObj);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // inserting runid
            MeterRun meterRun = new MeterRun(runId, "meter_id", recordDT, scanned, discovered, runTimeMs);

            try {
                session.save(meterRun);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // snapshot
            PrinterSnapshot printerSnapshot = printer.getPrinterSnapShot();
            try {
                session.save(printerSnapshot);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // connected device
            if (printer.getPrinterConnectedDevice() != null) {
                HashSet<PrinterConnDevice> printerConnectedDevice = printer.getPrinterConnectedDevice();
                for (PrinterConnDevice printerConnDevice : printerConnectedDevice) {

                    try {
                        session.merge(printerConnDevice);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

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
