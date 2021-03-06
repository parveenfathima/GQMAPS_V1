package com.gq.meter.xchange.object;
// default package
// Generated Jul 26, 2013 10:19:05 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ChartType generated by hbm2java
 */
public class ChartType implements java.io.Serializable {

    private String ctId;
    private String descr;
    private Set taskAssts = new HashSet(0);

    public ChartType() {
    }

    public ChartType(String ctId) {
        this.ctId = ctId;
    }

    public ChartType(String ctId, String descr, Set taskAssts) {
        this.ctId = ctId;
        this.descr = descr;
        this.taskAssts = taskAssts;
    }

    public String getCtId() {
        return this.ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Set getTaskAssts() {
        return this.taskAssts;
    }

    public void setTaskAssts(Set taskAssts) {
        this.taskAssts = taskAssts;
    }

}
