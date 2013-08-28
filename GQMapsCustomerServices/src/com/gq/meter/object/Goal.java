/**
 * 
 */
package com.gq.meter.object;

/**
 * @author GQ
 * 
 */
public class Goal {
    private String goal_Id;
    private int taskId;
    private CharSequence chartData;
    private double plainData;
    private String plain;
    private String chartType;
    private String positionId;
    private String descr;

    public Goal() {

    }

    public Goal(String goal_Id, int taskId, CharSequence chartData, double plainData, String plain, String chartType,
            String positionId, String descr) {
        this.goal_Id = goal_Id;
        this.taskId = taskId;
        this.chartData = chartData;
        this.plainData = plainData;
        this.plain = plain;
        this.chartType = chartType;
        this.positionId = positionId;
        this.descr = descr;
    }

    /**
     * @return the goal_Id
     */
    public String getGoal_Id() {
        return goal_Id;
    }

    /**
     * @param goal_Id the goal_Id to set
     */
    public void setGoal_Id(String goal_Id) {
        this.goal_Id = goal_Id;
    }

    /**
     * @return the taskId
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the chartData
     */
    public CharSequence getChartData() {
        return chartData;
    }

    /**
     * @param chartData the chartData to set
     */
    public void setChartData(CharSequence chartData) {
        this.chartData = chartData;
    }

    /**
     * @return the plainData
     */
    public double getPlainData() {
        return plainData;
    }

    /**
     * @param plainData the plainData to set
     */
    public void setPlainData(double plainData) {
        this.plainData = plainData;
    }

    /**
     * @return the plain
     */
    public String getPlain() {
        return plain;
    }

    /**
     * @param plain the plain to set
     */
    public void setPlain(String plain) {
        this.plain = plain;
    }

    /**
     * @return the chartType
     */
    public String getChartType() {
        return chartType;
    }

    /**
     * @param chartType the chartType to set
     * @return
     */
    public String setChartType(String chartType) {
        return this.chartType = chartType;
    }

    /**
     * @return the positionId
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * @param positionId the positionId to set
     */
    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}
