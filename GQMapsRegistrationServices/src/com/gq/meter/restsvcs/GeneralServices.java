/**
 * 
 */
package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.EnterpriseModel;
import com.gq.meter.model.EntpSummaryModel;
import com.gq.meter.object.Enterprise;
import com.gq.meter.object.EntpSummary;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Rathish
 * 
 */
@Path("/general")
public class GeneralServices {
    @Path("/getEntpSummary")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEntpSummary(@QueryParam("entpId") String entpId) {

        GQGateKeeperConstants.logger.info("EnterpriseId is for single enterprise details is: : " + entpId);
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

    @Path("/getEntpSummaryList")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntpSummaryList() {

        GQGateKeeperConstants.logger.info("Generating summary of all enterprises");
        EntpSummaryModel entpModel = new EntpSummaryModel();
        List<EntpSummary> summaryResult = null;
        try {
            summaryResult = entpModel.getEntpSummaryList();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the details of enterpriseid ", e);
            return Response.status(400).build();
        }
        return Response.ok(GQRegistrationConstants.gson.toJson(summaryResult)).build();
        // return Response.ok(200).build();
    }

    @Path("/registrationEmail")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegistrationEmail(@QueryParam("sId") short sId) {

        GQGateKeeperConstants.logger.info("Generating all the enterprise meters list from EnterpriseMeter");
        EnterpriseModel enpModel = new EnterpriseModel();
        List<Enterprise> meterResult = null;

        try {
            meterResult = enpModel.registrationEmail(sId);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the meter list ", e);
            return Response.status(400).build();
        }
        // Returning all the meterList in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(meterResult)).build();
    }

}
