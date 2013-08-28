package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
public class GoalSnmsht {
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

    @Path("/goal")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGoalSnpsht(@QueryParam("flag") String flag, String jsonString,
            @QueryParam("entpId") String entpId) {
        GoalSnpsht goalObject = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            // CustomerServiceConstant.logger.info("authString : " + jsonString);
            System.out.println("authString" + jsonString + "flag\t" + flag);
            goalObject = CustomerServiceConstant.gson.fromJson(jsonString, GoalSnpsht.class);
            Timestamp date = new Timestamp(new Date().getTime());
            System.out.println("curr date:" + date);
            if (flag.equalsIgnoreCase("save") && flag != null && flag != " ") {
                System.out.println("data inserting into goal_snpsht");
                String sql = "insert into goal_snpsht (goal_id,enterprise_id,start_date,cost_benefit) values(?,?,?,?);";
                prepStmt = (PreparedStatement) dbCon.prepareStatement(sql);
                System.out.println("objectvalues" + goalObject.toString());
                prepStmt.setString(1, goalObject.getGoalId());
                prepStmt.setString(2, entpId);
                prepStmt.setTimestamp(3, date);
                prepStmt.setString(4, goalObject.getCostBenefit());
                System.out.println("goal_snpsht query" + sql);
                prepStmt.addBatch();
                prepStmt.executeUpdate();
                System.out.println("data sucessfully inserted into goal_snpsht");

                String recentSnpshtId = "select start_date, max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?";
                PreparedStatement stmt = (PreparedStatement) dbCon.prepareStatement(recentSnpshtId);
                stmt.setString(1, entpId);
                ResultSet snpshtSet = stmt.executeQuery(recentSnpshtId);
                System.out.println("goal_snpsht query for date and maxvalue" + recentSnpshtId);

                while (snpshtSet.next()) {
                    snpshtId = snpshtSet.getInt("snpsht_id");
                    System.out.println("snpshtId" + snpshtId);
                    String startdate = snpshtSet.getString("start_date");
                    System.out.println("StartDate" + startdate);
                    entpId = snpshtSet.getString("enterprise_id");
                }

                System.out.println("data inserting into task_chklist");
                Timestamp startDate = new Timestamp(new Date().getTime());
                System.out.println("curr date:" + startDate);
                String tskChkList = "insert into task_chklst (snpsht_id,task_id,goal_id,enterprise_id,apply_date,usr_notes,cost_benefit,sys_notes) values(?,?,?,?,?,?,?,?);";
                PreparedStatement pStmt = (PreparedStatement) dbCon.prepareStatement(tskChkList);
                pStmt.setInt(1, snpshtId);
                pStmt.setString(2, goalObject.getTaskId());
                pStmt.setString(3, goalObject.getGoalId());
                pStmt.setString(4, entpId);
                pStmt.setTimestamp(5, startDate);
                pStmt.setString(6, goalObject.getUserNotes());
                pStmt.setString(7, goalObject.getCostBenefit());
                pStmt.setString(8, goalObject.getSysNotes());
                System.out.println("taskchklist query for date and maxvalue" + tskChkList);
                pStmt.addBatch();

                pStmt.execute();
                System.out.println("data sucessfully inserted into taskChklist");
                result = "values inserted";
            }
            else {
                // todo for finalise
                String snshtIdSql = "select  max(snpsht_id) as snpsht_id from goal_snpsht where enterprise_id=?";
                Statement stmt1 = (Statement) dbCon.createStatement();
                ResultSet snpshtSet1 = stmt1.executeQuery(snshtIdSql);
                System.out.println("goal_snpsht query for date and maxvalue" + snshtIdSql);
                int snshtId = 0;
                while (snpshtSet1.next()) {
                    snshtId = snpshtSet1.getInt("snpsht_id");
                    System.out.println("snpshtId" + snpshtId);
                }
                Timestamp applyDate = new Timestamp(new Date().getTime());
                System.out.println("curr date:" + applyDate);
                String finalize = "update goal_snpsht set end_date=?,notes=? where enterprise_id=? and snpsht_id=?;";
                PreparedStatement preStmt = (PreparedStatement) dbCon.prepareStatement(finalize);
                preStmt.setTimestamp(1, applyDate);
                preStmt.setString(2, goalObject.getNotes());
                preStmt.setInt(3, snshtId);
                preStmt.executeUpdate();
                System.out.println("data sucessfully saved");
            }
        }
        catch (Exception e) {
            System.out.println("exception occured" + e);

        }
        return Response.ok(result).build();
    }
}