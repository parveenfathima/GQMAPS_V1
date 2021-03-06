package com.gq.meter.object;

// default package
// Generated Apr 10, 2013 12:24:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Power generated by hbm2java
 */
public class Power implements java.io.Serializable {

    private long sid;
    private String meterId;
    private Date recDttm;
    private Double voltage;
    private Double amp;
    private double energy;

    public Power() {
    }

    public Power(long sid, String meterId, Date recDttm, double energy) {
        this.sid = sid;
        this.meterId = meterId;
        this.recDttm = recDttm;
        this.energy = energy;
    }

    public Power(long sid, String meterId, Date recDttm, Double voltage, Double amp, double energy) {
        this.sid = sid;
        this.meterId = meterId;
        this.recDttm = recDttm;
        this.voltage = voltage;
        this.amp = amp;
        this.energy = energy;
    }

    public long getSid() {
        return this.sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getMeterId() {
        return this.meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public Date getRecDttm() {
        return this.recDttm;
    }

    public void setRecDttm(Date recDttm) {
        this.recDttm = recDttm;
    }

    public Double getVoltage() {
        return this.voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getAmp() {
        return this.amp;
    }

    public void setAmp(Double amp) {
        this.amp = amp;
    }

    public double getEnergy() {
        return this.energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

}
