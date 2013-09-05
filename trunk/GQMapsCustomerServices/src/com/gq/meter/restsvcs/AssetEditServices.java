package com.gq.meter.restsvcs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.AssetEditModel;

import com.gq.meter.object.GetAsset;
import com.gq.meter.object.Asset;

import com.gq.meter.util.CustomerServiceConstant;

@Path("/AssetEditServices")
public class AssetEditServices {
    @Path("/getAssetData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAssetDetails(@QueryParam("enterpriseId") String enterpriseId) {
        System.out.println(enterpriseId);
        CustomerServiceConstant.logger
                .info(" [AssetEditServices]  Generating all the Get Asset service list from GQEntrprseDataProcessor");
        AssetEditModel getAssetModel = new AssetEditModel();
        GetAsset getAssetResult = getAssetModel.getAssetDetails(enterpriseId);
        // Returning all the enterprises in JSON format
        return CustomerServiceConstant.gson.toJson(getAssetResult);
    }

    @Path("/updateAssetData")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAsset(@QueryParam("enterpriseId") String enterpriseId, String assetObjectString) {
        Asset[] assetObject = null;
        try {
            // Update the Asset Details
            assetObject = CustomerServiceConstant.gson.fromJson(assetObjectString, Asset[].class);
            for (int assetCount = 0; assetCount < assetObject.length; assetCount++) {
                AssetEditModel assetModel = new AssetEditModel();
                assetModel.updateAssets(enterpriseId, assetObject[assetCount]);
            }
        }
        catch (Exception e) {
            CustomerServiceConstant.logger
                    .error(" [AssetEditServices]  Exception occured while updating enterprise", e);
            return Response.status(400).build();
        }
        // return Response.status(200).build();
        return Response.ok(CustomerServiceConstant.gson.toJson("success")).build();
    }
}
