package com.gq;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.object.*;
import com.gq.ui.object.DomainData;
import com.gq.util.GQMapsCustomerUIConstants;

public class AssetData {

	String enterpriseId ;
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss").create();

	public AssetData() {

	}

	// catalog list generation from the total domain data
	public List<DomainData> getCatalogList(String protocol)
			throws NoSuchElementException, ParseException {

		List<DomainData> domainDataListDB = new ArrayList<DomainData>();
		domainDataListDB = getDomainDataList(enterpriseId);

		List<DomainData> catalogListDB = new ArrayList<DomainData>();

		for (int i = 0; i < domainDataListDB.size(); i++) {
			if (domainDataListDB.get(i).getType().equals("ctlg")
					&& domainDataListDB.get(i).getProtocol().equals(protocol)) {
				DomainData domainData = new DomainData();
				domainData.setId(domainDataListDB.get(i).getId());
				domainData.setDesc(domainDataListDB.get(i).getDesc());
				domainData.setType(domainDataListDB.get(i).getType());
				if (protocol.equals("computer"))
					domainData.setProtocol(domainDataListDB.get(i)
							.getProtocol());
				catalogListDB.add(domainData);
			}
		}

		return catalogListDB;
	}

	// server app type list generation from the total domain data
	public List<DomainData> getSrvrAppType() throws NoSuchElementException,
			ParseException {

		List<DomainData> domainDataListDB = new ArrayList<DomainData>();
		domainDataListDB = getDomainDataList(enterpriseId);

		List<DomainData> srvrAppListDB = new ArrayList<DomainData>();

		for (int i = 0; i < domainDataListDB.size(); i++) {
			if (domainDataListDB.get(i).getType().equals("srvr")) {
				DomainData domainData = new DomainData();
				domainData.setId(domainDataListDB.get(i).getId());
				domainData.setDesc(domainDataListDB.get(i).getDesc());
				domainData.setType(domainDataListDB.get(i).getType());
				srvrAppListDB.add(domainData);
			}
		}
		return srvrAppListDB;
	}

	// asset importance level list generation from the total domain data
	public List<DomainData> getAssetImpLevel() throws NoSuchElementException,
			ParseException {

		List<DomainData> domainDataListDB = new ArrayList<DomainData>();
		domainDataListDB = getDomainDataList(enterpriseId);

		List<DomainData> assetImpLevelListDB = new ArrayList<DomainData>();

		for (int i = 0; i < domainDataListDB.size(); i++) {
			if (domainDataListDB.get(i).getType().equals("impl")) {
				DomainData domainData = new DomainData();
				domainData.setId(domainDataListDB.get(i).getId());
				domainData.setDesc(domainDataListDB.get(i).getDesc());
				domainData.setType(domainDataListDB.get(i).getType());
				assetImpLevelListDB.add(domainData);
			}
		}
		return assetImpLevelListDB;
	}

	// asset importance level list generation from the total domain data
	public List<DomainData> getCompType() throws NoSuchElementException,
			ParseException {

		List<DomainData> domainDataListDB = new ArrayList<DomainData>();
		domainDataListDB = getDomainDataList(enterpriseId);

		List<DomainData> compTypeListDB = new ArrayList<DomainData>();

		for (int i = 0; i < domainDataListDB.size(); i++) {
			if (domainDataListDB.get(i).getType().equals("comp")) {
				DomainData domainData = new DomainData();
				domainData.setId(domainDataListDB.get(i).getId());
				domainData.setDesc(domainDataListDB.get(i).getDesc());
				domainData.setType(domainDataListDB.get(i).getType());
				compTypeListDB.add(domainData);
			}
		}
		return compTypeListDB;
	}

	// all masters required for the asset config screen from the service
	public List<DomainData> getDomainDataList(String entpId) {
		enterpriseId = entpId;
		Client client = Client.create();
		String url = "http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/mapsDomainServices/getMapsDomainData?enterpriseId="
				+ entpId;		
		//url = url + "mapsDomainServices/getMapsDomainData?enterpriseId=" + entpId;

		WebResource webResourceDomain = client.resource(url);

		String domainDataStr = webResourceDomain.get(String.class);

		JSONObject jsnobject = null;
		try {
			if (!domainDataStr.equals(null) || !domainDataStr.trim().equals(""))
				jsnobject = new JSONObject(domainDataStr);
		} catch (ParseException e) {

			System.out.println("\nNo domain data\n");
			GQMapsCustomerUIConstants.logger.error("Error loading domain data is:\t" + "and the domainDataStr is : " + domainDataStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GQMapsCustomerUIConstants.logger.info("Domain data is:\t" + domainDataStr);
		System.out.println("\nDomainData String: " + domainDataStr);

		List<DomainData> domainDataListDB = new ArrayList<DomainData>();

		JSONArray jsonCtlgArray = jsnobject.getJSONArray("devCtlgResult");
		System.out.println("Catalog length: " + jsonCtlgArray.length());

		// adding catalog details to the domain data object
		for (int i = 0; i < jsonCtlgArray.length(); i++) {

			DomainData ctlg = new DomainData();

			if (jsonCtlgArray.getJSONObject(i).has("ctlgId"))
				ctlg.setId(jsonCtlgArray.getJSONObject(i).getString("ctlgId"));

			if (jsonCtlgArray.getJSONObject(i).has("descr"))
				ctlg.setDesc(jsonCtlgArray.getJSONObject(i).getString("descr"));

			if (jsonCtlgArray.getJSONObject(i).has("protocolId"))
				ctlg.setProtocol(jsonCtlgArray.getJSONObject(i).getString(
						"protocolId"));

			ctlg.setType("ctlg");
			domainDataListDB.add(ctlg);
		}

		JSONArray jsonSrvrArray = jsnobject.getJSONArray("srvrAppType");
		System.out.println("Server length: " + jsonSrvrArray.length());

		// adding server type details to the domain data object
		for (int j = 0; j < jsonSrvrArray.length(); j++) {

			DomainData srvr = new DomainData();

			if (jsonSrvrArray.getJSONObject(j).has("srvrAppId"))
				srvr.setId(jsonSrvrArray.getJSONObject(j)
						.getString("srvrAppId"));

			if (jsonSrvrArray.getJSONObject(j).has("descr")) {
				srvr.setDesc(jsonSrvrArray.getJSONObject(j).getString("descr"));
			}

			srvr.setType("srvr");

			domainDataListDB.add(srvr);
		}

		JSONArray jsonCompArray = jsnobject.getJSONArray("compType");
		System.out.println("Comp type length: " + jsonCompArray.length());

		// adding computer type details to the domain data object
		for (int k = 0; k < jsonCompArray.length(); k++) {

			DomainData comp = new DomainData();

			if (jsonCompArray.getJSONObject(k).has("typeId")) {
				comp.setId(jsonCompArray.getJSONObject(k).getString("typeId"));
			}

			if (jsonCompArray.getJSONObject(k).has("descr")) {
				comp.setDesc(jsonCompArray.getJSONObject(k).getString("descr"));
			}
			comp.setType("comp");
			domainDataListDB.add(comp);
		}

		JSONArray jsonImplArray = jsnobject.getJSONArray("assetImp");
		System.out.println("Impl length: " + jsonImplArray.length());

		// adding asset level details to the domain data object
		for (int l = 0; l < jsonImplArray.length(); l++) {

			DomainData impl = new DomainData();

			if (jsonImplArray.getJSONObject(l).has("impLvl")) {
				impl.setId(jsonImplArray.getJSONObject(l).getString("impLvl"));
			}

			if (jsonImplArray.getJSONObject(l).has("descr")) {
				impl.setDesc(jsonImplArray.getJSONObject(l).getString("descr"));
			}
			impl.setType("impl");
			domainDataListDB.add(impl);
		}

		return domainDataListDB;
	}

	public List<Asset> getAssetList(String entpId) {
		
		enterpriseId = entpId;
		Client client = Client.create();

		String url = "http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/AssetEditServices/getAssetData?enterpriseId="	+ entpId;
		//url = url + "AssetEditServices/getAssetData?enterpriseId=" + entpId;
		
		System.out.println("URL::::::::::::::::::: " + url);
		WebResource webResourceAsset = client.resource(url);

		String assetStr = webResourceAsset.get(String.class);
		


		JSONObject jsnobject = null;
		try {
			if (!assetStr.equals(null) || !assetStr.trim().equals(""))
				jsnobject = new JSONObject(assetStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			GQMapsCustomerUIConstants.logger.error("Error loading asset data:\t" + "and the assetStr is : " + assetStr);			
			e.printStackTrace();
		}
		
		GQMapsCustomerUIConstants.logger.info("Asset data is:\t" + assetStr);
		System.out.println("\nAssetData String: " + assetStr);
		
		JSONArray jsonAssetArray = jsnobject.getJSONArray("assetResult");

		List<Asset> assetListDB = new ArrayList<Asset>();

		for (int i = 0; i < jsonAssetArray.length(); i++) {

			Asset asset = gson.fromJson(jsonAssetArray.getJSONObject(i)
					.toString(), Asset.class);

			// setting the default values if null for catalog id, server app id,
			// importance level, ownership, dc/entp, and type id

			if (asset.getIpAddr() == null)
				asset.setIpAddr("0.0.0.0");
			if (asset.getCtlgId() == null
					&& asset.getProtocolId().equals("computer"))
				asset.setCtlgId("default_computer");
			else if (asset.getCtlgId() == null
					&& asset.getProtocolId().equals("printer"))
				asset.setCtlgId("default_printer");
			else if (asset.getCtlgId() == null
					&& asset.getProtocolId().equals("nsrg"))
				asset.setCtlgId("default_nsrg");

			if (asset.getSrvrAppId() == null)
				asset.setSrvrAppId((short) 4);

			if (asset.getOwnership() == null)
				asset.setOwnership("Own");

			if (asset.getDcEnt() == null)
				asset.setDcEnt("DC");

			if (asset.getType_id() == null
					&& asset.getProtocolId().equals("computer"))
				asset.setType_id("server");

			assetListDB.add(asset);
		}

		System.out.println("Length of assetListDB is : " + assetListDB.size());

		return assetListDB;
	}
}
