package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;
import com.gq.meter.model.EnterpriseModel;
import com.gq.meter.object.Enterprise;
import com.gq.meter.util.GQGateKeeperConstants;

/**
 * @author Chandru
 * 
 */
@Path("/gatekeeper")

public class GateKeeperServices {

    /**
     * This method used to fetch all the enterprises
     * 
     * @return
     */
    @GET
    @Path("/getregistration")
    @Produces("application/json")
       
    public String getAllEnterprises() {
    	
		String feedsEnterprise  = null;
		
        GQGateKeeperConstants.logger.info("Generating all the enterprises list from GQGatekeeper");
        EnterpriseModel entmodel = new EnterpriseModel();
        List<Enterprise> entMeterResult = entmodel.getAllEnterprises();

        //Converting the object json format using the gson object.
        Gson gson = new Gson();
        feedsEnterprise = gson.toJson(entMeterResult);
        
        //System.out.println(entMeterResult);
        return feedsEnterprise;
    }

}