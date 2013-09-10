package com.gq.restsvcs;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.model.Todo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

public class Tester {
    public static void main(String[] args) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());

        System.out.println("getBaseURI() .." + getBaseURI());

        // Create one todo
        Todo todo = new Todo("3", "Blablablah");
        // ClientResponse response =
        // service.path("gqm-gk").path("gatekeeper").path(todo.getId()).accept(MediaType.APPLICATION_XML).put(ClientResponse.class,
        // todo);
        // // Return code should be 201 == created resource
        // System.out.println(response.getStatus());
        //
        // System.out.println("-------------------------------------------------------------------");

        System.out.println("about to call rest web service.....");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // send a meter response json to be put to the system
        Form form = new Form();
        form.add("gqMeterResponse", gson.toJson(todo));
        form.add("summary", "Demonstration of the client lib for forms");
        ClientResponse response = service.path("gatekeeper").type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, form);
        // System.out.println("Form response " + response.getEntity(String.class));

        // // Get the all todos, id 4 should be deleted
        // System.out.println(service.path("rest").path("todos").accept(
        // MediaType.APPLICATION_XML).get(String.class));

    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/GQGatekeeper").build();
    }
}
