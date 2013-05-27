/**
 * 
 */
package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.EnterpriseMeterModel;
import com.gq.meter.object.EnterpriseMeter;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/enterpriseMeters")
public class EnterpriseMeterServices {

    /**
     * This method used to fetch all the enterprises
     * 
     * @return
     */
    @Path("/getEntMeters")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnterprises(@QueryParam("entpId") String entpId) {

        GQGateKeeperConstants.logger.info("Generating all the enterprise meters list from GQGatekeeper");
        EnterpriseMeterModel entMeterModel = new EnterpriseMeterModel();
        List<EnterpriseMeter> entMeterResult = null;
        try {
            entMeterResult = entMeterModel.getEnterpriseMeters(entpId);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises list ", e);
            return Response.status(400).build();
        }
        // Returning all the enterprises in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(entMeterResult)).build();
    }

    @Path("/addEntMeters")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEnterpriseMeter(String entMeterString) {
        EnterpriseMeter entMeterObject = null;
        try {
            entMeterObject = GQRegistrationConstants.gson.fromJson(entMeterString, EnterpriseMeter.class);
            GQGateKeeperConstants.logger.info("Saving the new enterprise : " + entMeterObject.getEnterpriseId());
            EnterpriseMeterModel entMeterModel = new EnterpriseMeterModel();
            entMeterModel.addEnterpriseMeter(entMeterObject);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(400).build();
        }
        return Response.status(200).build();
    }

}
