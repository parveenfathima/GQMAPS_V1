/**
 * 
 */
package com.gq.meter.xchange.restsvcs;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        String result = null;
        try {
            GQGateKeeperConstants.logger.info("Meter Validity is going to be processed ");
            gatekeeperResult = gkf.getExpirydate(meterId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String expirydDate = sdf.format(gatekeeperResult.get(0));
            Object o = gatekeeperResult.get(1);
            String protocolId = o.toString();
            String currDate = sdf.format(new Date());
            int dateValue = expirydDate.compareTo(currDate);
            GQGateKeeperConstants.logger.debug("Expiry Result from GateKeeperResult:" + expirydDate);
            GQGateKeeperConstants.logger.debug("ProtocolId from GateKeeperResult:" + protocolId);
            if (dateValue < 0) {
                GQGateKeeperConstants.logger.info("validating the expiry date and date value is " + dateValue
                        + "for the meter" + meterId);
                result = "expired";
            }
            else {
                result = "valid" + protocolId;
            }
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the details of enterpriseid ", e);
            return Response.status(400).build();
        }
        GQGateKeeperConstants.logger.info("Value of the Result before Returning to GqMeter " + gatekeeperResult.get(0));
        return Response.ok(GQRegistrationConstants.gson.toJson(result)).build();
        // return Response.status(200).entity("Valid").type("text/plain").build();
    }
}
