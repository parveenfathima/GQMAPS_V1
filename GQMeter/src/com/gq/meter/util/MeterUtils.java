package com.gq.meter.util;

import java.util.LinkedList;

public class MeterUtils {

	private static final String nextIpAddress(final String input) {
	    final String[] tokens = input.split("\\.");
	    if (tokens.length != 4)
	        throw new IllegalArgumentException();
	    
	    for (int i = tokens.length - 1; i >= 0; i--) {
	        final int item = Integer.parseInt(tokens[i]);
	        if (item < 255) {
	            tokens[i] = String.valueOf(item + 1);
	            for (int j = i + 1; j < 4; j++) {
	                tokens[j] = "0";
	            }
	            break;
	        }
	    }
	    
	    return new StringBuilder().append(tokens[0]).append('.').append(tokens[1]).append('.')
	    		.append(tokens[2]).append('.').append(tokens[3]) .toString();
	}

	public LinkedList<String> getIpAddressesInInclusiveRange(String ipAddrLowerBound , String ipAddrUpperBound) {

		LinkedList<String> ipList = new LinkedList<String>();
		
		ipList.add(ipAddrLowerBound);
		
		String currIp = ipAddrLowerBound;

		while(! (currIp = nextIpAddress(currIp)).equals(ipAddrUpperBound)) {
			ipList.add(currIp);
		}
		
		ipList.add(ipAddrUpperBound);
		
		return ipList;

	} // getIpAddressesInInclusiveRange ends
	
} // class ends
