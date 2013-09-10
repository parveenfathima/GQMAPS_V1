package com.gq.restsvcs;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Test {
    public static void main(String[] args) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        // Fluent interfaces
        System.out
                .println(service.path("gatekeeper").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString());
        System.out.println(service.path("hello").accept(MediaType.TEXT_PLAIN).get(String.class).toString());

        System.out.println("-------text plain over--------------\n");

        // Get app xml text
        System.out.println(service.path("gatekeeper").accept(MediaType.APPLICATION_XML).get(String.class));

        System.out.println("-------- app xml over -------------\n");

        // Get XML
        System.out.println(service.path("hello").accept(MediaType.TEXT_XML).get(String.class));
        System.out.println("------text xml over--------------\n");

        // The HTML
        System.out.println(service.path("rest").path("hello").accept(MediaType.TEXT_HTML).get(String.class));
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/RESTProject/").build();
    }

}
