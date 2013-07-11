package com.gq.meter.object;

// Generated Feb 18, 2013 12:54:07 PM by Hibernate Tools 3.4.0.CR1

/**
 * NSRGSnapshot generated by hbm2java
 */
public class NSRGSnapshot implements java.io.Serializable {

    private CPNId id;
    private String ipAddr;
    private Long upTime;
    private Short numPorts;
    private Short numPortsUp;
    private Long ntwrkBytesIn;
    private Long ntwrkBytesOut;
    private String extras;

    public NSRGSnapshot() {

    }

    public NSRGSnapshot(CPNId id, String ipAddr, Long upTime, Short numPorts, Short numPortsUp, Long ntwrkBytesIn,
            Long ntwrkBytesOut, String extras) {
        super();
        this.id = id;
        this.ipAddr = ipAddr;
        this.upTime = upTime;
        this.numPorts = numPorts;
        this.numPortsUp = numPortsUp;
        this.ntwrkBytesIn = ntwrkBytesIn;
        this.ntwrkBytesOut = ntwrkBytesOut;
        this.extras = extras;
    }

    public CPNId getId() {
        return this.id;
    }

    public void setId(CPNId id) {
        this.id = id;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Long getUpTime() {
        return this.upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    public Short getNumPorts() {
        return this.numPorts;
    }

    public void setNumPorts(Short numPorts) {
        this.numPorts = numPorts;
    }

    public Short getNumPortsUp() {
        return this.numPortsUp;
    }

    public void setNumPortsUp(Short numPortsUp) {
        this.numPortsUp = numPortsUp;
    }

    public Long getNtwrkBytesIn() {
        return this.ntwrkBytesIn;
    }

    public void setNtwrkBytesIn(Long ntwrkBytesIn) {
        this.ntwrkBytesIn = ntwrkBytesIn;
    }

    public Long getNtwrkBytesOut() {
        return this.ntwrkBytesOut;
    }

    public void setNtwrkBytesOut(Long ntwrkBytesOut) {
        this.ntwrkBytesOut = ntwrkBytesOut;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

}