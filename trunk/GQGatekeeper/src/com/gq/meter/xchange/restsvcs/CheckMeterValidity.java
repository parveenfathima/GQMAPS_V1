/**
 * 
 */
package com.gq.meter.xchange.restsvcs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.xchange.filter.GateKeeperFilter;
import com.gq.meter.xchange.object.GateKeeper;
import com.gq.meter.xchange.util.GQGateKeeperConstants;
import com.gq.meter.xchange.util.GQRegistrationConstants;

/**
 * @author Rathish
 * 
 */
@Path("/metercheck")
public class CheckMeterValidity {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpirydate(@QueryParam("meterId") String meterId) {
        GQGateKeeperConstants.logger.info("MeterId received from GQMeter " + meterId);
        Gson gson = new GsonBuilder().create();

        GQGateKeeperConstants.logger
                .info("Generating expiry date for the meter to validate in gqmeter from GQGatekeeper ");
        GateKeeperFilter gkf = new GateKeeperFilter();
        List<GateKeeper> gatekeeperResult = null;
        try {
            GQGateKeeperConstants.logger.info("Meter Validity is going to be processed ");
            gatekeeperResult = gkf.getExpirydate(meterId);

        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the details of enterpriseid ", e);
            return Response.status(400).build();
        }
        GQGateKeeperConstants.logger.info("Value of the Result before Returning to GqMeter " + gatekeeperResult.get(0));
        return Response.ok(GQRegistrationConstants.gson.toJson(gatekeeperResult)).build();
        // return Response.status(200).entity("Success").type("text/plain").build();

    }
}
