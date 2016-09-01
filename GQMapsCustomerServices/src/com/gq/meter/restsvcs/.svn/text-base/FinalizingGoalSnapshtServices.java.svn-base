package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.FinalizeGoalModel;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.util.CustomerServiceUtils;

@Path("/saveAndFinalize")
public class FinalizingGoalSnapshtServices {

    @Path("/submit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response finalizetasks(String string) {
    	
        CustomerServiceUtils.logger.debug(" entered rest svc............. Goalmaster object"+string);
        GoalMaster goalmaster = new GoalMaster();
        FinalizeGoalModel goalModel = new FinalizeGoalModel();
		try {
        CustomerServiceUtils.logger.debug(" Building Goalmaster object"+string);

        goalmaster = goalModel.buildGoalMasterObj(string);
		CustomerServiceUtils.logger.debug("Data before processing snapshot"+goalmaster.getGoalSnpshtList().size()+"task template details"+goalmaster.getTemplateTaskDetails().size());

        //goalmaster = CustomerServiceConstant.gson.fromJson(jsonString, GoalMaster.class);
        //if (json.getString("actionName").equals("save")) {
    		CustomerServiceUtils.logger.debug("before save");

    	 goalModel.CompleteGoal(goalmaster,string);
            CustomerServiceUtils.logger.debug("sucessfully completed the save operation");

        //}
        //else if(json.getString("actionName").equals("finalize")) {
        //   goalModel.FinalizeGoal(goalmaster);
         //   CustomerServiceConstant.logger.info("sucessfully completed the finalize operation");

       // }
		} catch (Exception e) {
			CustomerServiceUtils.logger.error("exception occured while processing the data",e);
			return Response.status(400).build();
		}
		return Response.ok(CustomerServiceUtils.gson.toJson("success")).build();
    }// end of method
}// end of class
