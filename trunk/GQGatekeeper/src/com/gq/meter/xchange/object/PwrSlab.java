package com.gq.meter.xchange.object;
// default package
// Generated Jul 26, 2013 10:19:05 AM by Hibernate Tools 3.4.0.CR1

/**
 * PwrSlab generated by hbm2java
 */
public class PwrSlab implements java.io.Serializable {

    private int sid;
    private String locId;
    private short ULwrBnd;
    private short UUprBnd;
    private double rate;

    public PwrSlab() {
    }

    public PwrSlab(int sid, String locId, short ULwrBnd, short UUprBnd, double rate) {
        this.sid = sid;
        this.locId = locId;
        this.ULwrBnd = ULwrBnd;
        this.UUprBnd = UUprBnd;
        this.rate = rate;
    }

    public int getSid() {
        return this.sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getLocId() {
        return this.locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public short getULwrBnd() {
        return this.ULwrBnd;
    }

    public void setULwrBnd(short ULwrBnd) {
        this.ULwrBnd = ULwrBnd;
    }

    public short getUUprBnd() {
        return this.UUprBnd;
    }

    public void setUUprBnd(short UUprBnd) {
        this.UUprBnd = UUprBnd;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}