package com.gq.cust;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
	static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	// this is for the goal screen
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
		
		if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
	 
		String svcOutput = response.getEntity(String.class);
		gm = gson.fromJson(svcOutput , GoalMaster.class);
	}

	// this is for the goal snapshot screen
	public GoalHelper( String snapshotId ) {
		
		Client client = Client.create();
		WebResource service;
		ClientResponse response;
		
		service = client.resource("http://"+GQMapsCustomerUIConstants.webSvcHost+":8080/GQMapsCustomerServices/");

		//http://192.168.8.15:8080/GQMapsCustomerServices/getGoalSnapshot/goalSnapshot?snapShtId=17
			
		response = service.path("getGoalSnapshot").path("goalSnapshot")
								.queryParam("snapShtId",snapshotId )
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		else {
			GQMapsCustomerUIConstants.logger.debug(" Successful response from the client");
		}
		
		String svcOutput = response.getEntity(String.class);
		gm = gson.fromJson(svcOutput , GoalMaster.class);
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
	
		return gm.getTemplateTaskDetails();
	}


}
