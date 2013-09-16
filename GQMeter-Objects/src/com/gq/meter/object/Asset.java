package com.gq.meter.object;

// default package
// Generated Jul 26, 2013 4:15:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Asset generated by hbm2java
 */
public class Asset implements java.io.Serializable {

    private String assetId;
    private String protocolId;
    private String name;
    private String descr;
    private String ipAddr;
    private String contact;
    private String location;
    private Short srvrAppId;
    private String assetUsg;
    private String ownership;
    private String dcEnt;
    private Character active;
    private Date inactiveDttm;
    private String ctlgId;
    private Byte imp_level;
    private String type_id;

    public Asset() {
    }

    public Asset(String assetId, String protocolId, String assetUsg) {
        this.assetId = assetId;
        this.protocolId = protocolId;
        this.assetUsg = assetUsg;
    }

    public Asset(String assetId, String protocolId, String name, String descr, String ipAddr, String contact,
            String location, Short srvrAppId, String assetUsg, String ctlgId, Byte imp_level) {
        super();
        this.assetId = assetId;
        this.protocolId = protocolId;
        this.name = name;
        this.descr = descr;
        this.contact = contact;
        this.location = location;
        this.srvrAppId = srvrAppId;
        this.assetUsg = assetUsg;
        this.ctlgId = ctlgId;
        this.imp_level = imp_level;
        this.ipAddr = ipAddr;
    }

    public Asset(String assetId, String protocolId, String name, String descr, String ipAddr, String contact,
            String location, Short srvrAppId, String assetUsg, String ownership, String dcEnt, Character active,
            Date inactiveDttm, String ctlgId, Byte imp_level, String type_id) {
        super();
        this.assetId = assetId;
        this.protocolId = protocolId;
        this.name = name;
        this.descr = descr;
        this.ipAddr = ipAddr;
        this.contact = contact;
        this.location = location;
        this.srvrAppId = srvrAppId;
        this.assetUsg = assetUsg;
        this.ownership = ownership;
        this.dcEnt = dcEnt;
        this.active = active;
        this.inactiveDttm = inactiveDttm;
        this.ctlgId = ctlgId;
        this.imp_level = imp_level;
        this.type_id = type_id;
    }

    public Asset(String assetId, String protocolId, String name, String descr, String ipAddr, String contact,
            String location) {
        super();
        this.assetId = assetId;
        this.protocolId = protocolId;
        this.name = name;
        this.descr = descr;
        this.ipAddr = ipAddr;
        this.contact = contact;
        this.location = location;
    }

    public String getCtlgId() {
        return ctlgId;
    }

    public void setCtlgId(String ctlgId) {
        this.ctlgId = ctlgId;
    }

    public Byte getImp_level() {
        return imp_level;
    }

    public void setImp_level(Byte imp_level) {
        this.imp_level = imp_level;
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

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
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

    public Short getSrvrAppId() {
        return srvrAppId;
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

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

}