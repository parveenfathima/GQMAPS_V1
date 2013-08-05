package com.gq.meter.restsvcs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gq.meter.model.MapsDomainModel;
import com.gq.meter.object.MapsDomainBean;
import com.gq.meter.util.MapsDomainServiceConstant;

@Path("/mapsDomainServices")
public class MapsDomainServices {

    @Path("/getMapsDomainData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCatalogDetails() {
        MapsDomainServiceConstant.logger
                .info("Generating all the Maps Domain service list from GQEntrprseDataProcessor");
        MapsDomainModel mapsDomainModel = new MapsDomainModel();
        MapsDomainBean mapsDomainResult = mapsDomainModel.getAllMapsDomainDetails();
        // Returning all the enterprises in JSON format
        return MapsDomainServiceConstant.gson.toJson(mapsDomainResult);
    }
}
