package com.gq.meter.xchange.object;

// Generated May 2, 2013 1:01:39 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * GateKeeperAudit generated by hbm2java
 */
public class GateKeeperAudit implements java.io.Serializable {

    private Short sid;
    private String enterpriseId;
    private String comment;
    private Date expDttm;
    private Date creDttm;

    public GateKeeperAudit() {
    }

    public GateKeeperAudit(String enterpriseId, String comment, Date creDttm) {
        this.enterpriseId = enterpriseId;
        this.comment = comment;
        this.creDttm = creDttm;
    }

    public GateKeeperAudit(String enterpriseId, String comment, Date expDttm, Date creDttm) {
        this.enterpriseId = enterpriseId;
        this.comment = comment;
        this.expDttm = expDttm;
        this.creDttm = creDttm;
    }

    public Short getSid() {
        return this.sid;
    }

    public void setSid(Short sid) {
        this.sid = sid;
    }

    public String getEnterpriseId() {
        return this.enterpriseId;
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

}
