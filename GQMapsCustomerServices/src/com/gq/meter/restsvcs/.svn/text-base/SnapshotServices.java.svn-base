package com.gq.meter.restsvcs;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.SnapshotModel;
import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.object.TaskCheckList;
import com.gq.meter.object.TemplateTaskDetails;
import com.gq.meter.util.CustomerServiceUtils;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/getGoalSnapshot")
public class SnapshotServices {
	
    @Path("/goalSnapshot")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSnapshot(@QueryParam("snapShtId") int snpshtId) {
    	
        GoalMaster goalMaster = new GoalMaster();
    	SnapshotModel snpshtModel = new SnapshotModel();
    	
    	//1. taskchk list for retrieving the details based on the snapshotid
    	List<TemplateTaskDetails> chkList = snpshtModel.getSnapshot(snpshtId);
    	goalMaster.setTemplateTaskDetails(chkList);
    	CustomerServiceUtils.logger.debug("TaskchkList Data sucessfully set to the GoalMaster");
    	
    	 // 2.build goal snapshot
    	List<GoalSnpsht> snpshtList = snpshtModel.getGoalSnapshot(snpshtId);
    	goalMaster.setGoalSnpshtList(snpshtList);
    	
    	//3.build goal object
    	Goal goal = snpshtModel.getGoal();
    	goalMaster.setGoal(goal);
        return Response.ok(CustomerServiceUtils.gson.toJson(goalMaster)).build();

    }
}
