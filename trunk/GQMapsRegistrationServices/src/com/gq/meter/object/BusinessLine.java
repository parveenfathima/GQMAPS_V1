package com.gq.meter.object;

// Generated Mar 1, 2013 12:56:00 PM by Hibernate Tools 3.4.0.CR1

/**
 * BusinessLine generated by hbm2java
 */
public class BusinessLine implements java.io.Serializable {

    private char blCd;
    private String descr;

    public BusinessLine() {
    }

    public BusinessLine(char blCd, String descr) {
        this.blCd = blCd;
        this.descr = descr;
    }

    public char getBlCd() {
        return this.blCd;
    }

    public void setBlCd(char blCd) {
        this.blCd = blCd;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}