package com.gq.meter.object;

// default package
// Generated Apr 10, 2013 12:26:11 PM by Hibernate Tools 3.4.0.CR1

/**
 * SrvrAppType generated by hbm2java
 */
public class SrvrAppType implements java.io.Serializable {

    private int srvrAppId;
    private String descr;

    public SrvrAppType() {
    }

    public SrvrAppType(int srvrAppId, String descr) {
        this.srvrAppId = srvrAppId;
        this.descr = descr;
    }

    public int getSrvrAppId() {
        return srvrAppId;
    }

    public void setSrvrAppId(int srvrAppId) {
        this.srvrAppId = srvrAppId;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}
