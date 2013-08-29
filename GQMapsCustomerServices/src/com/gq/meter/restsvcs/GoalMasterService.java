/**
 * 
 */
package com.gq.meter.restsvcs;

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

import com.gq.meter.object.TaskAssist;
import com.gq.meter.util.CustomerServiceConstant;
import java.sql.Connection;
import com.mysql.jdbc.Statement;

/**
 * @author GQ
 * 
 */
@Path("/goalmaster")
public class GoalMasterService {
    @Path("/goals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGoals() throws ClassNotFoundException {
        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqexchange";
        String username = "gqmaps";
        String password = "Ch1ca803ear$";
        Connection dbCon = null;
        List<TaskAssist> goalArray = new ArrayList<TaskAssist>();
        String goals = "";

        Statement goalStmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            String goalSql = "select goal_id,descr from goal";
            goalStmt = (Statement) dbCon.createStatement();

            ResultSet goalset = goalStmt.executeQuery(goalSql);
            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Query sucessfully Executed from the Goal Table");
            while (goalset.next()) {
                TaskAssist goalData = new TaskAssist();
                String goal_id = goalset.getString("goal_id");
                goalData.setPlain(goal_id);
                String goaldescr = goalset.getString("descr");
                goalData.setDescr(goaldescr);
                goalArray.add(goalData);
            }
            JSONArray goalJsonArray = new JSONArray();
            JSONObject goalTitle = new JSONObject();
            for (int i = 0; i < goalArray.size(); i++) {
                JSONObject assetJson = new JSONObject();
                assetJson.put("goalId", goalArray.get(i).getPlain());
                assetJson.put("goalDescr", goalArray.get(i).getDescr());
                goalJsonArray.put(assetJson);
            }
            goalTitle.put("goalData", goalJsonArray);
            JSONArray result = new JSONArray();
            result.put(goalTitle);
            goals = result.toString();
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  Query sucessfully Executed Objects are constructed for the Goal and added to JSON Array");
        }
        catch (SQLException e) {
            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Exception Occured while fetching the Goals");
        }

        return Response.ok(goals).build();
    }
}
