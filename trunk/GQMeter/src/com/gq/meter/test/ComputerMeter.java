package com.gq.meter.test;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public  class ComputerMeter {

	// implement a power meter , this involves 3 distinct steps
	// 1.gather data to make the power protocol object
	// 2.make the json 
	// 3.make a https connection to the gate keeper url to send it to the server
	
	public void implement(Object o) {
		// use the following for pretty print.. atleast for testing
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// use the following for normal print.
		// Gson gson = new Gson();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(o);
		System.out.println("===== json is ======");
		System.out.println(json);

	}
	
	public static void main(String[] args) throws IOException {

		/*		Computer c = new Computer("sdfsad", 83, 100, 80, 30, 20, 500, 340, 89330, 7, 48, 10000, 22220, 2.34,
				"name", "192.149.4.4", "desc", "contact", "location", "extras", new Date());
		
		Gson gson = new GsonBuilder().create();

		GQMeterData pd = new GQMeterData("ss", new Date() ,"power" , "v1", "sstatis", "no comment dude" , gson.toJson(c));
		
		ComputerMeter cm = new ComputerMeter();
		
		cm.implement(pd);
		
		String json1 = gson.toJson(pd);
		
		GQMeterData pd1 = gson.fromJson(json1, GQMeterData.class);
		
		System.out.println("===== unmarshalled pd comment is ===== " + pd1.getComment());
		
		Computer c1 = gson.fromJson(pd1.getData(), Computer.class);
		
		System.out.println("===== unmarshalled computer ip is ===== "+ c1.getSysIP());
	
	*/
	}

}
