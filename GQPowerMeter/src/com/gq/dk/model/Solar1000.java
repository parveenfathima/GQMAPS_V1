package com.gq.dk.model;

import java.util.Date;

/* Model class for insertion of Solar1000 data from device input */
public class Solar1000
{
	// for hibernate
	long sid;  // id of the table 
	// device attributes
	int unitId ;
	Date recordingDttm;
	double panelVoltage1;
	double panelVoltage2;
	double batteryVoltage;
	double dgVoltage;
	double ebRVoltage;
	double ebYVoltage;
	double ebBVoltage;
	double solarChargeCurrent1;
	double solarChargeCurrent2;
	double btsChargeCurrent;
	double dischargeCurrent;
	double batteryAhPercent;
	double sunlight;
	double batteryTemperature;
	double batteryAh;
	double fuel;
	int enterpriseId;

	// constructor
	public Solar1000(int unitId, Date recordingDttm,
			double panelVoltage1, double panelVoltage2, double batteryVoltage,
			double dgVoltage, double ebRVoltage, double ebYVoltage,
			double ebBVoltage, double solarChargeCurrent1,
			double solarChargeCurrent2, double btsChargeCurrent,
			double dischargeCurrent, double batteryAhPercent, double sunlight,
			double batteryTemperature, double batteryAh, double fuel,int enterpriseId) {
		super();
		this.unitId = unitId;
		this.recordingDttm = recordingDttm;
		this.panelVoltage1 = panelVoltage1;
		this.panelVoltage2 = panelVoltage2;
		this.batteryVoltage = batteryVoltage;
		this.dgVoltage = dgVoltage;
		this.ebRVoltage = ebRVoltage;
		this.ebYVoltage = ebYVoltage;
		this.ebBVoltage = ebBVoltage;
		this.solarChargeCurrent1 = solarChargeCurrent1;
		this.solarChargeCurrent2 = solarChargeCurrent2;
		this.btsChargeCurrent = btsChargeCurrent;
		this.dischargeCurrent = dischargeCurrent;
		this.batteryAhPercent = batteryAhPercent;
		this.sunlight = sunlight;
		this.batteryTemperature = batteryTemperature;
		this.batteryAh = batteryAh;
		this.fuel = fuel;
		this.enterpriseId=enterpriseId;
	}

	@Override
	public String toString() {
		return "GQDKDeviceAttributes [unitId=" + unitId + ", recordingDttm="
				+ recordingDttm + ", panelVoltage1=" + panelVoltage1
				+ ", panelVoltage2=" + panelVoltage2 + ", batteryVoltage="
				+ batteryVoltage + ", dgVoltage=" + dgVoltage + ", ebRVoltage="
				+ ebRVoltage + ", ebYVoltage=" + ebYVoltage + ", ebBVoltage="
				+ ebBVoltage + ", solarChargeCurrent1=" + solarChargeCurrent1
				+ ", solarChargeCurrent2=" + solarChargeCurrent2
				+ ", btsChargeCurrent=" + btsChargeCurrent
				+ ", dischargeCurrent=" + dischargeCurrent
				+ ", batteryAhPercent=" + batteryAhPercent + ", sunlight="
				+ sunlight + ", batteryTemperature=" + batteryTemperature
				+ ", batteryAh=" + batteryAh + ", fuel=" + fuel + ", enterpriseId=" + enterpriseId +"]";
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public Date getRecordingDttm() {
		return recordingDttm;
	}

	public void setRecordingDttm(Date recordingDttm) {
		this.recordingDttm = recordingDttm;
	}

	public double getPanelVoltage1() {
		return panelVoltage1;
	}

	public void setPanelVoltage1(double panelVoltage1) {
		this.panelVoltage1 = panelVoltage1;
	}

	public double getPanelVoltage2() {
		return panelVoltage2;
	}

	public void setPanelVoltage2(double panelVoltage2) {
		this.panelVoltage2 = panelVoltage2;
	}

	public double getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(double batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public double getDgVoltage() {
		return dgVoltage;
	}

	public void setDgVoltage(double dgVoltage) {
		this.dgVoltage = dgVoltage;
	}

	public double getEbRVoltage() {
		return ebRVoltage;
	}

	public void setEbRVoltage(double ebRVoltage) {
		this.ebRVoltage = ebRVoltage;
	}

	public double getEbYVoltage() {
		return ebYVoltage;
	}

	public void setEbYVoltage(double ebYVoltage) {
		this.ebYVoltage = ebYVoltage;
	}

	public double getEbBVoltage() {
		return ebBVoltage;
	}

	public void setEbBVoltage(double ebBVoltage) {
		this.ebBVoltage = ebBVoltage;
	}

	public double getSolarChargeCurrent1() {
		return solarChargeCurrent1;
	}

	public void setSolarChargeCurrent1(double solarChargeCurrent1) {
		this.solarChargeCurrent1 = solarChargeCurrent1;
	}

	public double getSolarChargeCurrent2() {
		return solarChargeCurrent2;
	}

	public void setSolarChargeCurrent2(double solarChargeCurrent2) {
		this.solarChargeCurrent2 = solarChargeCurrent2;
	}

	public double getBtsChargeCurrent() {
		return btsChargeCurrent;
	}

	public void setBtsChargeCurrent(double btsChargeCurrent) {
		this.btsChargeCurrent = btsChargeCurrent;
	}

	public double getDischargeCurrent() {
		return dischargeCurrent;
	}

	public void setDischargeCurrent(double dischargeCurrent) {
		this.dischargeCurrent = dischargeCurrent;
	}

	public double getBatteryAhPercent() {
		return batteryAhPercent;
	}

	public void setBatteryAhPercent(double batteryAhPercent) {
		this.batteryAhPercent = batteryAhPercent;
	}

	public double getSunlight() {
		return sunlight;
	}

	public void setSunlight(double sunlight) {
		this.sunlight = sunlight;
	}

	public double getBatteryTemperature() {
		return batteryTemperature;
	}

	public void setBatteryTemperature(double batteryTemperature) {
		this.batteryTemperature = batteryTemperature;
	}

	public double getBatteryAh() {
		return batteryAh;
	}

	public void setBatteryAh(double batteryAh) {
		this.batteryAh = batteryAh;
	}

	public double getFuel() {
		return fuel;
	}

	public void setFuel(double fuel) {
		this.fuel = fuel;
	}
	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
		 
} // class ends
	