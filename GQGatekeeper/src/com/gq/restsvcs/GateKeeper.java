package com.gq.restsvcs;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.bo.GateKeeperFilter;
import com.gq.meter.GQMeterResponse;

//import com.sun.jerse
// POJO, no interface no extends

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/gatekeeper")
public class GateKeeper {

    // This method is called if TEXT_PLAIN is request
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    public String sayHello() {
        return "Gate keeper is up and running.....";
    }

    // This method is called if HTML is request
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
        return "<html> " + "<title>" + "Hello Jersey text html" + "</title>" + "<body><h1>" + "Hello Jersey text html "
                + "</body></h1>" + "</html> ";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void keepGate(@FormParam("gqMeterResponse") String gqMeterResponseString) throws IOException {

        System.out.println("Incoming json is => " + gqMeterResponseString);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        GQMeterResponse gqMeterResponse = gson.fromJson(gqMeterResponseString, GQMeterResponse.class);
        // unmarshall the response from the gqmeter running in premise and start processing.....

        GateKeeperFilter gkf = new GateKeeperFilter();

        gkf.process(gqMeterResponse);
    }

}