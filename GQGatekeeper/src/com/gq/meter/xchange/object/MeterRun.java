package com.gq.meter.xchange.object;

// Generated Mar 1, 2013 12:57:44 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * MeterRun generated by hbm2java
 */
public class MeterRun implements java.io.Serializable {

    private Integer runId;
    private String meterId;
    private Date recDttm;
    private short assetScnd;
    private short assetDisc;
    private long runTimeMs;

    public MeterRun() {
    }

    public MeterRun(String meterId, Date recDttm, short assetScnd, short assetDisc, long runTimeMs) {
        this.meterId = meterId;
        this.recDttm = recDttm;
        this.assetScnd = assetScnd;
        this.assetDisc = assetDisc;
        this.runTimeMs = runTimeMs;
    }

    public Integer getRunId() {
        return this.runId;
    }

    public void setRunId(Integer runId) {
        this.runId = runId;
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

    public short getAssetScnd() {
        return this.assetScnd;
    }

    public void setAssetScnd(short assetScnd) {
        this.assetScnd = assetScnd;
    }

    public short getAssetDisc() {
        return this.assetDisc;
    }

    public void setAssetDisc(short assetDisc) {
        this.assetDisc = assetDisc;
    }

    public long getRunTimeMs() {
        return this.runTimeMs;
    }

    public void setRunTimeMs(long runTimeMs) {
        this.runTimeMs = runTimeMs;
    }

}
