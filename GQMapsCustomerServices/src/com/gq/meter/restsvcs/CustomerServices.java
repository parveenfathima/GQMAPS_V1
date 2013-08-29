package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gq.meter.model.AssetLoadModel;
import com.gq.meter.model.AssetModel;
import com.gq.meter.model.CustomerServiceModel;
import com.gq.meter.object.AllCustomerServices;
import com.gq.meter.object.Asset;
import com.gq.meter.object.AssetLoad;
import com.gq.meter.object.ProtocolCount;
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
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES]  Generating all the customer service list from GQEntrprseDataProcessor");
        CustomerServiceModel customerServiceModel = new CustomerServiceModel();
        AllCustomerServices customerServiceResult = customerServiceModel.getAllCustomerServiceDetails();
        // Returning all the enterprises in JSON format
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES] Sucessfully Data Received from GQEntrprseDataProcessor");
        return CustomerServiceConstant.gson.toJson(customerServiceResult);
    }

    @Path("/getAssetCount")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAssetCount() {
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES]  Generating all the customer service list from GQEntrprseDataProcessor");
        AssetModel asset = new AssetModel();
        List<Asset> assetresult = asset.getAssetCount();
        // Returning all the enterprises in JSON format
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES] Sucessfully Data Received from GQEntrprseDataProcessor For AssetCount");
        return CustomerServiceConstant.gson.toJson(assetresult);
    }

    @Path("/getEntpProtocolCount")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntpProtocolCount() {
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES]  Generating all the customer service list from GQEntrprseDataProcessor");
        AssetModel asset = new AssetModel();
        List<ProtocolCount> protocolresult = asset.getEntpProtocolCount();
        // Returning all the enterprises in JSON format
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES] Sucessfully Data Received from GQEntrprseDataProcessor For Protocol Count");
        return CustomerServiceConstant.gson.toJson(protocolresult);
    }

    @Path("/mostConsumedAssets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String mostConsumedAssets() {
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES]  Generating all the customer service list from GQEntrprseDataProcessor");
        AssetLoadModel mostAsset = new AssetLoadModel();

        List<AssetLoad> assetresult = mostAsset.mostConsumedAssets();
        // Returning all the enterprises in JSON format
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES] Sucessfully Data Received from GQEntrprseDataProcessor For Most Consumed Asseets");
        return CustomerServiceConstant.gson.toJson(assetresult);
    }

    @Path("/leastConsumedAssets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String leastConsumedAssets() {
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES]  Generating all the customer service list from GQEntrprseDataProcessor");
        AssetLoadModel leastAsset = new AssetLoadModel();
        List<AssetLoad> assetresult = leastAsset.leastConsumedAssets();
        // Returning all the enterprises in JSON format
        CustomerServiceConstant.logger
                .info("[CUSTOMERSERVICES] Sucessfully Data Received from GQEntrprseDataProcessor For Least Consumed Assets");
        return CustomerServiceConstant.gson.toJson(assetresult);
    }

}
