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
 * @author parveen
 */

public class GqMeterPrinter {

    public  void insertData(Printer printer, GQMeterResponse gqmResponse, Long runId , Session session) {

        String meterId = gqmResponse.getGqmid();

        try {
            GQEDPConstants.logger.info("Session Successfully started for Printer");

            CPNId cid = printer.getId();
            cid.setRunId(runId);

            // inserting asset
            try {
                Asset assetObj = printer.getAssetObj();
                session.saveOrUpdate(assetObj);
            }
            catch (Exception e) {
                GQEDPConstants.logger.error(meterId + " Printer Data failed to save in the Asset table ", e);
            }

            // snapshot
            PrinterSnapshot printerSnapshot = printer.getPrinterSnapShot();
            if (printerSnapshot != null) {
	            try {
	                printerSnapshot.setId(cid);
	                session.save(printerSnapshot);
	                GQEDPConstants.logger.info(meterId + " Data successfully saved in the Printer Snapshot table ");
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
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId
                            + " Data failed to save in the Printer Connected devices table  ", e);
                }
            }
            // Actual insertion will happen at this step
            session.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }// finally ends
        
    }// method ends
    
}// class ends
