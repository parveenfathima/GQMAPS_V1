/**
 * 
 */
package com.gq.meter.xchange.controller;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author GQ
 * 
 */
public class GQDataXchangeController {

    public static void sendToEntDataProcessor(String fwdUrl, String protocolId, GQMeterResponse gqmResponse) {

        System.out.println("*************** After validation Sending data form the GQGatekeeper ******************");

        List<ProtocolData> pdList = gqmResponse.getAssetInformationList();

        List<ProtocolData> pdXchangeList = new LinkedList<ProtocolData>();

        for (ProtocolData pdData : pdList) {

            if (!pdData.getProtocol().toString().equals(protocolId)) {
                // ProtocolData pData = new ProtocolData(MeterProtocols.COMPUTER, pdData.getData());
                // pdXchangeList.add(pData);
                // }
                // else {
                pdList.remove(pdData);
                System.out.println("invalid meter data");
            }

        }
        // gqmResponse.setAssetInformationList(pdXchangeList);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(":::::::::::::::::::::XCHANGE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println("json of GQMeterData = " + gson.toJson(gqmResponse));
        System.out.println(":::::::::::::::::::::XCHANGE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        System.out.println("!!!!!!!!!!!!!!! : " + fwdUrl);

        // Sending the generated json output to the server
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(fwdUrl);

        com.sun.jersey.api.representation.Form form = new com.sun.jersey.api.representation.Form();
        form.add("gqMeterResponse", gson.toJson(gqmResponse));
        form.add("summary", "Demonstration of the client lib for forms");
        ClientResponse response = service.path("gqm-gqedp").path("gqentdataprocessor")
                .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, form);
        System.out.println("*************** sent successfully ******************");
    }
}
