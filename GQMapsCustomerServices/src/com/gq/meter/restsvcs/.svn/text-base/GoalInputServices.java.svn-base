/**
 * 
 */
package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.GoalInputModel;
import com.gq.meter.object.GoalInput;
import com.gq.meter.util.CustomerServiceUtils;

/**
 * @author rathish
 * 
 */
@Path("GoalInputServices")
public class GoalInputServices {
    @Path("getGoalInput")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGoalInput(@QueryParam("goalId") String goalId) {

        List<GoalInput> goalList = null;
        GoalInputModel goalModel = new GoalInputModel();

        try {
            goalList = goalModel.getGoalInput(goalId);
        }
        catch (Exception e) {

        }
        return Response.ok(CustomerServiceUtils.gson.toJson(goalList)).build();
    }
}