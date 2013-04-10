package com.gq.meter.object;

// Generated Mar 1, 2013 12:57:04 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * GateKeeperServices generated by hbm2java
 */
public class GateKeeper implements java.io.Serializable {

    private String enterpriseId;
    private Date expDttm;
    private Integer scnRmng;
    private char chkCndtn;

    public GateKeeper() {
    }

    public GateKeeper(String enterpriseId, char chkCndtn) {
        this.enterpriseId = enterpriseId;
        this.chkCndtn = chkCndtn;
    }

    public GateKeeper(String enterpriseId, Date expDttm, Integer scnAlwd, Integer scnRmng, char chkCndtn) {
        this.enterpriseId = enterpriseId;
        this.expDttm = expDttm;
        this.scnRmng = scnRmng;
        this.chkCndtn = chkCndtn;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Date getExpDttm() {
        return this.expDttm;
    }

    public void setExpDttm(Date expDttm) {
        this.expDttm = expDttm;
    }

    public Integer getScnRmng() {
        return this.scnRmng;
    }

    public void setScnRmng(Integer scnRmng) {
        this.scnRmng = scnRmng;
    }

    public char getChkCndtn() {
        return this.chkCndtn;
    }

    public void setChkCndtn(char chkCndtn) {
        this.chkCndtn = chkCndtn;
    }

}
