/**
 * 
 */
package com.gq.meter.xchange.controller;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.xchange.model.ClientDataModel;
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

    private static final String PROTOCOL_IT = "it";

    public static void sendToEntDataProcessor(String fwdUrl, String protocolId, GQMeterResponse gqmResponse) {

        System.out.println("*************** After validation Sending data from the GQGatekeeper ******************");

        protocolId = protocolId.toLowerCase();
        if (protocolId != PROTOCOL_IT) {
            List<ProtocolData> pdList = gqmResponse.getAssetInformationList();

            for (ProtocolData pdData : pdList) {
                System.out.println("protocolId : " + protocolId + " from JSON" + pdData.getProtocol().toString());
                if (!pdData.getProtocol().toString().toLowerCase().trim().equals(protocolId)) {
                    pdList.remove(pdData);
                    System.out.println("invalid meter data");
                }
            }// for ends
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(":::::::::::::::::::::XCHANGE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println("json of GQMeterData = " + gson.toJson(gqmResponse));
        System.out.println(":::::::::::::::::::::XCHANGE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

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
            System.out.println("*************** sent successfully to " + fwdUrl + " ******************");
        }
        catch (Exception e) {
            System.out.println("Exception occured while sending data from XChange Controller");
            System.out.println("Saving data to Client Data table for backup");
            ClientDataModel cDataModel = new ClientDataModel();
            cDataModel.saveClientData(gqmResponse.getRunid(), gqmResponse.toString(), new Date());
        }
    }
}
