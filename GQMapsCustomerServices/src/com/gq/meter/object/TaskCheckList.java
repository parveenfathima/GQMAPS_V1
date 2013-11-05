package com.gq.meter.object;

import java.sql.Timestamp;

public class TaskCheckList {
    private int snpsht_id;
    private int task_id;
    private Timestamp apply_date;
    private String usr_notes;
    private int cost_benifit;
    private String sys_notes;

    public TaskCheckList(int snpsht_id, int task_id, Timestamp apply_date, String usr_notes, int cost_benifit,
            String sys_notes) {
        super();
        this.snpsht_id = snpsht_id;
        this.task_id = task_id;
        this.apply_date = apply_date;
        this.usr_notes = usr_notes;
        this.cost_benifit = cost_benifit;
        this.sys_notes = sys_notes;
    }

    public int getSnpsht_id() {
        return snpsht_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public Timestamp getApply_date() {
        return apply_date;
    }

    public String getUsr_notes() {
        return usr_notes;
    }

    public int getCost_benifit() {
        return cost_benifit;
    }

    public String getSys_notes() {
        return sys_notes;
    }

}
