/**
 * 
 */
package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gq.meter.object.TaskMaster;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.Statement;

/**
 * @author GQ
 * 
 */
@Path("/taskmaster")
public class TaskmasterServices {
    @Path("/tasks")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response gettasks() throws ClassNotFoundException {
        Connection dbExchange = null;
        List<TaskMaster> taskArray = new ArrayList<TaskMaster>();
        String tasks = "";

        Statement taskStmt;
        try {
            // get db connections
            dbExchange = SqlUtil.getExchangeConnection();
            String goalSql = "select * from task_tmplt";
            taskStmt = (Statement) dbExchange.createStatement();

            ResultSet taskset = taskStmt.executeQuery(goalSql);
            CustomerServiceConstant.logger.debug("Query sucessfully Executed from the Goal Table");
            while (taskset.next()) {
                TaskMaster taskmaster = new TaskMaster();
                String goal_id = taskset.getString("goal_id");
                int task_id = taskset.getInt("task_id");
                taskmaster.setGoalId(goal_id);
                taskmaster.setTaskId(task_id);
                String goaldescr = taskset.getString("descr");
                String tooltip = taskset.getString("tooltip");
                taskmaster.setDescr(goaldescr);
                taskmaster.setTooltip(tooltip);
                taskArray.add(taskmaster);
            }
            JSONArray taskJsonArray = new JSONArray();
            JSONObject taskTitle = new JSONObject();
            for (int i = 0; i < taskArray.size(); i++) {
                JSONObject assetJson = new JSONObject();
                assetJson.put("goalId", taskArray.get(i).getGoalId());
                assetJson.put("taskId", taskArray.get(i).getTaskId());
                assetJson.put("goalDescr", taskArray.get(i).getDescr());
                assetJson.put("tootlTip", taskArray.get(i).getTooltip());
                taskJsonArray.put(assetJson);
            }
            taskTitle.put("goalData", taskJsonArray);
            JSONArray result = new JSONArray();
            result.put(taskTitle);
            tasks = result.toString();
            CustomerServiceConstant.logger
                    .debug("Query sucessfully Executed Objects are constructed for the Goal and added to JSON Array");
        }
        catch (SQLException e) {
            CustomerServiceConstant.logger.error("Exception Occured while fetching the Goals",e);
        } catch (Exception e) {
        	  CustomerServiceConstant.logger.error("Exception Occured while fetching DBConnection",e);
		}

        return Response.ok(tasks).build();
    }
}
