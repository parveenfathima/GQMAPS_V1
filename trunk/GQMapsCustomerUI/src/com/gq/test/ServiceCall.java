package com.gq.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ServiceCall {

		  public static void main(String[] args) {
			try {
		 
				Client client = Client.create();
				WebResource service;
				
				 service = client.resource("http://192.168.8.15:8080/GQMapsCustomerServices/");
		
				ClientResponse response = service.path("goalServices").path("goal")
				.queryParam("goalId", "mon").queryParam("entpId", "aps")
				.queryParam("goalInputs","__date__=2013-10-16~__asset_id__=C-000c293c937a")
				.get(ClientResponse.class);
				System.out.println("resp:::"+response);
				
				if (response.getStatus() != 200) {
				   throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
				}
		 
				String output = response.getEntity(String.class);
		 
				System.out.println("Output from Server .... \n");
				System.out.println(output);
		 
			  } catch (Exception e) {
		 
				e.printStackTrace();
		 
			  }
		 
			}
//    service = client.resource("http://192.168.8.15:8080/GQMapsCustomerServices/");
//	ClientResponse response = service.path("goalServices").path("goal").queryParam("goalId", "mon").queryParam("entpId", "servion").queryParam("goalInputs __date__","=2013-10-16~").queryParam("__asset_id__", "C-000c293c937a").post(ClientResponse.class);
//    System.out.println("");
//    String resp = response.getEntity(String.class).trim();

}
