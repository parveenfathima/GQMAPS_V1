package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.PreparedStatement;

@Path("/goalSnapshot")
public class GoalSnapshotServices {
    Connection dbExchange = null;
    PreparedStatement prepareStmt = null;
    ResultSet rs = null;
    Date startDate;
    int finalize = 0;
    int snpshtId = 0;
    String result = "";
    String entpId = "";

    @Path("/goal")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGoalSnpsht(@QueryParam("flag") String flag, String jsonString) {
        GoalSnpsht goalObject = null;
        try {
            dbExchange = SqlUtil.getExchangeConnection();
            CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Session Created Sucessfully for GQExchange");
            // CustomerServiceConstant.logger.info("authString : " + jsonString);
            goalObject = CustomerServiceConstant.gson.fromJson(jsonString, GoalSnpsht.class);
            Timestamp currentDate = new Timestamp(new Date().getTime());
            CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Current Date is" + currentDate);
            if (flag.equalsIgnoreCase("save") && flag != null && flag != " ") {
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Data is Inserting into GoalSnapshot");
                String goalSnpshtSql = "insert into goal_snpsht (goal_id,enterprise_id,start_date,cost_benefit) values(?,?,?,?);";
                prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalSnpshtSql);
                prepareStmt.setString(1, goalObject.getGoalId());
                prepareStmt.setString(2, goalObject.getEntpId());
                prepareStmt.setTimestamp(3, currentDate);
                prepareStmt.setString(4, goalObject.getCostBenefit());
                prepareStmt.addBatch();
                prepareStmt.executeUpdate();
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Data Sucessfully Inserted");

                String latestSnpshtId = "select start_date,enterprise_id, max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?;";
                PreparedStatement stmt = (PreparedStatement) dbExchange.prepareStatement(latestSnpshtId);
                stmt.setString(1, goalObject.getEntpId());
                ResultSet snpshtSet = stmt.executeQuery();
                while (snpshtSet.next()) {
                    snpshtId = snpshtSet.getInt("snpsht_id");
                    String startdate = snpshtSet.getString("start_date");
                    entpId = snpshtSet.getString("enterprise_id");
                }

                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES] Data Inserting into TaskChecklist");
                Timestamp startDate = new Timestamp(new Date().getTime());
                System.out.println("curr date:" + startDate);
                String tskChkList = "insert into task_chklst (snpsht_id,task_id,goal_id,enterprise_id,apply_date,usr_notes,cost_benefit,sys_notes) values(?,?,?,?,?,?,?,?);";
                PreparedStatement tskPrepStmt = (PreparedStatement) dbExchange.prepareStatement(tskChkList);
                tskPrepStmt.setInt(1, snpshtId);
                tskPrepStmt.setString(2, goalObject.getTaskId());
                tskPrepStmt.setString(3, goalObject.getGoalId());
                tskPrepStmt.setString(4, goalObject.getEntpId());
                tskPrepStmt.setTimestamp(5, startDate);
                tskPrepStmt.setString(6, goalObject.getUserNotes());
                tskPrepStmt.setString(7, goalObject.getCostBenefit());
                tskPrepStmt.setString(8, goalObject.getSysNotes());
                tskPrepStmt.addBatch();
                tskPrepStmt.execute();
                CustomerServiceConstant.logger
                        .info("[GOALSNAPSHOTSERVICES]  Data Sucessfully inserted into taskchecklist");
                // result = "values inserted";
            }
            else {
                // todo for finalise
                String finalizeSql = "select  max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?";
                PreparedStatement preparefinlStmt = (PreparedStatement) dbExchange.prepareStatement(finalizeSql);
                preparefinlStmt.setString(1, goalObject.getEntpId());
                ResultSet finalizeSnpshtSet = preparefinlStmt.executeQuery();
                int snshtId = 0;
                while (finalizeSnpshtSet.next()) {
                    snshtId = finalizeSnpshtSet.getInt("snpsht_id");
                }
                Timestamp applyDate = new Timestamp(new Date().getTime());
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Apply date is " + applyDate);
                String finalizeUpdateQuery = "update goal_snpsht set end_date=?,notes=? where enterprise_id=? and snpsht_id=?;";
                PreparedStatement prepStmt = (PreparedStatement) dbExchange.prepareStatement(finalizeUpdateQuery);
                prepStmt.setTimestamp(1, applyDate);
                prepStmt.setString(2, goalObject.getNotes());
                prepStmt.setString(3, goalObject.getEntpId());
                prepStmt.setInt(4, snshtId);
                prepStmt.executeUpdate();
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Data Sucessfully inserted");
            }
        }
        catch (Exception e) {
            CustomerServiceConstant.logger
                    .info("[GOALSNAPSHOTSERVICES]  Exception Occured while insertig the Data" + e);

        }
        finally {
            try {
                dbExchange.close();
            }
            catch (SQLException e) {
                CustomerServiceConstant.logger
                        .info("[GOALSNAPSHOTSERVICES]  Exception Occured while closing the Connection" + e);
            }
            CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Session closed Sucessfully GQExchange");
        }
        return Response.ok("SUCESS").build();
    }
}