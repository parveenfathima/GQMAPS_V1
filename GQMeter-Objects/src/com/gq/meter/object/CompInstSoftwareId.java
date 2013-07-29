package com.gq.meter.object;

// default package
// Generated Feb 21, 2013 12:58:17 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * CompInstSoftwareId generated by hbm2java
 */
public class CompInstSoftwareId implements java.io.Serializable {

    private Long runId;
    private String assetId;
    private String name;
    private Date instDttm;

    public CompInstSoftwareId() {
    }

    public CompInstSoftwareId(Long runId, String assetId, String name, Date instDttm) {
        this.runId = runId;
        this.assetId = assetId;
        this.name = name;
        this.instDttm = instDttm;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInstDttm() {
        return this.instDttm;
    }

    public void setInstDttm(Date instDttm) {
        this.instDttm = instDttm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assetId == null) ? 0 : assetId.hashCode());
        result = prime * result + ((instDttm == null) ? 0 : instDttm.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((runId == null) ? 0 : runId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CompInstSoftwareId other = (CompInstSoftwareId) obj;
        if (assetId == null) {
            if (other.assetId != null) {
                return false;
            }
        }
        else if (!assetId.equals(other.assetId)) {
            return false;
        }
        if (instDttm == null) {
            if (other.instDttm != null) {
                return false;
            }
        }
        else if (!instDttm.equals(other.instDttm)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        if (runId == null) {
            if (other.runId != null) {
                return false;
            }
        }
        else if (!runId.equals(other.runId)) {
            return false;
        }
        return true;
    }

}
