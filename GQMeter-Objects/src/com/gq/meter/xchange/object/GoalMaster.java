/**
 * 
 */
package com.gq.meter.xchange.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rathish
 * 
 */
public class GoalMaster {
    private Goal goal;
    private List<GoalSnpsht> goalSnpshtList = new ArrayList<GoalSnpsht>();
    private List<TemplateTaskDetails> TemplateTaskDetails = new ArrayList<TemplateTaskDetails>();

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public List<GoalSnpsht> getGoalSnpshtList() {
        return goalSnpshtList;
    }

    public void setGoalSnpshtList(List<GoalSnpsht> goalSnpshtList) {
        this.goalSnpshtList = goalSnpshtList;
    }

    public List<TemplateTaskDetails> getTemplateTaskDetails() {
        return TemplateTaskDetails;
    }

    public void setTemplateTaskDetails(List<TemplateTaskDetails> templateTaskDetails) {
        TemplateTaskDetails = templateTaskDetails;
    }

}
