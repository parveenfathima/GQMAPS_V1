package com.gq.meter.object;

// default package
// Generated Apr 5, 2013 12:29:13 PM by Hibernate Tools 3.4.0.CR1

/**
 * StorageSnpsht generated by hbm2java
 */
public class StorageSnpsht implements java.io.Serializable {

    private CPNId id;
    private Long upTime;
    private Long totDiskSpace;
    private Long usedDiskSpace;
    private Short numOfCntrlr;
    private Short numOfDisks;
    private Long iops;
    private String cnctrProtocol;
    private String extras;

    public StorageSnpsht() {
    }

    public StorageSnpsht(CPNId id, Long upTime, Long totDiskSpace, Long usedDiskSpace, Short numOfCntrlr,
            Short numOfDisks, Long iops, String cnctrProtocol, String extras) {
        this.id = id;
        this.upTime = upTime;
        this.totDiskSpace = totDiskSpace;
        this.usedDiskSpace = usedDiskSpace;
        this.numOfCntrlr = numOfCntrlr;
        this.numOfDisks = numOfDisks;
        this.iops = iops;
        this.cnctrProtocol = cnctrProtocol;
        this.extras = extras;
    }

    public CPNId getId() {
        return this.id;
    }

    public void setId(CPNId id) {
        this.id = id;
    }

    public Long getUpTime() {
        return this.upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    public Long getTotDiskSpace() {
        return this.totDiskSpace;
    }

    public void setTotDiskSpace(Long totDiskSpace) {
        this.totDiskSpace = totDiskSpace;
    }

    public Long getUsedDiskSpace() {
        return this.usedDiskSpace;
    }

    public void setUsedDiskSpace(Long usedDiskSpace) {
        this.usedDiskSpace = usedDiskSpace;
    }

    public Short getNumOfCntrlr() {
        return this.numOfCntrlr;
    }

    public void setNumOfCntrlr(Short numOfCntrlr) {
        this.numOfCntrlr = numOfCntrlr;
    }

    public Short getNumOfDisks() {
        return this.numOfDisks;
    }

    public void setNumOfDisks(Short numOfDisks) {
        this.numOfDisks = numOfDisks;
    }

    public Long getIops() {
        return this.iops;
    }

    public void setIops(Long iops) {
        this.iops = iops;
    }

    public String getCnctrProtocol() {
        return this.cnctrProtocol;
    }

    public void setCnctrProtocol(String cnctrProtocol) {
        this.cnctrProtocol = cnctrProtocol;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

}