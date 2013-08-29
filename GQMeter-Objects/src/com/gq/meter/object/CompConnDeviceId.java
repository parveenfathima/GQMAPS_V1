package com.gq.meter.object;

// default package
// Generated Feb 21, 2013 2:44:12 PM by Hibernate Tools 3.4.0.CR1

/**
 * CompConnDeviceId generated by hbm2java
 */
public class CompConnDeviceId implements java.io.Serializable {

    private Long runId;
    private String assetId;
    private String ipAddr;
    private int port;

    public CompConnDeviceId() {
    }

    public CompConnDeviceId(Long runId, String assetId, String ipAddr, int port) {
        this.runId = runId;
        this.assetId = assetId;
        this.ipAddr = ipAddr;
        this.port = port;
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

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assetId == null) ? 0 : assetId.hashCode());
        result = prime * result + ((ipAddr == null) ? 0 : ipAddr.hashCode());
        result = prime * result + port;
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
        CompConnDeviceId other = (CompConnDeviceId) obj;
        if (assetId == null) {
            if (other.assetId != null) {
                return false;
            }
        }
        else if (!assetId.equals(other.assetId)) {
            return false;
        }
        if (ipAddr == null) {
            if (other.ipAddr != null) {
                return false;
            }
        }
        else if (!ipAddr.equals(other.ipAddr)) {
            return false;
        }
        if (port != other.port) {
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