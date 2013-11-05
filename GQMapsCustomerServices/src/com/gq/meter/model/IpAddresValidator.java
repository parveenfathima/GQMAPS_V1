/**
 * 
 */
package com.gq.meter.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rathish
 *
 */
public class IpAddresValidator {
	
	public static String validateIpAddress(String hostName) {

		String host="";
		String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(hostName);
   	
   	  if (matcher.find()) {
   	   	host=hostName;//returns the unresolved ipaddress
   	    return matcher.group();
   	  }else{ 
   	    String[] hostsplit = hostName.split("\\.");
   	       
   	    //to trim the unwanted characters and to display a valid domain names like google.co.in..
   	    if(hostsplit.length >= 3){
 	      host = hostsplit[hostsplit.length-3]+"."+hostsplit[hostsplit.length-2]+"."+hostsplit[hostsplit.length-1];
   	    }else{// to display a completely qualified domain names
    	  host = hostsplit[hostsplit.length-2]+"."+hostsplit[hostsplit.length-1];
   	    }
   	 }
   	   return host;
	}
}
