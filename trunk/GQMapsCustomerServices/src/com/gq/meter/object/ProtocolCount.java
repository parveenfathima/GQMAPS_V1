/**
 * 
 */
package com.gq.meter.object;

/**
 * @author GQ
 * 
 */
public class ProtocolCount {
    private String protocolId;
    private long pcount;

    public ProtocolCount() {
    }

    public ProtocolCount(String protocolId, long pcount) {
        this.protocolId = protocolId;
        this.pcount = pcount;
    }

    /**
     * @return the protocolId
     */
    public String getProtocolId() {
        return protocolId;
    }

    /**
     * @param protocolId the protocolId to set
     */
    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return the pcount
     */
    public long getPcount() {
        return pcount;
    }

    /**
     * @param pcount the pcount to set
     */
    public void setPcount(long pcount) {
        this.pcount = pcount;
    }

}
