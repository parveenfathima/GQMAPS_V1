package com.gq.meter.object;

// default package
// Generated Jul 10, 2013 2:52:19 PM by Hibernate Tools 3.4.0.CR1

/**
 * CompSnpsht generated by hbm2java
 */
public class CompSnpsht implements java.io.Serializable {

    private CompSnpshtId id;
    private String ipAddr;
    private String osId;
    private Long totMem;
    private Long usedMem;
    private Long totVMem;
    private Long usedVMem;
    private Long totDiskSpace;
    private Long usedDiskSpace;
    private short cpuLoad;
    private Long upTime;
    private Short numLgdInUsrs;
    private Short numProcs;
    private Long ntwrkBytesIn;
    private Long ntwrkBytesOut;
    private Double clockSpeed;
    private String extras;

    public CompSnpsht() {
    }

    public CompSnpsht(CompSnpshtId id, short cpuLoad) {
        this.id = id;
        this.cpuLoad = cpuLoad;
    }

    public CompSnpsht(CompSnpshtId id, String ipAddr, String osId, Long totMem, Long usedMem, Long totVMem,
            Long usedVMem, Long totDiskSpace, Long usedDiskSpace, short cpuLoad, Long upTime, Short numLgdInUsrs,
            Short numProcs, Long ntwrkBytesIn, Long ntwrkBytesOut, Double clockSpeed, String extras) {
        this.id = id;
        this.ipAddr = ipAddr;
        this.osId = osId;
        this.totMem = totMem;
        this.usedMem = usedMem;
        this.totVMem = totVMem;
        this.usedVMem = usedVMem;
        this.totDiskSpace = totDiskSpace;
        this.usedDiskSpace = usedDiskSpace;
        this.cpuLoad = cpuLoad;
        this.upTime = upTime;
        this.numLgdInUsrs = numLgdInUsrs;
        this.numProcs = numProcs;
        this.ntwrkBytesIn = ntwrkBytesIn;
        this.ntwrkBytesOut = ntwrkBytesOut;
        this.clockSpeed = clockSpeed;
        this.extras = extras;
    }

    public CompSnpshtId getId() {
        return this.id;
    }

    public void setId(CompSnpshtId id) {
        this.id = id;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getOsId() {
        return this.osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public Long getTotMem() {
        return this.totMem;
    }

    public void setTotMem(Long totMem) {
        this.totMem = totMem;
    }

    public Long getUsedMem() {
        return this.usedMem;
    }

    public void setUsedMem(Long usedMem) {
        this.usedMem = usedMem;
    }

    public Long getTotVMem() {
        return this.totVMem;
    }

    public void setTotVMem(Long totVMem) {
        this.totVMem = totVMem;
    }

    public Long getUsedVMem() {
        return this.usedVMem;
    }

    public void setUsedVMem(Long usedVMem) {
        this.usedVMem = usedVMem;
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

    public short getCpuLoad() {
        return this.cpuLoad;
    }

    public void setCpuLoad(short cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public Long getUpTime() {
        return this.upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    public Short getNumLgdInUsrs() {
        return this.numLgdInUsrs;
    }

    public void setNumLgdInUsrs(Short numLgdInUsrs) {
        this.numLgdInUsrs = numLgdInUsrs;
    }

    public Short getNumProcs() {
        return this.numProcs;
    }

    public void setNumProcs(Short numProcs) {
        this.numProcs = numProcs;
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

    public Double getClockSpeed() {
        return this.clockSpeed;
    }

    public void setClockSpeed(Double clockSpeed) {
        this.clockSpeed = clockSpeed;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

}