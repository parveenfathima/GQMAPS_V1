package com.gq.cust;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.*;
import com.gq.meter.object.Asset;
import com.gq.meter.object.DevCtlg;
import com.gq.util.GQMapsCustomerUIConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AssetHelper {

	static	Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss").create();
	
//	public static void main(String args[]) throws NoSuchElementException,
//			ParseException {
//		List<Asset> la = getAssetList();
//	}

	public static List<Asset> getAssetList(String enterpriseId) throws NoSuchElementException,
			ParseException {

		Client client = Client.create();

		WebResource webResourceAsset = client
		// .resource("http://192.168.1.95:8080/GQMapsCustomerServices/gqm-gqedp/getAssetServices/getAssetData");
				.resource("http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/AssetEditServices/getAssetData?enterpriseId="+ enterpriseId);

		ClientResponse response = webResourceAsset.accept("application/json")
				.get(ClientResponse.class);

		String assetStr = webResourceAsset.get(String.class);

		System.out.println("AssetData String: " + assetStr);

		JSONObject jsnobject = new JSONObject(assetStr);
		JSONArray jsonAssetArray = jsnobject.getJSONArray("assetResult");
		List<Asset> assetListDB = new ArrayList<Asset>();

		System.out.println("length: " + jsonAssetArray.length());

		for (int i = 0; i < jsonAssetArray.length(); i++) {

			Asset asset = gson.fromJson(jsonAssetArray.getJSONObject(i)
					.toString(), Asset.class);

			System.out.println("IP: " + asset.getInactiveDttm());

			if (asset.getProtocolId().equals("computer")) {
				assetListDB.add(asset);
			}
		}

		return assetListDB;
	} // method ends

	public static List<DevCtlg> getDevCtlgList(String enterpriseId) throws NoSuchElementException,
			ParseException {

		Client client = Client.create();

		WebResource webResourceDomain = client
				.resource("http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/mapsDomainServices/getMapsDomainData?enterpriseId="+enterpriseId);

		String domainDataStr = webResourceDomain.get(String.class);

		JSONObject jsnobject = new JSONObject(domainDataStr);

		System.out.println("\nDomainData String: " + domainDataStr);

		JSONArray jsonCtlgArray = jsnobject.getJSONArray("devCtlgResult");
		System.out.println("Catalog length: " + jsonCtlgArray.length());

		List<DevCtlg> dcListDB = new ArrayList<DevCtlg>();
		
		// adding catalog details to the domain data object
		for (int i = 0; i < jsonCtlgArray.length(); i++) {

			DevCtlg devctlg = gson.fromJson(jsonCtlgArray.getJSONObject(i)
					.toString(), DevCtlg.class);
			
			dcListDB.add(devctlg);
		}
		return dcListDB;
	}
}
