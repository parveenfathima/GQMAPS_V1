package com.gq.meter.object;

// default package
// Generated Aug 1, 2013 3:46:57 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

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
    private String ctlgId;
    private Short srvrAppId;
    private String assetUsg;
    private Byte impLvl;
    private String ownership;
    private String dcEnt;
    private Character active;
    private Date inactiveDttm;
    private String typeId;

    public Asset() {
    }

    public Asset(String assetId, String protocolId, String name, String descr, String contact, String location,
            String ctlgId, Short srvrAppId, String assetUsg, Byte impLvl, String ownership, String dcEnt,
            Character active, Date inactiveDttm, String typeId) {
        super();
        this.assetId = assetId;
        this.protocolId = protocolId;
        this.name = name;
        this.descr = descr;
        this.contact = contact;
        this.location = location;
        this.ctlgId = ctlgId;
        this.srvrAppId = srvrAppId;
        this.assetUsg = assetUsg;
        this.impLvl = impLvl;
        this.ownership = ownership;
        this.dcEnt = dcEnt;
        this.active = active;
        this.inactiveDttm = inactiveDttm;
        this.typeId = typeId;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getProtocolId() {
        return protocolId;
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

    public String getCtlgId() {
        return this.ctlgId;
    }

    public void setCtlgId(String ctlgId) {
        this.ctlgId = ctlgId;
    }

    public Short getSrvrAppId() {
        return this.srvrAppId;
    }

    public void setSrvrAppId(Short srvrAppId) {
        this.srvrAppId = srvrAppId;
    }

    public String getAssetUsg() {
        return this.assetUsg;
    }

    public void setAssetUsg(String assetUsg) {
        this.assetUsg = assetUsg;
    }

    public Byte getImpLvl() {
        return this.impLvl;
    }

    public void setImpLvl(Byte impLvl) {
        this.impLvl = impLvl;
    }

    public String getOwnership() {
        return this.ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getDcEnt() {
        return this.dcEnt;
    }

    public void setDcEnt(String dcEnt) {
        this.dcEnt = dcEnt;
    }

    public Character getActive() {
        return this.active;
    }

    public void setActive(Character active) {
        this.active = active;
    }

    public Date getInactiveDttm() {
        return this.inactiveDttm;
    }

    public void setInactiveDttm(Date inactiveDttm) {
        this.inactiveDttm = inactiveDttm;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
