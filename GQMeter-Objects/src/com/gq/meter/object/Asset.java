package com.gq.meter.object;

// Generated Feb 18, 2013 12:23:57 PM by Hibernate Tools 3.4.0.CR1

/**
 * Asset generated by hbm2java
 */
public class Asset implements java.io.Serializable {

    private String assetId;
    private String protocolId;
    private String name;
    private String descr;
    private String contact;
    private String location;
    private String appId;
    private String assetUsg;
    private Byte assetStrength;
    private String ctlgId;

    public Asset() {
    }

    public Asset(String assetId, String protocolId) {
        super();
        this.assetId = assetId;
        this.protocolId = protocolId;
    }

    public Asset(String assetId, String protocolId, String name, String descr, String contact, String location,
            String appId, String assetUsg, Byte assetStrength, String ctlgId) {
        this.assetId = assetId;
        this.protocolId = protocolId;
        this.name = name;
        this.descr = descr;
        this.contact = contact;
        this.location = location;
        this.appId = appId;
        this.assetUsg = assetUsg;
        this.assetStrength = assetStrength;
        this.ctlgId = ctlgId;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getProtocolId() {
        return this.protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAssetUsg() {
        return this.assetUsg;
    }

    public void setAssetUsg(String assetUsg) {
        this.assetUsg = assetUsg;
    }

    public Byte getAssetStrength() {
        return this.assetStrength;
    }

    public void setAssetStrength(Byte assetStrength) {
        this.assetStrength = assetStrength;
    }

    public String getCtlgId() {
        return this.ctlgId;
    }

    public void setCtlgId(String ctlgId) {
        this.ctlgId = ctlgId;
    }

}
