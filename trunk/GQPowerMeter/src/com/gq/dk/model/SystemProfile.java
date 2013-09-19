package com.gq.dk.model;


/* Model class for System Profile details */
public class SystemProfile{
	
	String keycol;
	String value;
	
	//Getters and Setters for System Profile 

	public String getValue() {
		return value;
	}
	public String getKeycol() {
		return keycol;
	}
	public void setKeycol(String keycol) {
		this.keycol = keycol;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "SystemProfile [keycol=" + keycol + ", value=" + value + "]";
	}

}
