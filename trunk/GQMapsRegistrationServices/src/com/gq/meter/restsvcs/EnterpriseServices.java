package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.EnterpriseModel;
import com.gq.meter.object.Enterprise;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/enterprise")
public class EnterpriseServices {
    /**
     * This method used to fetch all the enterprises
     * 
     * @return
     */
    @Path("/getregistration")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEnterprises() {
        GQGateKeeperConstants.logger.info("Generating all the enterprises list from GQGatekeeper");
        EnterpriseModel entmodel = new EnterpriseModel();
        List<Enterprise> entMeterResult = entmodel.getAllEnterprises();
        // Returning all the enterprises in JSON format
        return GQRegistrationConstants.gson.toJson(entMeterResult);
    }

    /**
     * This method used to create new enterprise
     * 
     * @param entObjectString
     * @return
     */
    @Path("/addregistration")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEnterprise(String entObjectString) {
        Enterprise entObject = null;
        try {
            entObject = GQRegistrationConstants.gson.fromJson(entObjectString, Enterprise.class);
            GQGateKeeperConstants.logger.info("Saving the new enterprise : " + entObject.getEnterpriseId());
            EnterpriseModel entmodel = new EnterpriseModel();
            entmodel.createEnterprise(entObject);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(400).build();
        }
        return Response.ok("success").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEnterprise(String entObjectString) {
        Enterprise entObject = null;
        try {
            entObject = GQRegistrationConstants.gson.fromJson(entObjectString, Enterprise.class);
            GQGateKeeperConstants.logger.info("Saving the new enterprise : " + entObject.getEnterpriseId());

            EnterpriseModel entmodel = new EnterpriseModel();
            entmodel.updateEnterprise(entObject);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(400).build();
        }
        return Response.ok("success").build();
    }
}