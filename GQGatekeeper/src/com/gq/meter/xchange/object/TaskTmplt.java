package com.gq.meter.xchange.object;
// default package
// Generated Jul 26, 2013 10:19:05 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * TaskTmplt generated by hbm2java
 */
public class TaskTmplt implements java.io.Serializable {

    private TaskTmpltId id;
    private TaskAsst taskAsst;
    private String descr;
    private String tooltip;
    private Set taskChklsts = new HashSet(0);

    public TaskTmplt() {
    }

    public TaskTmplt(TaskTmpltId id, String descr) {
        this.id = id;
        this.descr = descr;
    }

    public TaskTmplt(TaskTmpltId id, TaskAsst taskAsst, String descr, String tooltip, Set taskChklsts) {
        this.id = id;
        this.taskAsst = taskAsst;
        this.descr = descr;
        this.tooltip = tooltip;
        this.taskChklsts = taskChklsts;
    }

    public TaskTmpltId getId() {
        return this.id;
    }

    public void setId(TaskTmpltId id) {
        this.id = id;
    }

    public TaskAsst getTaskAsst() {
        return this.taskAsst;
    }

    public void setTaskAsst(TaskAsst taskAsst) {
        this.taskAsst = taskAsst;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public Set getTaskChklsts() {
        return this.taskChklsts;
    }

    public void setTaskChklsts(Set taskChklsts) {
        this.taskChklsts = taskChklsts;
    }

}