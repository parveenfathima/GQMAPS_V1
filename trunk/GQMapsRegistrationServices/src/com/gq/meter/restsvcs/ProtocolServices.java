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

import com.gq.meter.model.ProtocolModel;
import com.gq.meter.object.Protocol;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/protocol")
public class ProtocolServices {

    /**
     * This method used to fetch all the protocols
     * 
     * @return
     */
    @Path("/getProtocols")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEnterprises() {
        GQGateKeeperConstants.logger.info("Generating all the enterprise meters list from GQGatekeeper");
        ProtocolModel protoModel = new ProtocolModel();
        List<Protocol> protocolResult = null;
        try {
            protocolResult = protoModel.getAllProtocols();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises list ", e);
            return Response.status(400).build();
        }
        // Returning all the enterprises in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(protocolResult)).build();
    }

}
