package com.gq.meter.object;
// default package
// Generated Aug 1, 2013 3:02:28 PM by Hibernate Tools 3.4.0.CR1

/**
 * SrvrAppType generated by hbm2java
 */
public class SrvrAppType implements java.io.Serializable {

    private short srvrAppId;
    private String descr;

    public SrvrAppType() {
    }

    public SrvrAppType(short srvrAppId, String descr) {
        this.srvrAppId = srvrAppId;
        this.descr = descr;
    }

    public short getSrvrAppId() {
        return this.srvrAppId;
    }

    public void setSrvrAppId(short srvrAppId) {
        this.srvrAppId = srvrAppId;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}
