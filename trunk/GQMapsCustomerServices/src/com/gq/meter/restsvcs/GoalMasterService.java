/**
 * 
 */
package com.gq.meter.restsvcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gq.meter.object.TaskAssist;
import com.gq.meter.util.CustomerServiceUtils;
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGoals(@QueryParam("entpId") String entpId) {
        Connection dbExchange = null;
        List<TaskAssist> goalArray = new ArrayList<TaskAssist>();
        String goalResult = "";

        PreparedStatement goalStmt;
        try {
            dbExchange = SqlUtil.getExchangeConnection();
            String goalSql = "select b.goal_id,a.descr from goal a,enterprise_goal b where a.goal_id=b.goal_id and enterprise_id = ?";
            goalStmt = (PreparedStatement) dbExchange.prepareStatement(goalSql);
            goalStmt.setString(1,entpId);
            ResultSet goalset = goalStmt.executeQuery();
            CustomerServiceUtils.logger.debug(" Query sucessfully Executed from the Goal Table");

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
            CustomerServiceUtils.logger
                    .debug(" Query sucessfully Executed Objects are constructed for the Goal and added to JSON Array");
        }
        catch (SQLException e) {
        	CustomerServiceUtils.logger.error(" Exception Occured while fetching the Goals");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Response.ok(goalResult).build();
    }
}
