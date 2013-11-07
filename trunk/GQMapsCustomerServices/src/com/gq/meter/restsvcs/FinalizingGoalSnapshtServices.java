package com.gq.meter.restsvcs;

import java.text.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.gq.meter.model.FinalizeGoalModel;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.util.CustomerServiceConstant;

@Path("/saveAndFinalize")
public class FinalizingGoalSnapshtServices {

    @Path("/submit")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response finalizetasks(String jsonString) {
        GoalMaster goalmaster = new GoalMaster();
        FinalizeGoalModel goalModel = new FinalizeGoalModel();
		try {
			JSONObject json = new JSONObject(jsonString);
		

        goalmaster = goalModel.buildGoalMasterObj(jsonString);
        //goalmaster = CustomerServiceConstant.gson.fromJson(jsonString, GoalMaster.class);
        if (json.getString("actionName").equals("save")) {
            goalModel.CompleteGoal(goalmaster);
        }
        else if(json.getString("actionName").equals("finalize")) {
            goalModel.FinalizeGoal(goalmaster);
        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.ok("sucess").build();

        
    }// end of method
}// end of class
