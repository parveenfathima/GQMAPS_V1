/**
 * 
 */
package com.gq.meter.object;

import java.util.Date;

/**
 * @author GQ
 * 
 */
public class EntpSummary implements java.io.Serializable {
    private String enterpriseId;
    private String eName;
    private Date expDttm;

    public EntpSummary() {

    }

    public EntpSummary(String enterpriseId, String eName, Date expDttm) {
        this.enterpriseId = enterpriseId;
        this.eName = eName;
        this.expDttm = expDttm;

    }

    /**
     * @return the enterpriseId
     */
    public String getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * @param enterpriseId the enterpriseId to set
     */
    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * @return the eName
     */
    public String geteName() {
        return eName;
    }

    /**
     * @param eName the eName to set
     */
    public void seteName(String eName) {
        this.eName = eName;
    }

    /**
     * @return the expDttm
     */
    public Date getExpDttm() {
        return expDttm;
    }

    /**
     * @param expDttm the expDttm to set
     */
    public void setExpDttm(Date expDttm) {
        this.expDttm = expDttm;
    }

}
