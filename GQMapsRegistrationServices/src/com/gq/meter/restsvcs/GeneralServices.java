/**
 * 
 */
package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.EntpSummaryModel;
import com.gq.meter.object.EntpSummary;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author GQ
 * 
 */
@Path("/general")
public class GeneralServices {
    @Path("/getEntpSummary")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEntpSummary(@QueryParam("entpId") String entpId) {

        GQGateKeeperConstants.logger.info("authString : " + entpId);
        EntpSummaryModel entpModel = new EntpSummaryModel();
        List<EntpSummary> result = null;
        try {
            result = entpModel.getEntpSummary(entpId);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the details of enterpriseid ", e);
            return Response.status(400).build();
        }
        return Response.ok(GQRegistrationConstants.gson.toJson(result)).build();
        // return Response.ok(200).build();
    }
}
