/**
 * 
 */
package com.gq.meter.xchange.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GQ
 * 
 */
public class GoalMaster {
    private Goal goal;
    private List<GoalSnpsht> goalSnpshtList = new ArrayList<GoalSnpsht>();
    private List<Goal> taskList = new ArrayList<Goal>();

    /**
     * @return the goal
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * @param goal the goal to set
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * @return the goalSnpshtList
     */
    public List<GoalSnpsht> getGoalSnpshtList() {
        return goalSnpshtList;
    }

    /**
     * @param goalSnpshtList the goalSnpshtList to set
     */
    public void setGoalSnpshtList(List<GoalSnpsht> goalSnpshtList) {
        this.goalSnpshtList = goalSnpshtList;
    }

    /**
     * @return the taskList
     */
    public List<Goal> getGoalList() {
        return taskList;
    }

    /**
     * @param taskList the taskList to set
     */
    public void setGoalList(List<Goal> goalList) {
        this.taskList = goalList;
    }

}
