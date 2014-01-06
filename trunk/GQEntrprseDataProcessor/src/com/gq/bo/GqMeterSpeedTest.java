package com.gq.bo;

import org.hibernate.Session;

import com.gq.meter.GQMeterResponse;
import com.gq.meter.object.SpeedTestSnpsht;
import com.gq.util.GQEDPConstants;

public class GqMeterSpeedTest {
	public void insertData(SpeedTestSnpsht speedTestSnapshotObj, GQMeterResponse gqmResponse, Long runId, Session session) {
		String meterId = gqmResponse.getGqmid();

        try {
               // snapshot
                
                if (speedTestSnapshotObj != null) {
                    try {
                        //speedTestSnapshotObj.setId(cid);
                    	GQEDPConstants.logger.debug("runId:"+speedTestSnapshotObj.getId().getRunId());                    	
                    	speedTestSnapshotObj.getId().setRunId(runId);
                    	GQEDPConstants.logger.debug("runId:"+speedTestSnapshotObj.getId().getRunId());
                        session.save(speedTestSnapshotObj);
                        GQEDPConstants.logger.debug(meterId + " Data successfully to save in the SpeedTest Snapshot table ");
                    }
                    catch (Exception e) {
                        GQEDPConstants.logger.error(meterId + " Data failed to save in the SpeedTest Snapshot table ", e);
                    }
                }
               
                // Actual insertion will happen at this step
                session.flush();
        
        }// try ends
        catch (Exception e) {
            GQEDPConstants.logger.error(meterId + " Data failed to save in the SpeedTestMeter exception ", e);
            e.printStackTrace();
        }
        finally {
        	
        }// finally ends
	}
}
