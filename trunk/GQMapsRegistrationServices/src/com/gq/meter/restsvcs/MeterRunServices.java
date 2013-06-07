package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.MeterRunModel;
import com.gq.meter.object.MeterRun;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;
/**
 * @author Rathish
 * 
 */

@Path("/meterrun")

public class MeterRunServices {
    
    @Path("/getMeterRun")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeterRun(@QueryParam("entpId") String entpId) {
        
        GQGateKeeperConstants.logger.info("Generating all the enterprise meters list from EnterpriseMeter");
        MeterRunModel meterModel = new MeterRunModel();
        List<MeterRun> meterResult = null;
        
        try {
            meterResult = meterModel.getMeterRun(entpId);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the meter list ", e);
            return Response.status(400).build();
        }
        // Returning all the meterList in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(meterResult)).build();
    }

}
