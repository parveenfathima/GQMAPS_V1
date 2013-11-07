package com.gq.meter.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.object.TemplateTaskDetails;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class FinalizeGoalModel {
    Connection dbExchange = null;

    public void CompleteGoal(GoalMaster goalmaster) {

        PreparedStatement preStmt;
        Connection dbExchange = null;
        ResultSet snpshtset = null;
        String goalSnpshtSql = "";
        String tskChkList = "";
        String entpId = "";
        int snpshtId = 0;

        Timestamp currentDate = new Timestamp(new Date().getTime());
        CustomerServiceConstant.logger.info(" Current Date is" + currentDate);
        CustomerServiceConstant.logger.info(" Data is Inserting into GoalSnapshot");
        try {
        	  dbExchange = (Connection) SqlUtil.getExchangeConnection();
              PreparedStatement preparegoalStmt = null;

        	  
            // intial check to the goal snpsht
            String snpshtcheck = " select * from goal_snpsht where snpsht_id= ? and enterprise_id= ?;";
            preStmt = (PreparedStatement) dbExchange.prepareStatement(snpshtcheck);
            preStmt.setInt(1, goalmaster.getGoalSnpshtList().get(0).getSnpshtId());
            preStmt.setString(2, goalmaster.getGoalSnpshtList().get(0).getEntpId());
            ResultSet rs = preStmt.executeQuery();

            if (rs.next()) {
                goalSnpshtSql = "UPDATE goal_snpsht SET   goal_id = ?, enterprise_id = ? , start_date = ?,"
                        + " notes = ? , cost_benefit = ?  WHERE snpsht_id = ? and enterprise_id = ?;";
                
                preparegoalStmt = (PreparedStatement) dbExchange.prepareStatement(goalSnpshtSql);
                preparegoalStmt.setString(1, goalmaster.getGoalSnpshtList().get(0).getGoalId());
                preparegoalStmt.setString(2, goalmaster.getGoalSnpshtList().get(0).getEntpId());
                preparegoalStmt.setTimestamp(3, goalmaster.getGoalSnpshtList().get(0).getStartDate());
                preparegoalStmt.setString(4, goalmaster.getGoalSnpshtList().get(0).getNotes());
                preparegoalStmt.setInt(5, goalmaster.getGoalSnpshtList().get(0).getCostBenefit());
                //pre and post result is not used now 28-10-2013
//                preparegoalStmt.setString(6, goalmaster.getGoalSnpshtList().get(0).getPreResult());
//                preparegoalStmt.setString(7, goalmaster.getGoalSnpshtList().get(0).getPostResult());
                
                preparegoalStmt.setInt(6, goalmaster.getGoalSnpshtList().get(0).getSnpshtId());
                preparegoalStmt.setString(7, goalmaster.getGoalSnpshtList().get(0).getEntpId());

            }else{


                // first step inserting into the goal snpsht
            goalSnpshtSql = "insert into goal_snpsht (goal_id,enterprise_id,start_date,cost_benefit,notes) values(?,?,?,?,?);";
            //for(int i=1;i<goalmaster.getGoalSnpshtList().size();i++){
            preparegoalStmt = (PreparedStatement) dbExchange.prepareStatement(goalSnpshtSql);
            preparegoalStmt.setString(1, goalmaster.getGoalSnpshtList().get(0).getGoalId());
            preparegoalStmt.setString(2, goalmaster.getGoalSnpshtList().get(0).getEntpId());
            preparegoalStmt.setTimestamp(3, goalmaster.getGoalSnpshtList().get(0).getStartDate());
            preparegoalStmt.setInt(4, goalmaster.getGoalSnpshtList().get(0).getCostBenefit());
            preparegoalStmt.setString(5, goalmaster.getGoalSnpshtList().get(0).getNotes());
          //preresult and post result is not used now 28-10-2013
//            preparegoalStmt.setString(5, goalmaster.getGoalSnpshtList().get(0).getPreResult());
//            preparegoalStmt.setString(6, goalmaster.getGoalSnpshtList().get(0).getPostResult());
           
            
            }
            preparegoalStmt.executeUpdate();
            CustomerServiceConstant.logger.info(" Data is inserted into GoalSnapshot");

            //}
            // fetching the recently added goalsnpsht_id for the enterprise
            String latestSnpshtId = "select start_date,enterprise_id, max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=? and end_date is null;";
            PreparedStatement stmt = (PreparedStatement) dbExchange.prepareStatement(latestSnpshtId);
            stmt.setString(1, goalmaster.getGoalSnpshtList().get(0).getEntpId());
            ResultSet snpshtSet = stmt.executeQuery();
            while (snpshtSet.next()) {
                snpshtId = snpshtSet.getInt("snpsht_id");
                entpId = snpshtSet.getString("enterprise_id");
            }
            CustomerServiceConstant.logger.info(" selected max snapshotid from  GoalSnapshot");

            // fetching the task details fr snpsht id
            String snpShtquery = "SELECT * FROM task_chklst where  snpsht_id = ? ";
            PreparedStatement snpshtStmt = (PreparedStatement) dbExchange.prepareStatement(snpShtquery);
            snpshtStmt.setInt(1, snpshtId);

            snpshtset = snpshtStmt.executeQuery();
            PreparedStatement tskPrepStmt = null;

            if (snpshtset.next()) {
                for(int i=0;i<goalmaster.getTemplateTaskDetails().size();i++){

                // update the existing table
                tskChkList = "UPDATE task_chklst SET snpsht_id= ?, task_id = ?, apply_date = ? ,  usr_notes = ? , cost_benefit = ? ,"
                        + " sys_notes = ?  WHERE snpsht_id = ? and task_id = ? ";
                tskPrepStmt = (PreparedStatement) dbExchange.prepareStatement(tskChkList);
                tskPrepStmt.setInt(1, snpshtId);
                tskPrepStmt.setInt(2, goalmaster.getTemplateTaskDetails().get(i).getTask_id());
                Timestamp applyDate =null;
                if(goalmaster.getTemplateTaskDetails().get(i).getApply_date()!=null){
                	 applyDate =goalmaster.getTemplateTaskDetails().get(i).getApply_date();   
                }
                tskPrepStmt.setTimestamp(3, applyDate);
                tskPrepStmt.setString(4, goalmaster.getTemplateTaskDetails().get(i).getUsr_notes());// need to remove
                tskPrepStmt.setInt(5, goalmaster.getTemplateTaskDetails().get(i).getCost_benefit());
                tskPrepStmt.setString(6, goalmaster.getTemplateTaskDetails().get(i).getSys_notes());
                tskPrepStmt.setInt(7, snpshtId);
                tskPrepStmt.setInt(8, goalmaster.getTemplateTaskDetails().get(i).getTask_id());
                tskPrepStmt.executeUpdate();

                }
            }
            else {
                for(int i=0;i<goalmaster.getTemplateTaskDetails().size();i++){

                // inserting a new row the goal snapshot all the time to record the goal has been viewed
                tskChkList = "insert into task_chklst (snpsht_id,task_id,apply_date,usr_notes,cost_benefit,sys_notes) values(?,?,?,?,?,?);";

                // inserting to task check list
                tskPrepStmt = (PreparedStatement) dbExchange.prepareStatement(tskChkList);
                tskPrepStmt.setInt(1, snpshtId);
                tskPrepStmt.setInt(2, goalmaster.getTemplateTaskDetails().get(i).getTask_id());
                Timestamp applyDate = new Timestamp(new Date().getTime());// settting the Current date and time
                tskPrepStmt.setTimestamp(3, goalmaster.getTemplateTaskDetails().get(i).getApply_date());
                tskPrepStmt.setString(4, goalmaster.getTemplateTaskDetails().get(i).getUsr_notes());// need to remove
                tskPrepStmt.setInt(5, goalmaster.getTemplateTaskDetails().get(i).getCost_benefit());
                tskPrepStmt.setString(6, goalmaster.getTemplateTaskDetails().get(i).getSys_notes());
                tskPrepStmt.executeUpdate();

                }
            }
            CustomerServiceConstant.logger.info(" inserting into task checklist");

        }
        catch (SQLException e) {
            CustomerServiceConstant.logger.error("Exception occured while inserting the Data",e);//fuctionality blk
        } catch (Exception e) {
            CustomerServiceConstant.logger.error("Exception occured",e);//fuctionality blk
		}
        finally{
        	try {
				dbExchange.close();
			} catch (SQLException e) {
	            CustomerServiceConstant.logger.error("Exception occured while closing the connection",e);//dbxlose
			}
        }

    }

    public void FinalizeGoal(GoalMaster goalmaster) {

        // for finalise

        try {
            int snshtId = 0;
            dbExchange = (Connection) SqlUtil.getExchangeConnection();
            CustomerServiceConstant.logger.info(" finalize operation started");

            // fetching the recently added goalsnpsht_id for the enterprise
            String finalizeSql = "select  max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?";
            PreparedStatement preparefinlStmt = (PreparedStatement) dbExchange.prepareStatement(finalizeSql);
            preparefinlStmt.setString(1, goalmaster.getGoalSnpshtList().get(0).getEntpId());
            ResultSet finalizeSnpshtSet = preparefinlStmt.executeQuery();
            while (finalizeSnpshtSet.next()) {
                snshtId = finalizeSnpshtSet.getInt("snpsht_id");
            }

            // updating the goalSnpsht with the end date and the sys notes
            Timestamp applyDate = new Timestamp(new Date().getTime());
            CustomerServiceConstant.logger.info(" Apply date is " + applyDate);
            String finalizeUpdateQuery = "update goal_snpsht set end_date=?,notes=? where enterprise_id=? and snpsht_id=?;";
            PreparedStatement prepStmt = (PreparedStatement) dbExchange.prepareStatement(finalizeUpdateQuery);
            prepStmt.setTimestamp(1, applyDate);
            prepStmt.setString(2, goalmaster.getGoalSnpshtList().get(0).getNotes());
            prepStmt.setString(3, goalmaster.getGoalSnpshtList().get(0).getEntpId());
            prepStmt.setInt(4, snshtId);
            prepStmt.executeUpdate();
            CustomerServiceConstant.logger.info(" sucessfully updated the table ");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    	try {
			dbExchange.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//db close
		}
    
    }

	public GoalMaster buildGoalMasterObj(String jsonString) {
		GoalMaster gm = new GoalMaster();
		List<GoalSnpsht> gslist = new ArrayList<GoalSnpsht>(1);
		try {
			//getting the snapshot id
			// get the submitted form json into one container json object.
			JSONObject json = new JSONObject(jsonString);
			int snpshtId=0;
			//getting the snapshot id
			if(json.getInt("gs_id")	== 0){
				String snpshtIdQuery="select max(snpsht_id) as snpsht_id from goal_snpsht;";
				Statement createStmt = (Statement) dbExchange.createStatement();
				ResultSet rs = createStmt.executeQuery(snpshtIdQuery);
				while (rs.next()){
					snpshtId = rs.getInt(1) + 1;//manually creating snpshot id 
				}
			}else{
				snpshtId = json.getInt("gs_id");
			}
			 Timestamp	startDate = new Timestamp(new Date().getTime());
			// end date is null because cannot set enddate nw will be handledwhen finalize is called
				gslist.add(new GoalSnpsht(snpshtId, json.getString("gs_entpid"), json.getString("gs_goalid"), json.getString("gs_notes"), json.getInt("gs_cost_benefit"),
						null, null/* pre result and post resultfield is not used as of now  */, 
						startDate, null/* end date */));
			gm.setGoalSnpshtList(gslist);

			// here we have a gs.id
			
			// we are going to make an array of t.t.d objects.
			// to do this , getting the array length from one of the sub arrays is enough.
			int ttdArrayLength = json.getJSONArray("usernotes").length();
			
			List<TemplateTaskDetails> ttdList = new ArrayList<TemplateTaskDetails>(ttdArrayLength);

			for(int i =0;i< ttdArrayLength ; i++) {
				Timestamp applyDate = null;
				if (json.getJSONArray("hd_chkApply").get(i).equals("on")) {
					applyDate = new Timestamp(new Date().getTime());
				}
				int s =gm.getGoalSnpshtList().get(0).getSnpshtId();
				ttdList.add(new TemplateTaskDetails(snpshtId , applyDate,  json.getJSONArray("cost_benefit").getInt(i),
						json.getJSONArray("usernotes").get(i).toString() ,json.getJSONArray("systemnotes").get(i).toString() , json.getJSONArray("taskid").getInt(i),
	                    null /* since this is a save , tdescr is not reqd */, 0/* since this is a save , ts_id is not reqd */ , null , null,null,null));
			}
			
			gm.setTemplateTaskDetails(ttdList);
			
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return gm;
	}
}