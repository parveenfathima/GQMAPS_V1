package com.gq;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.gq.meter.object.Asset;
import com.gq.ui.object.DomainData;

public class AssetData {
	Client client = Client.create();
	WebResource webResourceDomain = client
			.resource("http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/mapsDomainServices/getMapsDomainData");

	String domainDataStr = webResourceDomain.get(String.class);

	public AssetData() {
	}

	public List<DomainData> getCatalogList(String protocol) throws NoSuchElementException,
			ParseException 
	{
			List<DomainData> domainDataListDB = new ArrayList<DomainData>();
			domainDataListDB = getDomainDataList();

			List<DomainData> catalogListDB = new ArrayList<DomainData>();
			
				for(int i = 0; i < domainDataListDB.size(); i++)
				{	
					if(domainDataListDB.get(i).getType().equals(protocol))
					{
						DomainData domainData = new DomainData();
						domainData.setId(domainDataListDB.get(i).getId());
						domainData.setDesc(domainDataListDB.get(i).getDesc());
						domainData.setType(domainDataListDB.get(i).getType());
						catalogListDB.add(domainData);
					}
				}
			
			for(int cc=0; cc<catalogListDB.size(); cc++)
			{
				System.out.println("Catalog for " + protocol + ": "+ catalogListDB.get(cc).toString());
			}
			
			return catalogListDB;
	}

	public List<DomainData> getServerTypeList() throws NoSuchElementException,
			ParseException {
		
		List<DomainData> srvrTypeList = new ArrayList<DomainData>();
		JSONObject jsnobject = new JSONObject(domainDataStr);
		
		JSONArray jsonSrvrArray = jsnobject.getJSONArray("srvrAppType");

		// adding server type details to the domain data object
		for (int j = 0; j < jsonSrvrArray.length(); j++) {

			DomainData srvr = new DomainData();

			if (jsonSrvrArray.getJSONObject(j).has("srvrAppId")) {
				srvr.setId(jsonSrvrArray.getJSONObject(j)
						.getString("srvrAppId"));
			}

			if (jsonSrvrArray.getJSONObject(j).has("descr")) {
				srvr.setDesc(jsonSrvrArray.getJSONObject(j).getString("descr"));
			}

			srvr.setType("srvr");
			
			srvrTypeList.add(srvr);
		}

		return srvrTypeList;

	}

	public List<DomainData> getDomainDataList() throws NoSuchElementException,
			ParseException {

		System.out.println("\nDomainData String: " + domainDataStr);

		List<DomainData> domainDataListDB = new ArrayList<DomainData>();

		JSONObject jsnobject = new JSONObject(domainDataStr);

		JSONArray jsonCtlgArray = jsnobject.getJSONArray("devCtlgResult");
		System.out.println("Catalog length: " + jsonCtlgArray.length());

		// adding catalog details to the domain data object
		for (int i = 0; i < jsonCtlgArray.length(); i++) {

			DomainData ctlg = new DomainData();

			if (jsonCtlgArray.getJSONObject(i).has("ctlgId")) 				
				ctlg.setId(jsonCtlgArray.getJSONObject(i).getString("ctlgId"));
				else if(jsonCtlgArray.getJSONObject(i).has("protocolId") && jsonCtlgArray.getJSONObject(i).getString("protocolId").equals("computer"))
					ctlg.setId("default_computer");
				else if(jsonCtlgArray.getJSONObject(i).has("protocolId") && jsonCtlgArray.getJSONObject(i).getString("protocolId").equals("printer"))
					ctlg.setId("default_printer");
				else if(jsonCtlgArray.getJSONObject(i).has("protocolId") && jsonCtlgArray.getJSONObject(i).getString("protocolId").equals("nsrg"))
					ctlg.setId("default_nsrg");

				if (jsonCtlgArray.getJSONObject(i).has("descr")) {
					ctlg.setDesc(jsonCtlgArray.getJSONObject(i).getString(
							"descr"));

					
				}
				ctlg.setType("ctlg");
				domainDataListDB.add(ctlg);
			}
		}
		JSONObject jsnobject = new JSONObject(domainDataStr);
		JSONArray jsonSrvrArray = jsnobject.getJSONArray("srvrAppType");
		System.out.println("Server length: " + jsonSrvrArray.length());

		// adding server type details to the domain data object
		for (int j = 0; j < jsonSrvrArray.length(); j++) {

			DomainData srvr = new DomainData();

			if (jsonSrvrArray.getJSONObject(j).has("srvrAppId")) {
				srvr.setId(jsonSrvrArray.getJSONObject(j)
						.getString("srvrAppId"));
			}

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

		/*
		 * System.out.println("Domain length: " + domainDataListDB.size()); for
		 * (int i = 0; i < domainDataListDB.size(); i++)
		 * System.out.println(domainDataListDB.get(i).toString());
		 */

		return domainDataListDB;
	}

	public List<Asset> getAssetList() throws NoSuchElementException,
			ParseException {

		WebResource webResourceAsset = client
				.resource("http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/getAssetServices/getAssetData");
		ClientResponse response = webResourceAsset.accept("application/json")
				.get(ClientResponse.class);

		String assetStr = webResourceAsset.get(String.class);

		System.out.println("\nAssetData String: " + assetStr);

		JSONObject jsnobject = new JSONObject(assetStr);
		JSONArray jsonAssetArray = jsnobject.getJSONArray("assetResult");
		List<Asset> assetListDB = new ArrayList<Asset>();

		for (int i = 0; i < jsonAssetArray.length(); i++) {

			Asset a = new Asset();

			if (jsonAssetArray.getJSONObject(i).has("assetId")) {
				a.setAssetId(jsonAssetArray.getJSONObject(i).getString(
						"assetId"));
			}

			if (jsonAssetArray.getJSONObject(i).has("protocolId")) {
				a.setProtocolId(jsonAssetArray.getJSONObject(i).getString(
						"protocolId"));
			}

			if (jsonAssetArray.getJSONObject(i).has("active")) {
				a.setActive(jsonAssetArray.getJSONObject(i).getString("active")
						.charAt(0));
			}

			if (jsonAssetArray.getJSONObject(i).has("ctlgId")) 
				a.setCtlgId(jsonAssetArray.getJSONObject(i).getString("ctlgId"));
			else if(jsonAssetArray.getJSONObject(i).getString("protocolId").equals("computer"))
				a.setCtlgId("default_computer");
			else if(jsonAssetArray.getJSONObject(i).getString("protocolId").equals("printer"))
				a.setCtlgId("default_printer");
			else if(jsonAssetArray.getJSONObject(i).getString("protocolId").equals("nsrg"))
				a.setCtlgId("default_nsrg");
			

			if (jsonAssetArray.getJSONObject(i).has("name")) {
				a.setName(jsonAssetArray.getJSONObject(i).getString("name"));
			} else
				a.setName(" ");

			if (jsonAssetArray.getJSONObject(i).has("descr")) {
				a.setDescr(jsonAssetArray.getJSONObject(i).getString("descr"));
			} else
				a.setDescr("novalue");

			if (jsonAssetArray.getJSONObject(i).has("contact")) {
				a.setContact(jsonAssetArray.getJSONObject(i).getString(
						"contact"));
			} else
				a.setContact("novalue");

			if (jsonAssetArray.getJSONObject(i).has("ipAddr")) {
				a.setIpAddr(jsonAssetArray.getJSONObject(i).getString("ipAddr"));
			}

			if (jsonAssetArray.getJSONObject(i).has("location")) {
				a.setLocation(jsonAssetArray.getJSONObject(i).getString(
						"location"));
			} else
				a.setLocation("novalue");

			if (jsonAssetArray.getJSONObject(i).has("srvrAppId")) {
				a.setSrvrAppId(Short.valueOf(jsonAssetArray.getJSONObject(i)
						.getString("srvrAppId")));
			} else
				a.setSrvrAppId((short) 4);

			if (jsonAssetArray.getJSONObject(i).has("assetUsg")) {
				a.setAssetUsg(jsonAssetArray.getJSONObject(i).getString(
						"assetUsg"));
			} else
				a.setAssetUsg("novalue");

			if (jsonAssetArray.getJSONObject(i).has("impLvl")) {
				a.setImpLvl(Byte.valueOf(jsonAssetArray.getJSONObject(i)
						.getString("impLvl")));
			} else
				a.setImpLvl((byte) 3);

			if (jsonAssetArray.getJSONObject(i).has("ownership")) {
				a.setOwnership(jsonAssetArray.getJSONObject(i).getString(
						"ownership"));
			} else
				a.setOwnership("Own");

			if (jsonAssetArray.getJSONObject(i).has("dcEnt")) {
				a.setDcEnt(jsonAssetArray.getJSONObject(i).getString("dcEnt"));
			} else
				a.setDcEnt("DC");

			if (jsonAssetArray.getJSONObject(i).has("inactiveDttm")) {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
				Date dt = sdf.parse(jsonAssetArray.getJSONObject(i).getString(
						"inactiveDttm"));
				a.setInactiveDttm(dt);
			}

			if (jsonAssetArray.getJSONObject(i).has("typeId")) {
				a.setTypeId(jsonAssetArray.getJSONObject(i).getString("typeId"));
			}

			assetListDB.add(a);
		}

		// System.out.println("Asset Array length: " + assetListDB.size());
		//
		// for (int i = 0; i < assetListDB.size(); i++) {
		// System.out.println("\n" + i + " " + assetListDB.get(i).getAssetId()
		// + ", " + assetListDB.get(i).getProtocolId() + ", "
		// + assetListDB.get(i).getActive() + ", "
		// + assetListDB.get(i).getName() + ", "
		// + assetListDB.get(i).getDescr() + ", "
		// + assetListDB.get(i).getContact() + ", "
		// + assetListDB.get(i).getIpAddr() + ", "
		// + assetListDB.get(i).getLocation() + ", "
		// + assetListDB.get(i).getSrvrAppId() + ", "
		// + assetListDB.get(i).getAssetUsg() + ", "
		// + assetListDB.get(i).getImp_level() + ", "
		// + assetListDB.get(i).getOwnership() + ", "
		// + assetListDB.get(i).getDcEnt());
		// }

		return assetListDB;
	}
}
