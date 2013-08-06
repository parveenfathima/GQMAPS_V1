package com.gq.meter.xchange.object;
// default package
// Generated Jul 26, 2013 10:19:05 AM by Hibernate Tools 3.4.0.CR1

/**
 * GoalInput generated by hbm2java
 */
public class GoalInput implements java.io.Serializable {

    private String goalId;
    private Goal goal;
    private DataTypes dataTypes;
    private String colHoldr;
    private String descr;

    public GoalInput() {
    }

    public GoalInput(Goal goal, DataTypes dataTypes, String colHoldr, String descr) {
        this.goal = goal;
        this.dataTypes = dataTypes;
        this.colHoldr = colHoldr;
        this.descr = descr;
    }

    public String getGoalId() {
        return this.goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public DataTypes getDataTypes() {
        return this.dataTypes;
    }

    public void setDataTypes(DataTypes dataTypes) {
        this.dataTypes = dataTypes;
    }

    public String getColHoldr() {
        return this.colHoldr;
    }

    public void setColHoldr(String colHoldr) {
        this.colHoldr = colHoldr;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}
