package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.object.Asset;
import com.gq.meter.object.GetAsset;
import com.gq.meter.util.UpdateAssetServiceConstant;
import com.gq.meter.model.UpdateAssetModel;

@Path("/updateAssetServices")
public class UpdateAssetServices {

    @Path("/updateAssetData")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAsset(String assetObjectString) {
        Asset assetObject = null;
        try {
            assetObject = UpdateAssetServiceConstant.gson.fromJson(assetObjectString, Asset.class);
            UpdateAssetModel assetModel = new UpdateAssetModel();
            assetModel.updateAssets(assetObject);
        }
        catch (Exception e) {
            UpdateAssetServiceConstant.logger.error("Exception occured while updating enterprise", e);
            return Response.status(400).build();
        }
        // return Response.status(200).build();
        return Response.ok(UpdateAssetServiceConstant.gson.toJson("success")).build();
    }
}
