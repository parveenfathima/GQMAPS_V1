package com.gq.meter.object;

// Generated Mar 1, 2013 12:56:53 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * EnterpriseMeter generated by hbm2java
 */
public class EnterpriseMeter implements java.io.Serializable {

    private String meterId;
    private String protocolId;
    private String enterpriseId;
    private String descr;
    private String address;
    private String phone;
    private Date creDttm;
    private double latitude;
    private double longitude;

    private long pcount;

    public EnterpriseMeter() {
    }

    public EnterpriseMeter(String meterId, String protocolId, String enterpriseId, String descr, String address,
            String phone, double latitude, double longitude, Date creDttm, long pcount) {
        this.meterId = meterId;
        this.protocolId = protocolId;
        this.enterpriseId = enterpriseId;
        this.descr = descr;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creDttm = creDttm;
        this.setPcount(pcount);
    }

    public String getMeterId() {
        return this.meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getProtocolId() {
        return this.protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getEnterpriseId() {
        return this.enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreDttm() {
        return this.creDttm;
    }

    public void setCreDttm(Date creDttm) {
        this.creDttm = creDttm;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getPcount() {
        return pcount;
    }

    public void setPcount(long pcount) {
        this.pcount = pcount;
    }

}
