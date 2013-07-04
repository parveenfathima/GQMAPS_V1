package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gq.meter.model.AssetModel;
import com.gq.meter.model.CustomerServiceModel;
import com.gq.meter.object.AllCustomerServices;
import com.gq.meter.object.Asset;
import com.gq.meter.util.CustomerServiceConstant;

@Path("/customerservices")
public class CustomerServices {

    /**
     * This method used to fetch all the customer services
     * 
     * @return
     */
    @Path("/getdetails")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCustomerServiceDetails() {
        CustomerServiceConstant.logger.info("Generating all the customer service list from GQEntrprseDataProcessor");
        CustomerServiceModel customerServiceModel = new CustomerServiceModel();
        AllCustomerServices customerServiceResult = customerServiceModel.getAllCustomerServiceDetails();
        // Returning all the enterprises in JSON format
        return CustomerServiceConstant.gson.toJson(customerServiceResult);
    }

    @Path("/getAssetCount")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAssetCount() {
        CustomerServiceConstant.logger.info("Generating all the customer service list from GQEntrprseDataProcessor");
        AssetModel asset = new AssetModel();
        List<Asset> assetresult = asset.getAssetCount();
        // Returning all the enterprises in JSON format
        CustomerServiceConstant.logger.info("Number of asset Id in asset" + assetresult);
        return CustomerServiceConstant.gson.toJson(assetresult);
    }
}
