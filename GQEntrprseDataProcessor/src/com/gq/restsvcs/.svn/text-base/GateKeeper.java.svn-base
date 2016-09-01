package com.gq.restsvcs;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import com.gq.bo.EDPFilter;
import com.gq.meter.GQMeterResponse;
import com.gq.util.GQEDPConstants;

//import com.sun.jerse
// POJO, no interface no extends

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/gqentdataprocessor")
public class GateKeeper {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void keepGate(@FormParam("gqMeterResponse") String gqMeterResponseString) {

        try {
        	
            GQEDPConstants.logger.debug("\n******************** NEW EDP WS REQUEST BEGINS *************************************\n");

            GQEDPConstants.logger.debug(" Incoming json is => " + gqMeterResponseString);

            Gson gson = new GsonBuilder().create();

            GQMeterResponse gqMeterResponse = gson.fromJson(gqMeterResponseString, GQMeterResponse.class);
            // unmarshall the response from the gqmeter running in premise and start processing.....

            EDPFilter gkf = new EDPFilter();

            gkf.process(gqMeterResponse);
        }
        catch (JsonSyntaxException e) {
            e.printStackTrace();
            GQEDPConstants.logger.error(" Exception received from json " + e);
        }
        finally {
            GQEDPConstants.logger.debug("\n******************** EDP WS REQUEST ENDS *************************************\n");
        }
        
//        GqEDPFilter gkf = new GqEDPFilter();
//        Gson gson = new GsonBuilder().create();
//        GQMeterResponse gqMeterResponse = gson.fromJson(gqMeterResponseString, GQMeterResponse.class);
//        gkf.process(gqMeterResponse);
    }

}