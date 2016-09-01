/**
 * 
 */
package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.BusinessLineModel;
import com.gq.meter.object.BusinessLine;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author GQ
 * 
 */
@Path("/busnLine")
public class BusinessLineServices {

    /**
     * This method used to fetch all the protocols
     * 
     * @return
     */
    @Path("/getBusnLine")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBusinessCodes() {
        GQGateKeeperConstants.logger.info("Generating all the BusinessLine codes from GQGatekeeper");
        BusinessLineModel busLineModel = new BusinessLineModel();
        List<BusinessLine> busLineResult = null;
        try {
            busLineResult = busLineModel.getAllBusinessCodes();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the BusinessLine codes ", e);
            return Response.status(400).build();
        }
        // Returning all the enterprises in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(busLineResult)).build();
    }

}
