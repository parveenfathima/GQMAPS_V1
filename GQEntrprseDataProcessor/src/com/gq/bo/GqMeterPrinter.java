package com.gq.bo;

import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Printer;
import com.gq.meter.object.PrinterConnDevice;
import com.gq.meter.object.PrinterSnapshot;

import com.gq.util.GQEDPConstants;

/**
 * @author yogalakshmi
 * @change	parveen
 */


public class GqMeterPrinter {

    public void insertData(Printer printer, GQMeterResponse gqmResponse, Long runId, Session session) {

        String meterId = gqmResponse.getGqmid();

        try {
            CPNId cid = printer.getId();
            cid.setRunId(runId);

            // inserting asset
            Asset assetObj = printer.getAssetObj();
            if (assetObj.getAssetId() != " " && assetObj.getAssetId() != null
                    && !assetObj.getAssetId().equalsIgnoreCase("P-null")) {
            	GQEDPConstants.logger.info(" Session  started for Printer asset " + assetObj.getAssetId());
                session.saveOrUpdate(assetObj);

                // snapshot
                PrinterSnapshot printerSnapshot = printer.getPrinterSnapShot();
                if (printerSnapshot != null) {
                    try {
                        printerSnapshot.setId(cid);
                        session.save(printerSnapshot);
                        GQEDPConstants.logger.debug(meterId + " Data successfully saved in the Printer Snapshot table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger.error(meterId + " Data failed to save in the Printer Snapshot table ", e);
                    }
                }

                // connected device
                if (printer.getPrinterConnectedDevice() != null) {
                    try {
                        for (PrinterConnDevice printerConnDevice : printer.getPrinterConnectedDevice()) {
                            printerConnDevice.getId().setRunId(runId);
                            session.merge(printerConnDevice);
                           }
                        GQEDPConstants.logger.debug(meterId + " Data successfully saved in the Printer Connected Device table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger.error(meterId
                                + " Data failed to save in the Printer Connected devices table  ", e);
                    }
                }
                // Actual insertion will happen at this step
                session.flush();
            }
        }// try ends
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }// finally ends
    }// method ends
}// class ends
