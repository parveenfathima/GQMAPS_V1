package com.gq.dk.model;

/* Model class for Enterprise details */
public class Enterprise {
	
	int enterpriseId;
	String enterpriseName;
	String adminName;
	long mobileno;
	String email;
	int port;
    double centerLatitude;
    double centerLongitude;
	int zoomLevel;
	int screenRefreshTimeout;
	int firstEscMinutes;
	int secondEscMinutes;
	

	//Getters and setters for Enterprise
	public int getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public double getCenterLatitude() {
		return centerLatitude;
	}

	public void setCenterLatitude(double centerLatitude) {
		this.centerLatitude = centerLatitude;
	}

	public double getCenterLongitude() {
		return centerLongitude;
	}

	public void setCenterLongitude(double centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getScreenRefreshTimeout() {
		return screenRefreshTimeout;
	}

	public void setScreenRefreshTimeout(int screenRefreshTimeout) {
		this.screenRefreshTimeout = screenRefreshTimeout;
	}

	public int getFirstEscMinutes() {
		return firstEscMinutes;
	}

	public void setFirstEscMinutes(int firstEscMinutes) {
		this.firstEscMinutes = firstEscMinutes;
	}

	public int getSecondEscMinutes() {
		return secondEscMinutes;
	}

	public void setSecondEscMinutes(int secondEscMinutes) {
		this.secondEscMinutes = secondEscMinutes;
	}
		
}