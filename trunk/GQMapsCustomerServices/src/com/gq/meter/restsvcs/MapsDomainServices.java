package com.gq.meter.restsvcs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.MediaType;

import com.gq.meter.model.MapsDomainModel;

import com.gq.meter.object.MapsDomainBean;

import com.gq.meter.util.CustomerServiceConstant;

/**
 * @author parveen
 * 
 */

// This class is define the services of all domain data's
@Path("/mapsDomainServices")
public class MapsDomainServices {

    @Path("/getMapsDomainData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // This method takes care of getting all domain details
    public String getAllDomainDetails(@QueryParam("enterpriseId") String enterpriseId) {

        CustomerServiceConstant.logger.info("Generating all Domain service list from gqm" + enterpriseId);
        MapsDomainModel mapsDomainModel = new MapsDomainModel();
        MapsDomainBean mapsDomainResult = mapsDomainModel.getAllMapsDomainDetails(enterpriseId);
        // Returning all the domain list in JSON format
        return CustomerServiceConstant.gson.toJson(mapsDomainResult);
    }
}
