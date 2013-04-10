/**
 * 
 */
package com.gq.meter.xchange.controller;

import java.util.Date;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.xchange.model.ClientDataModel;
import com.gq.meter.xchange.util.GQGateKeeperConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 * @author Chandru
 * 
 */
public class GQDataXchangeController {

    public void sendToEntDataProcessor(String fwdUrl, String protocolId, GQMeterResponse gqmResponse) {

        GQGateKeeperConstants.logger.info("After validation Sending data from GQGatekeeper to GQEDP");

        Gson gson = new GsonBuilder().create();

        // GQGateKeeperConstants.logger.debug(":::::::::::::::::::::XCHANGE:::::::::::::::::::::::::::::::");
        // GQGateKeeperConstants.logger.debug("json of GQMeterData = " + gson.toJson(gqmResponse));
        // GQGateKeeperConstants.logger.debug(":::::::::::::::::::::XCHANGE:::::::::::::::::::::::::::::::");

        try {
            // Sending the generated json output to the server
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);

            WebResource service = client.resource(fwdUrl);
            service = service.path("gqm-gqedp").path("gqentdataprocessor");

            Form form = new Form();
            form.add("gqMeterResponse", gson.toJson(gqmResponse));
            form.add("summary", "Demonstration of the client lib for forms");

            Builder builder = service.type(MediaType.APPLICATION_JSON);

            ClientResponse response = builder.post(ClientResponse.class, form);
            GQGateKeeperConstants.logger.info("Data is sent successfully to : " + fwdUrl);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while sending data from GQGatekeeper to GQEDP", e);
            GQGateKeeperConstants.logger.info("Saving data to Client Data table for backup");

            ClientDataModel cDataModel = new ClientDataModel();
            cDataModel.saveClientData(gqmResponse.getRunid(), gqmResponse.toString(), new Date());
        }
    }
}
