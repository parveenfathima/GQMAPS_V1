package com.gq.meter.restsvcs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.gq.meter.model.MapsDomainModel;
import com.gq.meter.object.MapsDomainBean;
import com.gq.meter.util.CustomerServiceConstant;

@Path("/mapsDomainServices")
public class MapsDomainServices {

    @Path("/getMapsDomainData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCatalogDetails(@QueryParam("enterpriseId") String enterpriseId) {
        CustomerServiceConstant.logger.info(" [MapsDomainServices]  Generating all Domain service list from gqm"
                + enterpriseId);
        MapsDomainModel mapsDomainModel = new MapsDomainModel();
        MapsDomainBean mapsDomainResult = mapsDomainModel.getAllMapsDomainDetails(enterpriseId);
        // Returning all the enterprises in JSON format
        return CustomerServiceConstant.gson.toJson(mapsDomainResult);
        // return mapsDomainResult;
    }
}
