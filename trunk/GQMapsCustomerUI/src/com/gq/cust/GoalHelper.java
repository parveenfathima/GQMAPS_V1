package com.gq.cust;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gson.*;
import com.gq.meter.object.Asset;
import com.gq.meter.object.DevCtlg;
import com.gq.meter.xchange.object.Goal;
import com.gq.meter.xchange.object.GoalMaster;
import com.gq.meter.xchange.object.GoalSnpsht;
import com.gq.meter.xchange.object.TaskTmplt;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GoalHelper {

	GoalMaster gm;
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss")
			.create();

	public GoalHelper(String enterpriseId , String goalId) {

		Client client = Client.create();

		WebResource webResourceAsset = client
				.resource("http://192.168.8.15:8080/GQMapsCustomerServices/goalServices/goal?goalId="
							+ goalId + "&entpId=" + enterpriseId);

		ClientResponse response = webResourceAsset.accept("application/json").get(ClientResponse.class);

		String jsonString = webResourceAsset.get(String.class);

		System.out.println("AssetData String: " + jsonString);

		gm = gson.fromJson(jsonString, GoalMaster.class);

	}

	public List<GoalSnpsht> getGoalSnapshot() throws NoSuchElementException,
			ParseException {

		return gm.getGoalSnpshtList();
	}

	public Goal getGoal() {

		return gm.getGoal();
	}

	// method ends

	public List<TaskTmplt> getTaskTmplt() throws NoSuchElementException,
			ParseException {

		return gm.getTaskList();
	}

}
