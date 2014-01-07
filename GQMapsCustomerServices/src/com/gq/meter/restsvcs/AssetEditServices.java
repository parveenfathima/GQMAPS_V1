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

import com.gq.meter.util.CustomerServiceUtils;

/**
 * @author parveen
 * 
 */

// This class defines the services of getting and updating asset(computer,printer,nsrg and storage) data's.
@Path("/AssetEditServices")
public class AssetEditServices {

    @Path("/getAssetData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // This method takes care of getting asset details for requesting enterprise.
    public String getAllAssetDetails(@QueryParam("enterpriseId") String enterpriseId) {

        CustomerServiceUtils.logger.debug(" Generating all the Asset list for requesting enterprise");
        AssetEditModel getAssetModel = new AssetEditModel();
        GetAsset getAssetResult = getAssetModel.getAssetDetails(enterpriseId);
        // Returning all the Assets in JSON format
        return CustomerServiceUtils.gson.toJson(getAssetResult);
    }

    @Path("/updateAssetData")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    // This method takes care of updating asset details for requesting enterprise.
    public Response updateAsset(@QueryParam("enterpriseId") String enterpriseId, String assetObjectString) {
        Asset[] assetObject = null;
        try {
            // Update the Asset Details
            assetObject = CustomerServiceUtils.gson.fromJson(assetObjectString, Asset[].class);
            for (int assetCount = 0; assetCount < assetObject.length; assetCount++) {
                AssetEditModel assetModel = new AssetEditModel();
                assetModel.updateAssets(enterpriseId, assetObject[assetCount]);
            }
        }
        catch (Exception e) {
            CustomerServiceUtils.logger.error(" Exception occured while updating enterprise", e);
            return Response.status(400).build();
        }
        return Response.ok(CustomerServiceUtils.gson.toJson("success")).build();
    }
}
