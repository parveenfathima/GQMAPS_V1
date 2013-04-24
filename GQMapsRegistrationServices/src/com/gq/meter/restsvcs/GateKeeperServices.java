/**
 * 
 */
package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.GateKeeperModel;
import com.gq.meter.object.GateKeeper;
import com.gq.meter.object.GateKeeperAudit;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/gatekeeper")
public class GateKeeperServices {

    @Path("/addGateKeeper")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGateKeeper(String gkGatekeeper) {
        GateKeeper gKeeper = null;
        try {
            gKeeper = GQRegistrationConstants.gson.fromJson(gkGatekeeper, GateKeeper.class);
            GateKeeperModel gkModel = new GateKeeperModel();
            gkModel.addGateKeeper(gKeeper);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises list ", e);
            return Response.status(400).build();
        }
        return Response.ok("success").build();
    }

    @Path("/addAudit")
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
        return Response.ok("success").build();
    }

}
