package com.gq.meter.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.object.TaskCheckList;
import com.gq.meter.object.TemplateTaskDetails;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class SnapshotModel {
	
	String goalId="";
	public List<TemplateTaskDetails> getSnapshot(int snpshtId){
	 Connection dbExchange = null;
     List<TemplateTaskDetails>  chklst = new ArrayList<TemplateTaskDetails>(10);
     try {
         // getting database connection to MySQL server
         dbExchange = (Connection) SqlUtil.getExchangeConnection();
         String snapshotSql = "SELECT  tc.snpsht_id , tc.apply_date , tc.cost_benefit , tc.usr_notes , " +
         		"tc.sys_notes , tt.task_id , tt.descr , tt.ts_id , tt.tooltip FROM  task_chklst tc , task_tmplt tt " +
         		"where tc.snpsht_id = ? and tt.task_id = tc.task_id;";
         PreparedStatement snpshtStmt = (PreparedStatement) dbExchange.prepareStatement(snapshotSql);
         snpshtStmt.setInt(1, snpshtId);
         ResultSet rs = snpshtStmt.executeQuery();
         
         while(rs.next()) {
      
             chklst.add(new TemplateTaskDetails(rs.getInt("snpsht_id"), rs
                     .getTimestamp("apply_date"), rs.getInt("cost_benefit"), rs
                     .getString("usr_notes"), rs.getString("sys_notes"), rs.getInt("task_id"),
                     rs.getString("descr"), rs.getInt("ts_id"), rs
                             .getString("tooltip"),null,null,null));//chart data is null since ts_id is not used 
         }
     }
     catch (Exception e) {
         CustomerServiceConstant.logger.error("exception occured" + e);
     }
     try {
			dbExchange.close();
		} catch (SQLException e) {
			CustomerServiceConstant.logger.error("Exception while closing the session", e);
		}
	return chklst;
	}

	public List<GoalSnpsht> getGoalSnapshot(int snpshtId) {
		Connection dbExchange = null;
        List<GoalSnpsht> gsList = new ArrayList<GoalSnpsht>(20);

		try{
	         dbExchange = (Connection) SqlUtil.getExchangeConnection();

	     String goalSnpshtQuery = "select * from goal_snpsht where snpsht_id = ? ";
		 PreparedStatement prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalSnpshtQuery);
		 prepareStmt.setInt(1, snpshtId);

         ResultSet chkSet = prepareStmt.executeQuery();
         CustomerServiceConstant.logger.debug(" Query Sucessfully Executed for the Goal Snapshot");

         while (chkSet.next()) {
        	 goalId = chkSet.getString("goal_id");
             gsList.add(new GoalSnpsht(chkSet.getInt("snpsht_id"), chkSet.getString("enterprise_id"), chkSet.getString("goal_id"),chkSet.getString("notes"),
                     chkSet.getInt("cost_benefit"), chkSet.getString("pre_result"), chkSet
                             .getString("post_result"), chkSet.getTimestamp("start_date"), chkSet
                             .getTimestamp("end_date")));
         }
		}catch(Exception e){
			CustomerServiceConstant.logger.error("Exception Occured ",e);

		}
		try {
			dbExchange.close();
		} catch (SQLException e) {
			CustomerServiceConstant.logger.error("Exception while closing the session", e);
		}
		return gsList;
	}

	public Goal getGoal() {
		Connection dbExchange = null;
        Goal goal = null;

		try{
        dbExchange = (Connection) SqlUtil.getExchangeConnection();

		String GoalSql = "select goal_id,descr,time_bound,perf_notes from goal where goal_id= ?;";
        PreparedStatement prepareStmt = (PreparedStatement) dbExchange.prepareStatement(GoalSql);
         prepareStmt.setString(1, goalId);
        ResultSet rs = prepareStmt.executeQuery();
         CustomerServiceConstant.logger.debug(" Query Executed for the Goal Table");

         if (rs.next()) {

             goal = new Goal(rs.getString("goal_id"), rs.getString("descr"), rs.getString("perf_notes"),
                     rs.getString("time_bound"), null); // last arg image is set to null for now - ss oct 10,13
         }
         else {
             CustomerServiceConstant.logger.error("No Goal row found in table");
             return null;
         }
		}catch(Exception e){
			CustomerServiceConstant.logger.error("Exception Occured ",e);
		}
		try {
			dbExchange.close();
		} catch (SQLException e) {
			CustomerServiceConstant.logger.error("Exception while closing the session", e);
		}
		return goal;
	}
}
