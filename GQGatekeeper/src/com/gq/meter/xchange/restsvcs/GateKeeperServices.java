package com.gq.meter.xchange.restsvcs;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.xchange.filter.GateKeeperFilter;
import com.gq.meter.xchange.model.EnterpriseModel;
import com.gq.meter.xchange.object.Enterprise;
import com.gq.meter.xchange.util.GQGateKeeperConstants;

/**
 * @author Chandru
 * 
 */
@Path("/gatekeeper")
public class GateKeeperServices {

    /**
     * This is a test method which returns text response
     * 
     * @return
     */
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    public String sayHello() {
        GQGateKeeperConstants.logger.info("Gate keeper is up and running.....");
        return "Gate keeper is up and running.....";
    }

    /**
     * This method used to fetch all the enterprises
     * 
     * @return
     */
    @Path("/getentmeters")
    @GET
    @Produces("text/plain")
    public String getAllEnterprises() {
        GQGateKeeperConstants.logger.info("Generating all the enterprises list from GQGatekeeper");
        EnterpriseModel entmodel = new EnterpriseModel();
        List<Enterprise> entMeterResult = entmodel.getAllEnterprises();
        return entMeterResult.get(0).getAns1();
    }

    /**
     * This method accepts json type string as input and processes the data
     * 
     * @param gqMeterResponseString
     * @throws IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void keepGate(@FormParam("gqMeterResponse") String gqMeterResponseString) {
        // GQGateKeeperConstants.logger.debug("Incoming json is : " + gqMeterResponseString);
        GQGateKeeperConstants.logger.info("Data is received by GQGatekeeper");
        Gson gson = new GsonBuilder().create();

        GQMeterResponse gqMeterResponse = null;
        try {
            GQGateKeeperConstants.logger.info("Unmarshalling the received data");
            // unmarshall the response from the gqmeter running in premise and start processing.....
            gqMeterResponse = gson.fromJson(gqMeterResponseString, GQMeterResponse.class);
        }
        catch (JsonSyntaxException e) {
            GQGateKeeperConstants.logger.fatal("Unmarshall exception is occured : ", e);
        }

        GQGateKeeperConstants.logger.info("Unmarshalll is finished successfully");
        GateKeeperFilter gkf = new GateKeeperFilter();

        GQGateKeeperConstants.logger.info("Sending unmarshalled data for validation");
        gkf.process(gqMeterResponse);
    }

}