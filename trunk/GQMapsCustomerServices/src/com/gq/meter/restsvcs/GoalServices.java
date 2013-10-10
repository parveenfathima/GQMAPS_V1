package com.gq.meter.restsvcs;

import java.sql.Connection;
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

import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.object.TemplateTaskDetails;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Path("/goalServices")
public class GoalServices {

    @Path("/goal")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGoals(@QueryParam("goalId") String goalId, @QueryParam("entpId") String entpId,
            @QueryParam("assetId") String assetId, @QueryParam("recDttm") String recDtt) {

        Connection dbExchange = null;
        Connection dbCustomer = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement prepareStmt;
        String masterGoalSql = "";
        String result = "";

        GoalMaster goalMaster = new GoalMaster();

        try {
            // get db connections
            dbExchange = SqlUtil.getExchangeConnection();
            dbCustomer = SqlUtil.getCustomerConnection(entpId);

            // build the goal object
            masterGoalSql = "select goal_id,descr,time_bound,perf_notes from goal where goal_id=?;";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(masterGoalSql);
            prepareStmt.setString(1, goalId);
            rs = prepareStmt.executeQuery();
            CustomerServiceConstant.logger.info(" Query Executed for the Goal Table");
            Goal goal = null;

            if (rs.next()) {

                goal = new Goal(rs.getString("goal_id"), rs.getString("descr"), rs.getString("perf_notes"),
                        rs.getString("time_bound"), null); // last arg image is set to null for now - ss oct 10,13
            }
            else {
                CustomerServiceConstant.logger.error("No Goal row found in table");
                return null;
            }

            goalMaster.setGoal(goal);

            // build goal snapshot

            String goalSnpshtQuery = "select snpsht_id as SnapshotId, start_date as StartDate, "
                    + " end_date as EndDate, notes as Notes,cost_benefit as RealizedBenefit , pre_result , post_result "
                    + " from goal_snpsht where goal_id=? and enterprise_id=? and end_date is not NULL ";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalSnpshtQuery);
            prepareStmt.setString(1, goalId);
            prepareStmt.setString(2, entpId);

            ResultSet chkSet = prepareStmt.executeQuery();
            CustomerServiceConstant.logger.info(" Query Sucessfully Executed for the Goal Snapshot");
            List<GoalSnpsht> gsList = new ArrayList<GoalSnpsht>(20);

            while (chkSet.next()) {

                gsList.add(new GoalSnpsht(chkSet.getInt("SnapshotId"), "not-obt", "not-obt", chkSet.getString("Notes"),
                        chkSet.getInt("RealizedBenefit"), chkSet.getString("pre_result"), chkSet
                                .getString("post_result"), chkSet.getTimestamp("StartDate"), chkSet
                                .getTimestamp("EndDate")));
            }
            goalMaster.setGoalSnpshtList(gsList);

            // Task Template details for the particular task

            String taskTmpltQuery = "SELECT  gs.snpsht_id  , tc.apply_date , tc.cost_benefit , tc.usr_notes , "
                    + " tc.sys_notes , tt.task_id , tt.descr , tt.ts_id , tt.tooltip "
                    + " FROM  gqexchange.task_tmplt tt inner join  gqexchange.goal_snpsht gs using ( goal_id ) "
                    + " left join gqexchange.task_chklst tc on  tt.task_id = tc.task_id "
                    + "  and gs.snpsht_id = tc.snpsht_id where gs.enterprise_id =?  "
                    + " and gs.goal_id =? and gs.end_date is null";

            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(taskTmpltQuery);
            prepareStmt.setString(1, entpId);
            prepareStmt.setString(2, goalId);

            System.out.println("finaltskquery::" + taskTmpltQuery);
            ResultSet taskTmpltset = prepareStmt.executeQuery();

            List<TemplateTaskDetails> templateTaskDetails = new ArrayList<TemplateTaskDetails>(10);

            while (taskTmpltset.next()) {
                templateTaskDetails.add(new TemplateTaskDetails(taskTmpltset.getInt("snpsht_id"), taskTmpltset
                        .getTimestamp("apply_date"), taskTmpltset.getInt("cost_benefit"), taskTmpltset
                        .getString("usr_notes"), taskTmpltset.getString("sys_notes"), taskTmpltset.getInt("task_id"),
                        taskTmpltset.getString("descr"), taskTmpltset.getInt("ts_id"), taskTmpltset
                                .getString("tooltip"), null));// chartdata is set to null since no data as of now 10-oct
            }

            goalMaster.setTemplateTaskDetails(templateTaskDetails);

            result = CustomerServiceConstant.gson.toJson(goalMaster);
        }
        catch (SQLException ex) {
            CustomerServiceConstant.logger.error(" Execption Occured while Executing Goal Services" + ex);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error(" Execption Occured " + e);
        }
        finally {
            // close connection
            try {
                dbExchange.close();
                dbCustomer.close();
            }
            catch (SQLException e) {
                CustomerServiceConstant.logger.error(" Execption Occured while closing session" + e);
            }

            CustomerServiceConstant.logger.info("Session Closed sucessfully");
        }
        CustomerServiceConstant.logger.info("GOAL Services Executed Sucessfully");
        return Response.ok().entity(result).build();

    } // end of method
} // end of class

/*
 * String goalTmpltSql = "select * from task_tmplt where goal_id=? "; prepareStmt = (PreparedStatement)
 * dbExchange.prepareStatement(goalTmpltSql); prepareStmt.setString(1, goal_Id); ResultSet goalTmpltSet =
 * prepareStmt.executeQuery(); CustomerServiceConstant.logger.info(" Query Executed for the TaskTmpltTable");
 * 
 * while (goalTmpltSet.next()) { goal_Id = goalTmpltSet.getString("goal_id"); taskId = goalTmpltSet.getInt("task_id");
 * descr = goalTmpltSet.getString("descr"); tsId = goalTmpltSet.getInt("ts_id");
 * 
 * if (goalTmpltSet.getString("tooltip") != null && goalTmpltSet.getString("tooltip").trim() != "") { toolTip =
 * goalTmpltSet.getString("tooltip"); } else { toolTip = "NA"; } Goal goalObj = new Goal(); goalObj.getGoal_Id(goal_Id);
 * goalObj.setTaskId(taskId); goalObj.getDescr(descr); toolTip = goalTmpltSet.getString("tooltip"); String
 * goalTaskAsstSql = "select * from task_asst where ts_id=?;"; prepareStmt = (PreparedStatement)
 * dbExchange.prepareStatement(goalTaskAsstSql); prepareStmt.setInt(1, tsId);
 * CustomerServiceConstant.logger.info(" Query Executed for the TaskAsst Table for " + goal_Id + " Goal"); ResultSet
 * goalTaskAsstSet = prepareStmt.executeQuery(); while (goalTaskAsstSet.next()) { tsId =
 * goalTaskAsstSet.getInt("ts_id"); descr = goalTaskAsstSet.getString("descr"); tsql =
 * goalTaskAsstSet.getString("tsql"); dynamicInput = goalTaskAsstSet.getString("dynamic"); ctId =
 * goalTaskAsstSet.getString("ct_id"); relatedDb = goalTaskAsstSet.getString("relatd_db"); String colHeader[] = null; if
 * (goalTaskAsstSet.getString("col_hdr") != null && goalTaskAsstSet.getString("col_hdr").trim() != "") { if
 * ((goalTaskAsstSet.getString("col_hdr").split(",").length) >= 2) { colHeader =
 * goalTaskAsstSet.getString("col_hdr").split(","); } else { colHeader[0] = "NA"; colHeader[1] = "NA"; } } String
 * chartquery = "select ct_Id,descr from chart_type where ct_id=? and ct_Id is not null;";
 * goalObj.setChartType(goalTaskAsstSet.getString("ct_id")); prepareStmt = (PreparedStatement)
 * dbExchange.prepareStatement(chartquery); prepareStmt.setString(1,
 * goalObj.setChartType(goalTaskAsstSet.getString("ct_id"))); ResultSet taskAsstChartType = prepareStmt.executeQuery();
 * CustomerServiceConstant.logger.info(" Query Executed for the chart Table"); while (taskAsstChartType.next()) {
 * chartType = taskAsstChartType.getString("ct_id"); String descrp = taskAsstChartType.getString("descr"); } positionId
 * = goalTaskAsstSet.getString("pos_id"); goalObj.setPositionId(positionId); goalObj.setChartType(chartType); if
 * (goalId.equals("mon")) { String newSql = tsql.replace(" __asset_id__", assetId); goalTaskAsst =
 * newSql.replace("__date__ ", recDtt); tsql = goalTaskAsst; } if (dynamicInput.equals("y")) { String entpquery = tsql;
 * dynamicChar = entpquery.replace("__filter", "?"); String finalString = dynamicChar.replaceAll("[']", ""); String
 * resultString = finalString.replaceAll("[\"]", "'");
 * CustomerServiceConstant.logger.info(" Dynamic Filter Value replaced"); if (!relatedDb.equalsIgnoreCase("e")) {
 * CustomerServiceConstant.logger.info(" Query with Dynamic value executing for Enterprise" + entpId); prepareStmt =
 * (PreparedStatement) dbCustomer.prepareStatement(resultString); prepareStmt.setString(1, entpId); entpResultset =
 * prepareStmt.executeQuery(); } else {
 * CustomerServiceConstant.logger.info(" Query with Dynamic value executing for Exchange"); prepareStmt =
 * (PreparedStatement) dbExchange.prepareStatement(resultString); prepareStmt.setString(1, entpId); entpResultset =
 * prepareStmt.executeQuery(); } } else { if (!relatedDb.equalsIgnoreCase("e")) {
 * CustomerServiceConstant.logger.info(" Query For non Dynamic value executing for Enterprise" + entpId); stmt =
 * (Statement) dbCustomer.prepareStatement(tsql); // Resultset returned by query entpResultset =
 * stmt.executeQuery(tsql); } else {
 * CustomerServiceConstant.logger.info(" Query for non Dynamic value executing for Exchange"); stmt = (Statement)
 * dbExchange.prepareStatement(tsql); // Resultset returned by query entpResultset = stmt.executeQuery(tsql); } }
 * 
 * ResultSetMetaData rsMetaData = entpResultset.getMetaData(); int metaDataColumnCount = rsMetaData.getColumnCount();
 * DataTable chartdata = new DataTable(); if (chartType.equals("bar") || chartType.equals("pie")) {
 * ArrayList<ColumnDescription> pieBarColumn = new ArrayList<ColumnDescription>();
 * CustomerServiceConstant.logger.info("[GOALSERVICES]  Columns are being set for BAR/PIE charts"); pieBarColumn.add(new
 * ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0])); pieBarColumn.add(new ColumnDescription(colHeader[1],
 * ValueType.NUMBER, colHeader[1])); chartdata.addColumns(pieBarColumn); } else if (chartType.equals("line")) {
 * ArrayList<ColumnDescription> lineColumn = new ArrayList<ColumnDescription>();
 * CustomerServiceConstant.logger.info(" Columns are being set for Annotated TimeLine charts"); lineColumn.add(new
 * ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0])); lineColumn.add(new
 * ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1])); chartdata.addColumns(lineColumn); }
 * List<ChartRowData> cDataList = new ArrayList<ChartRowData>(); while (entpResultset.next()) { ChartRowData cData = new
 * ChartRowData(); for (int i = 1; i <= metaDataColumnCount; i++) { int type = rsMetaData.getColumnType(i); if (type ==
 * Types.VARCHAR || type == Types.CHAR) { cData.setName(entpResultset.getString(i)); } else if (type == Types.TIMESTAMP)
 * { cData.setName(entpResultset.getString(i)); } else { cData.setValue(entpResultset.getDouble(i)); } }
 * CustomerServiceConstant.logger .info("[GOALSERVICES]  Dynamic Column data's are added to the List ");
 * cDataList.add(cData); } if (chartType.equals("bar") || chartType.equals("pie")) {
 * CustomerServiceConstant.logger.info(" Rows are being set for BAR/PIE charts"); for (int i = 0; i < cDataList.size();
 * i++) { chartdata.addRowFromValues(cDataList.get(i).getName(), cDataList.get(i).getValue()); } } else if
 * (chartType.equals("line")) { CustomerServiceConstant.logger.info(" Rows are being set for AnnotatedTimeLine charts");
 * GregorianCalendar calendar = new GregorianCalendar(); calendar.setTimeZone(TimeZone.getTimeZone("GMT")); for (int i =
 * 0; i < cDataList.size(); i++) { String other = cDataList.get(i).getName(); int year =
 * Integer.parseInt(other.substring(0, 4)); int month = Integer.parseInt(other.substring(5, 7)); int date =
 * Integer.parseInt(other.substring(8, 10)); int hours = Integer.parseInt(other.substring(11, 13)); int minutes =
 * Integer.parseInt(other.substring(14, 16)); int seconds = Integer.parseInt(other.substring(17, 19));
 * calendar.set(year, month, date, hours, minutes, seconds); chartdata.addRowFromValues(calendar,
 * cDataList.get(i).getValue()); } } CustomerServiceConstant.logger.info(" ArrowFormat data's are being constructed");
 * renderchart = JsonRenderer.renderDataTable(chartdata, true, true); goalObj.setChartData(renderchart); if
 * (chartType.equals("plain")) { CustomerServiceConstant.logger.info(" Dynamic Data Rows with are Plain Text"); if
 * (cDataList.size() > 0) { if (cDataList.get(0).getName() != null && cDataList.get(0).getName() != " ") {
 * goalObj.setPlain(cDataList.get(0).getName()); } else if (cDataList.get(0).getValue() != 0) {
 * goalObj.setPlainData(cDataList.get(0).getValue()); } } else { goalObj.setPlainData(0); } }
 * CustomerServiceConstant.logger .info("[GOALSERVICES]  All the Constructed Objects are added to list");
 * taskList.add(goalObj); } }
 * 
 * goalMaster.setGoalList(taskList); CustomerServiceConstant.logger.info(" Asset JSON is Constructed");
 */