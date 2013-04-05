/**
 * 
 */
package com.gq.meter.xchange.controller;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQErrorInformation;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.xchange.model.ClientDataModel;
import com.gq.meter.xchange.model.GqMeterErrorInfo;
import com.gq.meter.xchange.util.GQGateKeeperConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author Chandru
 * 
 */
public class GQDataXchangeController {

    public static void sendToEntDataProcessor(String fwdUrl, String protocolId, GQMeterResponse gqmResponse) {

        GQGateKeeperConstants.logger
                .info("*************** After validation Sending data from the GQGatekeeper ******************");

        // Inserting error information into asset_err table
        List<GQErrorInformation> gqerrList = gqmResponse.getErrorInformationList();
        GqMeterErrorInfo.insertErrorInfo(gqerrList, gqmResponse.getRunid());

        Gson gson = new GsonBuilder().create();

        GQGateKeeperConstants.logger
                .info(":::::::::::::::::::::XCHANGE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        GQGateKeeperConstants.logger.info("json of GQMeterData = " + gson.toJson(gqmResponse));
        GQGateKeeperConstants.logger
                .info(":::::::::::::::::::::XCHANGE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        try {
            // Sending the generated json output to the server
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource service = client.resource(fwdUrl);

            com.sun.jersey.api.representation.Form form = new com.sun.jersey.api.representation.Form();
            form.add("gqMeterResponse", gson.toJson(gqmResponse));
            form.add("summary", "Demonstration of the client lib for forms");
            ClientResponse response = service.path("gqm-gqedp").path("gqentdataprocessor")
                    .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, form);
            GQGateKeeperConstants.logger.info("*************** sent successfully to " + fwdUrl + " ******************");
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.info("Exception occured while sending data from XChange Controller");
            GQGateKeeperConstants.logger.info("Saving data to Client Data table for backup");
            ClientDataModel cDataModel = new ClientDataModel();
            cDataModel.saveClientData(gqmResponse.getRunid(), gqmResponse.toString(), new Date());
        }
    }
}
