package com.gq.cust;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gson.*;

import com.gq.meter.xchange.object.Goal;
import com.gq.meter.xchange.object.GoalMaster;
import com.gq.meter.xchange.object.GoalSnpsht;
import com.gq.meter.xchange.object.TemplateTaskDetails;
import com.gq.util.GQMapsCustomerUIConstants;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GoalHelper {

	GoalMaster gm;
	
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss").create();

	public GoalHelper(String enterpriseId , String goalId, String goalInputs) {
		
		Client client = Client.create();
		WebResource service;
		ClientResponse response;
		
		service = client.resource("http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/");

		if ( goalInputs != null ) { // for goals which dont have no goal inputs set
			goalInputs = goalInputs + "~__ent_id__=" + enterpriseId;
		}
		else {
			goalInputs = "__ent_id__=" + enterpriseId;
		} 
		
		response = service.path("goalServices").path("goal")
								.queryParam("goalId",goalId)
								.queryParam("entpId", enterpriseId)
								.queryParam("goalInputs",goalInputs)
								.get(ClientResponse.class);
		
		System.out.println("ks enterpriseId = <" +enterpriseId+ "> , uri = <"+response+">");
		
		if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
	 
		String svcOutput = response.getEntity(String.class);
		System.out.println("kb jsaon resp:::"+svcOutput);
		
		gm = gson.fromJson(svcOutput , GoalMaster.class);
		
		//------------------
//		Client client = Client.create();
//
//		WebResource webResourceAsset = client
//				.resource("http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/goalServices/goal?goalId="
//							+ goalId + "&entpId=" + enterpriseId+"&goalInputs=__ent__=" + enterpriseId );
//
//		//ClientResponse response = webResourceAsset.accept("application/json").get(ClientResponse.class);
//
//		String jsonString = webResourceAsset.get(String.class);

		//System.out.println("AssetData String: " + jsonString);

//		gm = gson.fromJson(response. , GoalMaster.class);

	}

	public List<GoalSnpsht> getGoalSnapshot() throws NoSuchElementException,
			ParseException {

		return gm.getGoalSnpshtList();
	}

	public Goal getGoal() {

		return gm.getGoal();
	}

	public List<TemplateTaskDetails> getTemplateTaskDetails() throws NoSuchElementException,
	ParseException {
//		System.out.println("num gcd records = "+gm.getTemplateTaskDetails().size());
//		
//		for (int i = 0; i < gm.getTemplateTaskDetails().size(); i++) {
//			
//			System.out.println("gcd.java.type = "+gm.getTemplateTaskDetails().get(i).getChartType());
//			if ( gm.getTemplateTaskDetails().get(i).getChartType().equals("line")) {
//				System.out.println("gcd.java for line before = "+gm.getTemplateTaskDetails().get(i).getChartData());
//				
//				System.out.println("gcd.java for line after = "+gm.getTemplateTaskDetails().get(i).getChartData().replaceFirst("string", "datetime"));
//			}
//		}
		
		return gm.getTemplateTaskDetails();
	}


}
