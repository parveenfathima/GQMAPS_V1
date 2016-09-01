/**
 * 
 */
package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.GateKeeperModel;
import com.gq.meter.object.GateKeeperAudit;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/gatekeeper")
public class GateKeeperServices {

    @Path("/addEntAudit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGateKeeperAudit(String gkAuditString) {
        GateKeeperAudit gkAudit = null;
        try {
            gkAudit = GQRegistrationConstants.gson.fromJson(gkAuditString, GateKeeperAudit.class);
            GateKeeperModel gkModel = new GateKeeperModel();
            gkModel.addGateKeeperAudit(gkAudit);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises list ", e);
            return Response.status(400).build();
        }
        // return Response.status(200).build();
        return Response.ok(GQRegistrationConstants.gson.toJson("success")).build();
    }

    @Path("/getExpiryDays")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpiryDate(@QueryParam("entpId") String entpId) {
        GQGateKeeperConstants.logger.info("Service is retrieving expiry date");
        GateKeeperModel gkModel = new GateKeeperModel();
        int dt = 0;
        String result = null;

        try {
            dt = gkModel.getExpiryDate(entpId);
            System.out.println("int is " + dt);
            if (dt < 0) {
                result = "Your meter is expired";
            }
            else if (dt >= 0 && dt <= 15) {
                result = "Your meter will expire in " + dt + "days";
            }
            else {
                result = "No alerts";
            }
            System.out.println(result);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the Expirydate ", e);
            return Response.status(400).build();
        }
        return Response.ok(GQRegistrationConstants.gson.toJson(result)).build();

        // return Response.ok(GQRegistrationConstants.gson.toJson(gkList)).build();

    }
}
