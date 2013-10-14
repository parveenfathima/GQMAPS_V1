package com.gq.bo;

import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.NSRGConnDevice;
import com.gq.meter.object.NSRGSnapshot;

import com.gq.util.GQEDPConstants;

/**
 * @author parveen
 */

public class GqMeterNSRG {

    public  void insertData(NSRG nsrg, GQMeterResponse gqmResponse, Long runId , Session session) {

        String meterId = gqmResponse.getGqmid();

        try {
            CPNId cid = nsrg.getId();
            cid.setRunId(runId);

            // inserting asset
            Asset assetObj = nsrg.getAssetObj();
            GQEDPConstants.logger.info(" Session  started for NSRG asset " + assetObj.getAssetId());
            session.saveOrUpdate(assetObj);

            // snapshot
            NSRGSnapshot nsrgSnapshot = nsrg.getNsrgSnapShot();
            if (nsrgSnapshot != null) {
	            try {
	                nsrgSnapshot.setId(cid);
	                session.save(nsrgSnapshot);
	            }
	            catch (Exception e) {
	                GQEDPConstants.logger.error(meterId + " Data failed to save in the NSRG Snapshot table ", e);
	            }
            }
            // connected device
            if (nsrg.getNsrgConnectedDevices() != null) {
                try {
                    for (NSRGConnDevice nsrgConnDevice : nsrg.getNsrgConnectedDevices()) {
                        nsrgConnDevice.getId().setRunId(runId);
                        session.merge(nsrgConnDevice);
                    }// for ends
                }
                catch (Exception e) {
                    GQEDPConstants.logger.error(meterId + " Data failed to save in the NSRG Connected devices table ", e);
                }
            }
            // Actual insertion will happen at this step
            session.flush();
        }
        catch (Exception e) {
            GQEDPConstants.logger.error(meterId + " Data failed to save in the NSRG exception ", e);
            e.printStackTrace();
        }
        finally {
        
        }// finally ends
    }// method ends
    
}
