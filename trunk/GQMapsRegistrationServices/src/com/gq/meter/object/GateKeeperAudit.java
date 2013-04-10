package com.gq.meter.object;

// Generated Mar 1, 2013 12:57:30 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * GateKeeperAudit generated by hbm2java
 */
public class GateKeeperAudit implements java.io.Serializable {

    private int sId;
    private String enterpriseId;
    private String comment;
    private Integer scanPurchased;
    private Date expDttm;
    private Date creDttm;

    public GateKeeperAudit() {
    }

    public GateKeeperAudit(String enterpriseId, String comment, Date creDttm) {
        this.enterpriseId = enterpriseId;
        this.comment = comment;
        this.creDttm = creDttm;
    }

    public GateKeeperAudit(String enterpriseId, String comment, Integer scanPurchased, Date expDttm, Date creDttm) {
        this.enterpriseId = enterpriseId;
        this.comment = comment;
        this.scanPurchased = scanPurchased;
        this.expDttm = expDttm;
        this.creDttm = creDttm;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getExpDttm() {
        return this.expDttm;
    }

    public void setExpDttm(Date expDttm) {
        this.expDttm = expDttm;
    }

    public Date getCreDttm() {
        return this.creDttm;
    }

    public void setCreDttm(Date creDttm) {
        this.creDttm = creDttm;
    }

    public Integer getScanPurchased() {
        return scanPurchased;
    }

    public void setScanPurchased(Integer scanPurchased) {
        this.scanPurchased = scanPurchased;
    }

}
