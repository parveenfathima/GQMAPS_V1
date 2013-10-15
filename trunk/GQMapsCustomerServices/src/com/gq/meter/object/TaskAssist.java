package com.gq.meter.object;

/**
 * 
 */

/**
 * @author GQ
 * 
 */
public class TaskAssist {
    public int taskId;
    public String descr;
    public String sql;
    public String dynamic;
    public String chartType;
    public String[] columnHeader;
    public String relatedDb;
    public String positionId;
    private CharSequence chartData;
    private double plainData;
    private String plain;
    private String lineData;

    public TaskAssist() {

    }

    public TaskAssist(int taskId, String descr, String sql, String dynamic, String chartType, String[] columnHeader,
            String relatedDb, String positionId, CharSequence chartData, double plainData, String lineData, String plain) {
        this.taskId = taskId;
        this.descr = descr;
        this.sql = sql;
        this.dynamic = dynamic;
        this.chartType = chartType;
        this.columnHeader = columnHeader;
        this.relatedDb = relatedDb;
        this.positionId = positionId;
        this.chartData = chartData;
        this.plainData = plainData;
        this.lineData = lineData;
        this.plain = plain;

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
     * @return the descr
     */
    public String getDescr() {
        return descr;
    }

    /**
     * @param descr the descr to set
     */
    public void setDescr(String descr) {
        this.descr = descr;
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
     * @return the columnHeader
     */
    public String[] getColumnHeader() {
        return columnHeader;
    }

    /**
     * @param columnHeader the columnHeader to set
     */
    public void setColumnHeader(String[] columnHeader) {
        this.columnHeader = columnHeader;
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

    /**
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * @param sql the sql to set
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * @return the dynamic
     */
    public String getDynamic() {
        return dynamic;
    }

    /**
     * @param dynamic the dynamic to set
     */
    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * @return the relatedDb
     */
    public String getRelatedDb() {
        return relatedDb;
    }

    /**
     * @param relatedDb the relatedDb to set
     */
    public void setRelatedDb(String relatedDb) {
        this.relatedDb = relatedDb;
    }

    public CharSequence getChartData() {
        return chartData;
    }

    public void setChartData(CharSequence chartData2) {
        this.chartData = chartData2;
    }

    public double getPlainData() {
        return plainData;
    }

    public void setPlainData(double d) {
        this.plainData = d;
    }

    public String getLineData() {
        return lineData;
    }

    public void setLineData(String lineData) {
        this.lineData = lineData;
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

}
