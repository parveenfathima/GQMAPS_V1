package com.gq.meter.object;

// Generated Feb 18, 2013 3:31:30 PM by Hibernate Tools 3.4.0.CR1

/**
 * Protocol generated by hbm2java
 */
public class Protocol implements java.io.Serializable {

    private String protocolId;
    private String descr;

    public Protocol() {
    }

    public Protocol(String protocolId, String descr) {
        super();
        this.protocolId = protocolId;
        this.descr = descr;
    }

    public String getProtocolId() {
        return this.protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}