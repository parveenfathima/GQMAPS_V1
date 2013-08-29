package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
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
import com.mysql.jdbc.PreparedStatement;

@Path("/goalSnapshot")
public class GoalSnapshotServices {
    String dbURL = "jdbc:mysql://192.168.1.95:3306/gqexchange";
    String username = "gqmaps";
    String password = "Ch1ca803ear$";
    Connection dbCon = null;
    PreparedStatement prepStmt = null;
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
    public Response addGoalSnpsht(@QueryParam("flag") String flag, String jsonString) throws SQLException {
        GoalSnpsht goalObject = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Session Created Sucessfully for GQExchange");
            // CustomerServiceConstant.logger.info("authString : " + jsonString);
            goalObject = CustomerServiceConstant.gson.fromJson(jsonString, GoalSnpsht.class);
            Timestamp date = new Timestamp(new Date().getTime());
            CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Current Date is" + date);
            if (flag.equalsIgnoreCase("save") && flag != null && flag != " ") {
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Data is Inserting into GoalSnapshot");
                String sql = "insert into goal_snpsht (goal_id,enterprise_id,start_date,cost_benefit) values(?,?,?,?);";
                prepStmt = (PreparedStatement) dbCon.prepareStatement(sql);
                System.out.println("objectvalues" + goalObject.toString());
                prepStmt.setString(1, goalObject.getGoalId());
                prepStmt.setString(2, goalObject.getEntpId());
                prepStmt.setTimestamp(3, date);
                prepStmt.setString(4, goalObject.getCostBenefit());
                System.out.println("goal_snpsht query" + sql);
                prepStmt.addBatch();
                prepStmt.executeUpdate();
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Data Sucessfully Inserted");

                String recentSnpshtId = "select start_date,enterprise_id, max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?;";
                PreparedStatement stmt = (PreparedStatement) dbCon.prepareStatement(recentSnpshtId);
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
                PreparedStatement pStmt = (PreparedStatement) dbCon.prepareStatement(tskChkList);
                pStmt.setInt(1, snpshtId);
                pStmt.setString(2, goalObject.getTaskId());
                pStmt.setString(3, goalObject.getGoalId());
                pStmt.setString(4, goalObject.getEntpId());
                pStmt.setTimestamp(5, startDate);
                pStmt.setString(6, goalObject.getUserNotes());
                pStmt.setString(7, goalObject.getCostBenefit());
                pStmt.setString(8, goalObject.getSysNotes());
                pStmt.addBatch();
                pStmt.execute();
                CustomerServiceConstant.logger
                        .info("[GOALSNAPSHOTSERVICES]  Data Sucessfully inserted into taskchecklist");
                // result = "values inserted";
            }
            else {
                // todo for finalise
                String snshtIdSql = "select  max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?";
                PreparedStatement stmt1 = (PreparedStatement) dbCon.prepareStatement(snshtIdSql);
                stmt1.setString(1, goalObject.getEntpId());
                ResultSet snpshtSet1 = stmt1.executeQuery();
                int snshtId = 0;
                while (snpshtSet1.next()) {
                    snshtId = snpshtSet1.getInt("snpsht_id");
                }
                Timestamp applyDate = new Timestamp(new Date().getTime());
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Apply date is " + applyDate);
                String finalize = "update goal_snpsht set end_date=?,notes=? where enterprise_id=? and snpsht_id=?;";
                PreparedStatement preStmt = (PreparedStatement) dbCon.prepareStatement(finalize);
                preStmt.setTimestamp(1, applyDate);
                preStmt.setString(2, goalObject.getNotes());
                preStmt.setString(3, goalObject.getEntpId());
                preStmt.setInt(4, snshtId);
                preStmt.executeUpdate();
                CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Data Sucessfully inserted");
            }
        }
        catch (Exception e) {
            CustomerServiceConstant.logger
                    .info("[GOALSNAPSHOTSERVICES]  Exception Occured while insertig the Data" + e);

        }
        finally {
            dbCon.close();
            CustomerServiceConstant.logger.info("[GOALSNAPSHOTSERVICES]  Session closed Sucessfully GQExchange");

        }
        return Response.ok("SUCESS").build();
    }
}