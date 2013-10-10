package com.gq.meter.xchange.object;

/**
 * @author Rathish
 * 
 */
public class Goal {

    private String goal_Id;
    private String descr;
    private String perfNotes;
    private String timeBound;
    private String image;

    public Goal(String goal_Id, String descr, String perfNotes, String timeBound, String image) {
        super();
        this.goal_Id = goal_Id;
        this.descr = descr;
        this.perfNotes = perfNotes;
        this.timeBound = timeBound;
        this.image = image;
    }

    public String getGoal_Id() {
        return goal_Id;
    }

    public String getDescr() {
        return descr;
    }

    public String getPerfNotes() {
        return perfNotes;
    }

    public String getTimeBound() {
        return timeBound;
    }

    public String getImage() {
        return image;
    }

}
