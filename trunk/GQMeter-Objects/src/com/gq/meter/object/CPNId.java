package com.gq.meter.object;

// Generated Feb 18, 2013 12:24:17 PM by Hibernate Tools 3.4.0.CR1

/**
 * CompSnpshtId generated by hbm2java
 */
public class CPNId implements java.io.Serializable {

    private Long runId;
    private String assetId;

    public CPNId() {
    }

    public CPNId(Long runId, String assetId) {
        super();
        this.runId = runId;
        this.assetId = assetId;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assetId == null) ? 0 : assetId.hashCode());
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
        CPNId other = (CPNId) obj;
        if (assetId == null) {
            if (other.assetId != null) {
                return false;
            }
        }
        else if (!assetId.equals(other.assetId)) {
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
