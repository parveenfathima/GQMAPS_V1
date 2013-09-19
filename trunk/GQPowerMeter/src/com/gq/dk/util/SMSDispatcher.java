package com.gq.dk.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public final class SMSDispatcher {

	private static String userId = "gquotient";
	private static String passwd = "gquotient";
	
	private static String destinationURL = "http://www.meru.co.in/wip/sendsms";
	
	public static void main(String[] args) throws Exception {
		//SMSDispatcher dis = new SMSDispatcher();
		//SMSDispatcher.sendUnitAlert_Template1(9677889239L, 36, "Thanjavur-kumbakonam", 41);
		SMSDispatcher.sendSystemAlert_Template2(9677889239L, 41);

	}

	// this message sends actual sms
	private static void sendSMS(long phoneNumber , String message) {
		
		try {
			 
			URL url = new URL( destinationURL );
			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setAllowUserInteraction(true);

			DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
			  
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");
			data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(passwd, "UTF-8");
			data += "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(Long.toString(phoneNumber), "UTF-8");
			data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
			
			GQDKPMConstants.logger.info("SMS outbound =<"+data+">");
			
			dos.writeBytes(data);
			dos.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			
			String inputLine;
			StringBuilder sb = new StringBuilder(2000);
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();
			
			if ( sb.toString().contains("API000")) {
				// message delivered successfully 
			}
			else {
				// could be one of the following
				// API096~Invalid Template/Template Data 
				// API204~Parameter Message contains NULL value
				// API200~Validate Request Failed - May be Server Error
				// API005~Invalid Login Credentials for [gquotient]
				System.out.println(sb.toString());
				GQDKPMConstants.logger.error(sb.toString());   // todo , just logging aint enough , need to do more.
			}
				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		 

	}
	
	// this is the unit level alert , it has a template taking the below arguments
	 
	// sample message 1
	// http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient&to=7598223055&message=Alert : there is no power at unit 5 in location thiruvaimur for the last 5 minutes

	public static void sendUnitAlert_Template1(long phoneNumber , int unitId , String location , int minutes) {
		
		String message = "Alert : there is no power at unit " + unitId + " in location " + location + " for the last " + minutes +" minutes";
		sendSMS(phoneNumber , message); 
		 
	} // method ends

	// this is the system level alert , it has a template taking the below arguments
	 
	// sample message 2
	// http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient&to=7598223055&message=Alert : an unknown error has been reported by the system for unit 78

	public static void sendSystemAlert_Template2(long phoneNumber , int unitId ) {
		
		String message = "Alert : an unknown error has been reported by the system for unit " + unitId ;
		// we want this to be 
		//String message = "Alert : error <V1> has been reported for client <V2> for unit <V3>"  ;
		
		sendSMS(phoneNumber , message); 
		 
	} // method ends

}



/*


http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient&to=7598223055&message=Alert : there is no power at unit 5 in location thiruvaimur for the last 5 minutes

http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient&to=7598223055&message=Alert : an unknown error has been reported by the system for unit 78

fe6620a2-c69a-4bdb-bc59-f7fad8976ee8~7598223055~02-08-2012 12:47:31~API000~Message Accepted for Delivery 


http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient&to=7598223055&message=sdjngldfkjgnsdjklgfnsdjkfnsdjkf

0ba85b23-6e5e-4203-b88d-897b70fb1ee7~7598223055~02-08-2012 12:58:38~API096~Invalid Template/Template Data 


http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient&to=7598223055&message=

null~7598223055~02-08-2012 13:02:46~API204~Parameter Message contains NULL value 

http://www.meru.co.in/wip/sendsms?username=gquotient&password=gquotient

null~null~02-08-2012 13:03:35~API200~Validate Request Failed - May be Server Error 

http://www.meru.co.in/wip/sendsms?username=gquotient&password=g32quotient&to=7598223055&message=Alert : there is no power at unit 5 in location thiruvaimur for the last 5 minutes

null~7598223055~02-08-2012 13:04:26~API005~Invalid Login Credentials for [gquotient]

*/
