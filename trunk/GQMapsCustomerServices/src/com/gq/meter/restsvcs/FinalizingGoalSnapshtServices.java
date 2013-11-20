package com.gq.meter.restsvcs;

import java.net.URLDecoder;
import java.text.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.gq.meter.model.FinalizeGoalModel;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.util.CustomerServiceConstant;

@Path("/saveAndFinalize")
public class FinalizingGoalSnapshtServices {

    @Path("/submit")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response finalizetasks(@FormParam("jsonFormData")String string) {
    	
        CustomerServiceConstant.logger.info(" entered rest svc............. Goalmaster object"+string);

        GoalMaster goalmaster = new GoalMaster();
        FinalizeGoalModel goalModel = new FinalizeGoalModel();
		try {
			String result = URLDecoder.decode(string, "UTF-8");
			CustomerServiceConstant.logger.info("before converting to json"+result);
			JSONObject json = new JSONObject(result);
			CustomerServiceConstant.logger.info("before converting to json"+json);

        CustomerServiceConstant.logger.info(" Building Goalmaster object"+string);

        goalmaster = goalModel.buildGoalMasterObj(string);
		CustomerServiceConstant.logger.info("Data before processing snapshot"+goalmaster.getGoalSnpshtList().size()+"task template details"+goalmaster.getTemplateTaskDetails().size());

        //goalmaster = CustomerServiceConstant.gson.fromJson(jsonString, GoalMaster.class);
        //if (json.getString("actionName").equals("save")) {
    		CustomerServiceConstant.logger.info("before save");

            goalModel.CompleteGoal(goalmaster,string);
            CustomerServiceConstant.logger.info("sucessfully completed the save operation");

        //}
        //else if(json.getString("actionName").equals("finalize")) {
        //   goalModel.FinalizeGoal(goalmaster);
         //   CustomerServiceConstant.logger.info("sucessfully completed the finalize operation");

       // }
		} catch (Exception e) {
			CustomerServiceConstant.logger.error("exception occured while processing the data",e);
		}
		
        return Response.ok("sucess").build();

        
    }// end of method
}// end of class
