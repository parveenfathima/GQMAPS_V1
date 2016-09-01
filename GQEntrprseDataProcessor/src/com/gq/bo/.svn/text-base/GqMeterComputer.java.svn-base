package com.gq.bo;

import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.CompConnDevice;
import com.gq.meter.object.CompInstSoftware;
import com.gq.meter.object.CompProcess;
import com.gq.meter.object.CompSnapshot;
import com.gq.meter.object.Computer;

import com.gq.util.GQEDPConstants;

/**
 * @author yogalakshmi
 * @change	parveen
 */
// This class takes care of inserting computer switch data's to database
public class GqMeterComputer {

    public void insertData(Computer computer, GQMeterResponse gqmResponse, Long runId, Session session) {

        String meterId = gqmResponse.getGqmid();

        try {
            // This step will read hibernate.cfg.xml and prepare hibernate for use

            CPNId cid = computer.getId();
            cid.setRunId(runId);
            computer.setId(cid);

            // inserting asset

            Asset assetObj = computer.getAssetObj();
            if (assetObj.getAssetId() != " " && assetObj.getAssetId() != null
                    && !assetObj.getAssetId().equalsIgnoreCase("C-null")) {
                GQEDPConstants.logger.info(" Session successfully started for GQMeterComputer asset "
                        + assetObj.getAssetId());
                session.saveOrUpdate(assetObj);

                // snapshot
                CompSnapshot compSnapshot = computer.getSnapShot();
                if (compSnapshot != null) {
                    compSnapshot.setId(cid);
                    try {
                        session.save(compSnapshot);
                        GQEDPConstants.logger
                                .debug(meterId + " Data successfully saved in the Computer Snapshot table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger
                                .error(meterId + " Data failed to save in the Computer Snapshot table ", e);
                    }
                }
                // computer installed software
                if (computer.getCompInstSwList() != null) {
                    try {
                        for (CompInstSoftware compInsSoftware : computer.getCompInstSwList()) {
                            compInsSoftware.getId().setRunId(runId);
                            session.merge(compInsSoftware);
                          }
                        GQEDPConstants.logger
                        .debug(meterId + " Data successfully saved in the Computer Installed Software table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger.error(meterId
                                + " Data failed to save in the Computer Installed software table ", e);
                    }
                }

                // computer process list
                if (computer.getCompProcList() != null) {
                    try {
                        for (CompProcess compProcess : computer.getCompProcList()) {
                            compProcess.getId().setRunId(runId);
                            session.merge(compProcess);
                        }
                        GQEDPConstants.logger.debug(meterId + " Data successfully to save in the Computer process table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger.error(meterId + " Data failed to save in the Computer process table ", e);
                    }
                }

                // connected device
                if (computer.getCompConnDeviceSet() != null) {
                    try {
                        for (CompConnDevice compConnDevice : computer.getCompConnDeviceSet()) {
                            compConnDevice.getId().setRunId(runId);
                            session.merge(compConnDevice);
                        	}// for ends
                        GQEDPConstants.logger.debug(meterId + " Data successfully to save in the Computer Connected Devices table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger.error(meterId
                                + " Data failed to save in the Computer Connected devices table ", e);
                    }
                }
                // Actual insertion will happen at this step and no need to flush or clear or close since
                // getcurrentsession
                // is designed to handle all of these. - ss , oct 4 , 13
                session.flush();
            }
        }// try ends
        catch (Exception e) {
            e.printStackTrace();
            GQEDPConstants.logger.error(meterId + "computer meter edp Exception ", e);
        }
        finally {
        }// finally ends

    }// method ends
}// class ends
