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
     * This method used to authenticate the user
     * 
     * @param authString
     * @return
     */
    @Path("/authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(String authString) {
        Enterprise authObject = null;
        try {
            GQGateKeeperConstants.logger.info("authString : " + authString);
            authObject = GQRegistrationConstants.gson.fromJson(authString, Enterprise.class);
            EnterpriseModel entModel = new EnterpriseModel();
            boolean authValue = entModel.authenticate(authObject);
            if (authValue) {
                return Response.status(200).build();
            }
            else {
                return Response.status(401).build();
            }
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(401).build();
        }
    }

    /**
     * This method used to fetch all the enterprises
     * 
     * @return
     */
    @Path("/getRegistration")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEnterprises() {
        GQGateKeeperConstants.logger.info("Generating all the enterprises list from GQGatekeeper");
        EnterpriseModel entmodel = new EnterpriseModel();
        List<Enterprise> entMeterResult = null;
        try {
            entMeterResult = entmodel.getAllEnterprises();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises list ", e);
            return Response.status(400).build();
        }
        // Returning all the enterprises in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(entMeterResult)).build();
    }

    /**
     * This method used to create new enterprise
     * 
     * @param entObjectString
     * @return
     */
    @Path("/addRegistration")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEnterprise(String entObjectString) {
        Enterprise entObject = null;
        try {
            entObject = GQRegistrationConstants.gson.fromJson(entObjectString, Enterprise.class);
            GQGateKeeperConstants.logger.info("Saving the new enterprise : " + entObject.getEnterpriseId());
            EnterpriseModel entmodel = new EnterpriseModel();
            entmodel.addEnterprise(entObject);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(400).build();
        }
        return Response.ok(GQRegistrationConstants.gson.toJson("success")).build();
    }

    /**
     * TODO: have to check hibernate saveorupdate method, if it works can use one method for both save and update
     * operation
     * 
     * @param entObjectString
     * @return
     */
    @Path("/updateRegistration")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEnterprise(String entObjectString) {
        Enterprise entObject = null;
        try {
            entObject = GQRegistrationConstants.gson.fromJson(entObjectString, Enterprise.class);
            GQGateKeeperConstants.logger.info("Saving the new enterprise : " + entObject.getSid());

            EnterpriseModel entmodel = new EnterpriseModel();
            entmodel.updateEnterprise(entObject);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(400).build();
        }
        return Response.status(200).build();
    }
}