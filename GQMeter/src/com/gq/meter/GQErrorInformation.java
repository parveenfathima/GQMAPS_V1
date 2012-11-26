package com.gq.meter;

import java.util.List;

public class GQErrorInformation {

	// mostly system description or whatever that makes sense.
	String assetDescr;

	// list holds the error messages
	List<String> errorList;

	public GQErrorInformation(String assetDescr, List<String> errorList) {
		super();
		this.assetDescr = assetDescr;
		this.errorList = errorList;
	}
	
}
