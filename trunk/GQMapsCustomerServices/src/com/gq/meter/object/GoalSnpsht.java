/**
 * 
 */
package com.gq.meter.object;

import java.sql.Timestamp;

/**
 * @author GQ
 * 
 */
public class GoalSnpsht {

    private int snpshtId;
    private String entpId;
    private String goalId;
    private String taskId;
    private String userNotes;
    private String costBenefit;
    private String sysNotes;
    private String notes;
    private String plainData;
    private Timestamp startDate;
    private Timestamp endDate;

    public GoalSnpsht() {

    }

    public GoalSnpsht(int snpshtId, String entpId, String goalId, String taskId, String userNotes, String costBenifit,
            String sysNotes, String notes, String plainData, Timestamp startDate, Timestamp endDate) {
        this.snpshtId = snpshtId;
        this.entpId = entpId;
        this.goalId = goalId;
        this.taskId = taskId;
        this.userNotes = userNotes;
        this.costBenefit = costBenifit;
        this.sysNotes = sysNotes;
        this.notes = notes;
        this.plainData = plainData;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @return the goalId
     */
    public String getGoalId() {
        return goalId;
    }

    /**
     * @param goalId the goalId to set
     */
    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    /**
     * @return the entpId
     */
    public String getEntpId() {
        return entpId;
    }

    /**
     * @param entpId the entpId to set
     */
    public void setEntpId(String entpId) {
        this.entpId = entpId;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the userNotes
     */
    public String getUserNotes() {
        return userNotes;
    }

    /**
     * @param userNotes the userNotes to set
     */
    public void setUserNotes(String userNotes) {
        this.userNotes = userNotes;
    }

    /**
     * @return the sysNotes
     */
    public String getSysNotes() {
        return sysNotes;
    }

    /**
     * @param sysNotes the sysNotes to set
     */
    public void setSysNotes(String sysNotes) {
        this.sysNotes = sysNotes;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the costBenefit
     */
    public String getCostBenefit() {
        return costBenefit;
    }

    /**
     * @param costBenefit the costBenefit to set
     */
    public void setCostBenefit(String costBenefit) {
        this.costBenefit = costBenefit;
    }

    /**
     * @return the plainData
     */
    public String getPlainData() {
        return plainData;
    }

    /**
     * @param plainData the plainData to set
     */
    public void setPlainData(String plainData) {
        this.plainData = plainData;
    }

    /**
     * @return the snpshtId
     */
    public int getSnpshtId() {
        return snpshtId;
    }

    /**
     * @param snpshtId the snpshtId to set
     */
    public void setSnpshtId(int snpshtId) {
        this.snpshtId = snpshtId;
    }

    /**
     * @return the startDate
     */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return ("objects Are:" + this.entpId + "\t" + this.goalId + "\t" + this.taskId + "\t" + this.userNotes + "\t" + this.costBenefit);

    }
}
