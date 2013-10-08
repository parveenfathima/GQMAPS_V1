package com.gq.meter.xchange.object;

// default package
// Generated Sep 24, 2013 3:26:49 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * TaskChklst generated by hbm2java
 */
public class TaskChklst implements java.io.Serializable {

    private TaskChklstId id;
    private TaskTmplt taskTmplt;
    private String enterpriseId;
    private Date applyDate;
    private String usrNotes;
    private Integer costBenefit;
    private String sysNotes;

    public TaskChklst() {
    }

    public TaskChklst(TaskChklstId id, TaskTmplt taskTmplt) {
        this.id = id;
        this.taskTmplt = taskTmplt;
    }

    public TaskChklst(TaskChklstId id, TaskTmplt taskTmplt, String enterpriseId, Date applyDate, String usrNotes,
            Integer costBenefit, String sysNotes) {
        this.id = id;
        this.taskTmplt = taskTmplt;
        this.enterpriseId = enterpriseId;
        this.applyDate = applyDate;
        this.usrNotes = usrNotes;
        this.costBenefit = costBenefit;
        this.sysNotes = sysNotes;
    }

    public TaskChklstId getId() {
        return this.id;
    }

    public void setId(TaskChklstId id) {
        this.id = id;
    }

    public TaskTmplt getTaskTmplt() {
        return this.taskTmplt;
    }

    public void setTaskTmplt(TaskTmplt taskTmplt) {
        this.taskTmplt = taskTmplt;
    }

    public String getEnterpriseId() {
        return this.enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Date getApplyDate() {
        return this.applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getUsrNotes() {
        return this.usrNotes;
    }

    public void setUsrNotes(String usrNotes) {
        this.usrNotes = usrNotes;
    }

    public Integer getCostBenefit() {
        return this.costBenefit;
    }

    public void setCostBenefit(Integer costBenefit) {
        this.costBenefit = costBenefit;
    }

    public String getSysNotes() {
        return this.sysNotes;
    }

    public void setSysNotes(String sysNotes) {
        this.sysNotes = sysNotes;
    }

}