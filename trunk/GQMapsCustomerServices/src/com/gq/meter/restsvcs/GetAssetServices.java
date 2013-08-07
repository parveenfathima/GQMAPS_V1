package com.gq.meter.restsvcs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gq.meter.model.GetAssetModel;
import com.gq.meter.object.GetAsset;
import com.gq.meter.util.GetAssetServiceConstant;

@Path("/getAssetServices")
public class GetAssetServices {

    @Path("/getAssetData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAssetDetails() {

        GetAssetServiceConstant.logger.info("Generating all the Get Asset service list from GQEntrprseDataProcessor");
        GetAssetModel getAssetModel = new GetAssetModel();
        GetAsset getAssetResult = getAssetModel.getAssetDetails();
        // Returning all the enterprises in JSON format
        return GetAssetServiceConstant.gson.toJson(getAssetResult);
    }

}