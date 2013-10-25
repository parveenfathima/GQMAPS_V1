/**
 * 
 */
package com.gq.meter.restsvcs;

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

import com.gq.meter.object.TaskAssist;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;

import java.sql.Connection;
import com.mysql.jdbc.Statement;

/**
 * @author rathish
 * 
 */

@Path("/goalmaster")
public class GoalMasterService {
    @Path("/goals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGoals() {
        Connection dbExchange = null;
        List<TaskAssist> goalArray = new ArrayList<TaskAssist>();
        String goalResult = "";

        Statement goalStmt;
        try {
            dbExchange = SqlUtil.getExchangeConnection();
            String goalSql = "select goal_id,descr from goal";
            goalStmt = (Statement) dbExchange.createStatement();
            ResultSet goalset = goalStmt.executeQuery(goalSql);
            CustomerServiceConstant.logger.info(" Query sucessfully Executed from the Goal Table");

            while (goalset.next()) {
                TaskAssist taskAssistObj = new TaskAssist();
                String goal_id = goalset.getString("goal_id");
                taskAssistObj.setPlain(goal_id);
                String goaldescr = goalset.getString("descr");
                taskAssistObj.setDescr(goaldescr);
                goalArray.add(taskAssistObj);
            }

            JSONArray goalJsonArray = new JSONArray();
            JSONObject goalDataTitle = new JSONObject();

            for (int i = 0; i < goalArray.size(); i++) {
                JSONObject assetJson = new JSONObject();
                assetJson.put("goalId", goalArray.get(i).getPlain());
                assetJson.put("goalDescr", goalArray.get(i).getDescr());
                goalJsonArray.put(assetJson);
            }

            goalDataTitle.put("goalData", goalJsonArray);
            JSONArray result = new JSONArray();
            result.put(goalDataTitle);
            goalResult = result.toString();
            CustomerServiceConstant.logger
                    .info(" Query sucessfully Executed Objects are constructed for the Goal and added to JSON Array");
        }
        catch (SQLException e) {
            CustomerServiceConstant.logger.info(" Exception Occured while fetching the Goals");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Response.ok(goalResult).build();
    }
}
