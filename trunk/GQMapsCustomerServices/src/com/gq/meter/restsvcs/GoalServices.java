package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.gq.meter.object.ChartRowData;
import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Path("/goalServices")
public class GoalServices {
    private GoalMaster goalMaster;

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
        ResultSet entpResultset = null;
        PreparedStatement prepareStmt;
        String goal_Id = "";
        String descr = "";
        String timeBound = "";
        int taskId = 0;
        int tsId = 0;
        String toolTip = "";
        String tsql = "";
        String goalTaskAsst = "";
        String dynamicInput = "";
        String ctId = "";
        String masterGoalSql = "";
        String chartType = "";
        String[] columnHeader = null;
        String relatedDb = "";
        String positionId = "";
        String chartData = "";
        double plainData = 0;
        String plain = "";
        String dynamicChar = "";
        CharSequence renderchart = null;
        String result = "";
        String asset_Id = "";
        String ipAddr = "";
        double serverCost = 0;
        int monthlyRent = 0;
        String perfNotes = "";
        String goalDescr = "";
        // Goal goal = new Goal(goal_Id, taskId, chartData, plainData, plain, chartType, positionId, descr);
        List<Goal> taskList = new ArrayList<Goal>(20);
        // List<GoalSnpsht> goalSnpshtList = new ArrayList<GoalSnpsht>();
        goalMaster = new GoalMaster();
        try {
            dbExchange = SqlUtil.getExchangeConnection();
            dbCustomer = SqlUtil.getCustomerConnection(entpId);
            masterGoalSql = "select goal_id,descr,time_bound,perf_notes from goal where goal_id=?;";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(masterGoalSql);
            prepareStmt.setString(1, goalId);
            rs = prepareStmt.executeQuery();
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the Goal Table");
            while (rs.next()) {
                goal_Id = rs.getString("goal_id");
                goalDescr = rs.getString("descr");
                timeBound = rs.getString("time_bound");
                perfNotes = rs.getString("perf_notes");
            }
            Goal goal = new Goal();
            goal.setGoal_Id(goal_Id);
            goal.setGoalDescr(goalDescr);
            goal.setPerfNotes(perfNotes);
            goal.setTimeBound(timeBound);
            goalMaster.setGoal(goal);

            String goalTmpltSql = "select * from task_tmplt where goal_id=? and ts_id is not null;";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalTmpltSql);
            prepareStmt.setString(1, goal_Id);
            ResultSet goalTmpltSet = prepareStmt.executeQuery();
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the TaskTmpltTable");
            while (goalTmpltSet.next()) {
                goal_Id = goalTmpltSet.getString("goal_id");
                taskId = goalTmpltSet.getInt("task_id");
                descr = goalTmpltSet.getString("descr");
                tsId = goalTmpltSet.getInt("ts_id");
                if (goalTmpltSet.getString("tooltip") != null && goalTmpltSet.getString("tooltip").trim() != "") {
                    toolTip = goalTmpltSet.getString("tooltip");
                }
                else {
                    toolTip = "NA";
                }
                Goal goalObj = new Goal();
                goalObj.setGoal_Id(goal_Id);
                goalObj.setTaskId(taskId);
                goalObj.setDescr(descr);
                toolTip = goalTmpltSet.getString("tooltip");
                String goalTaskAsstSql = "select * from task_asst where ts_id=?;";
                prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalTaskAsstSql);
                prepareStmt.setInt(1, tsId);
                CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the TaskAsst Table for "
                        + goal_Id + " Goal");
                ResultSet goalTaskAsstSet = prepareStmt.executeQuery();
                while (goalTaskAsstSet.next()) {
                    tsId = goalTaskAsstSet.getInt("ts_id");
                    descr = goalTaskAsstSet.getString("descr");
                    tsql = goalTaskAsstSet.getString("tsql");
                    dynamicInput = goalTaskAsstSet.getString("dynamic");
                    ctId = goalTaskAsstSet.getString("ct_id");
                    relatedDb = goalTaskAsstSet.getString("relatd_db");
                    String colHeader[] = null;
                    if (goalTaskAsstSet.getString("col_hdr") != null
                            && goalTaskAsstSet.getString("col_hdr").trim() != "") {
                        if ((goalTaskAsstSet.getString("col_hdr").split(",").length) >= 2) {
                            colHeader = goalTaskAsstSet.getString("col_hdr").split(",");
                        }
                        else {
                            colHeader[0] = "NA";
                            colHeader[1] = "NA";
                        }
                    }
                    String chartquery = "select ct_Id,descr from chart_type where ct_id=? and ct_Id is not null;";
                    goalObj.setChartType(goalTaskAsstSet.getString("ct_id"));
                    prepareStmt = (PreparedStatement) dbExchange.prepareStatement(chartquery);
                    prepareStmt.setString(1, goalObj.setChartType(goalTaskAsstSet.getString("ct_id")));
                    ResultSet taskAsstChartType = prepareStmt.executeQuery();
                    CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the chart Table");
                    while (taskAsstChartType.next()) {
                        chartType = taskAsstChartType.getString("ct_id");
                        String descrp = taskAsstChartType.getString("descr");
                    }
                    positionId = goalTaskAsstSet.getString("pos_id");
                    goalObj.setPositionId(positionId);
                    goalObj.setChartType(chartType);
                    if (goalId.equals("mon")) {
                        String newSql = tsql.replace(" __asset_id__", assetId);
                        goalTaskAsst = newSql.replace("__date__ ", recDtt);
                        tsql = goalTaskAsst;
                    }
                    if (dynamicInput.equals("y")) {
                        String entpquery = tsql;
                        dynamicChar = entpquery.replace("__filter", "?");
                        String finalString = dynamicChar.replaceAll("[']", "");
                        String resultString = finalString.replaceAll("[\"]", "'");
                        CustomerServiceConstant.logger.info("[GOALSERVICES] Dynamic Filter Value replaced");
                        if (!relatedDb.equalsIgnoreCase("e")) {
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]  Query with Dynamic value executing for Enterprise" + entpId);
                            prepareStmt = (PreparedStatement) dbCustomer.prepareStatement(resultString);
                            prepareStmt.setString(1, entpId);
                            entpResultset = prepareStmt.executeQuery();
                        }
                        else {
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]   Query with Dynamic value executing for Exchange");
                            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(resultString);
                            prepareStmt.setString(1, entpId);
                            entpResultset = prepareStmt.executeQuery();
                        }
                    }
                    else {
                        if (!relatedDb.equalsIgnoreCase("e")) {
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]   Query For non Dynamic value executing for Enterprise"
                                            + entpId);
                            stmt = (Statement) dbCustomer.prepareStatement(tsql);
                            // Resultset returned by query
                            entpResultset = stmt.executeQuery(tsql);
                        }
                        else {
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]   Query for non Dynamic value executing for Exchange");
                            stmt = (Statement) dbExchange.prepareStatement(tsql);
                            // Resultset returned by query
                            entpResultset = stmt.executeQuery(tsql);
                        }
                    }

                    ResultSetMetaData rsMetaData = entpResultset.getMetaData();
                    int metaDataColumnCount = rsMetaData.getColumnCount();
                    DataTable chartdata = new DataTable();
                    if (chartType.equals("bar") || chartType.equals("pie")) {
                        ArrayList<ColumnDescription> pieBarColumn = new ArrayList<ColumnDescription>();
                        CustomerServiceConstant.logger.info("[GOALSERVICES]  Columns are being set for BAR/PIE charts");
                        pieBarColumn.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
                        pieBarColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                        chartdata.addColumns(pieBarColumn);
                    }
                    else if (chartType.equals("line")) {
                        ArrayList<ColumnDescription> lineColumn = new ArrayList<ColumnDescription>();
                        CustomerServiceConstant.logger
                                .info("[GOALSERVICES]  Columns are being set for Annotated TimeLine charts");
                        lineColumn.add(new ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0]));
                        lineColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                        chartdata.addColumns(lineColumn);
                    }
                    List<ChartRowData> cDataList = new ArrayList<ChartRowData>();
                    while (entpResultset.next()) {
                        ChartRowData cData = new ChartRowData();
                        for (int i = 1; i <= metaDataColumnCount; i++) {
                            int type = rsMetaData.getColumnType(i);
                            if (type == Types.VARCHAR || type == Types.CHAR) {
                                cData.setName(entpResultset.getString(i));
                            }
                            else if (type == Types.TIMESTAMP) {
                                cData.setName(entpResultset.getString(i));
                            }
                            else {
                                cData.setValue(entpResultset.getDouble(i));
                            }
                        }
                        CustomerServiceConstant.logger
                                .info("[GOALSERVICES]  Dynamic Column data's are added to the List ");
                        cDataList.add(cData);
                    }
                    if (chartType.equals("bar") || chartType.equals("pie")) {
                        CustomerServiceConstant.logger.info("[GOALSERVICES]  Rows are being set for BAR/PIE charts");
                        for (int i = 0; i < cDataList.size(); i++) {
                            chartdata.addRowFromValues(cDataList.get(i).getName(), cDataList.get(i).getValue());
                        }
                    }
                    else if (chartType.equals("line")) {
                        CustomerServiceConstant.logger
                                .info("[GOALSERVICES]  Rows are being set for AnnotatedTimeLine charts");
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                        for (int i = 0; i < cDataList.size(); i++) {
                            String other = cDataList.get(i).getName();
                            int year = Integer.parseInt(other.substring(0, 4));
                            int month = Integer.parseInt(other.substring(5, 7));
                            int date = Integer.parseInt(other.substring(8, 10));
                            int hours = Integer.parseInt(other.substring(11, 13));
                            int minutes = Integer.parseInt(other.substring(14, 16));
                            int seconds = Integer.parseInt(other.substring(17, 19));
                            calendar.set(year, month, date, hours, minutes, seconds);
                            chartdata.addRowFromValues(calendar, cDataList.get(i).getValue());
                        }
                    }
                    CustomerServiceConstant.logger.info("[GOALSERVICES]  ArrowFormat data's are being constructed");
                    renderchart = JsonRenderer.renderDataTable(chartdata, true, true);
                    goalObj.setChartData(renderchart);
                    if (chartType.equals("plain")) {
                        CustomerServiceConstant.logger.info("[GOALSERVICES]  Dynamic Data Rows with are Plain Text");
                        if (cDataList.size() > 0) {
                            if (cDataList.get(0).getName() != null && cDataList.get(0).getName() != " ") {
                                goalObj.setPlain(cDataList.get(0).getName());
                            }
                            else if (cDataList.get(0).getValue() != 0) {
                                goalObj.setPlainData(cDataList.get(0).getValue());
                            }
                        }
                        else {
                            goalObj.setPlainData(0);
                        }
                    }
                    CustomerServiceConstant.logger
                            .info("[GOALSERVICES]  All the Constructed Objects are added to list");
                    taskList.add(goalObj);
                }
            }

            goalMaster.setGoalList(taskList);
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Asset JSON is Constructed");
            String goalSnpshtQuery = "select snpsht_id as SnapshotId, start_date as StartDate, "
                    + "end_date as EndDate, notes as Notes,cost_benefit as RealizedBenefit from goal_snpsht where goal_id=?";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalSnpshtQuery);
            prepareStmt.setString(1, goalId);

            ResultSet chkSet = prepareStmt.executeQuery();
            CustomerServiceConstant.logger.info("[GOALSERVICES] Query Sucessfully Executed for the Goal Snapshot");
            List<GoalSnpsht> gsList = new ArrayList<GoalSnpsht>(20);

            while (chkSet.next()) {
                GoalSnpsht goalSnpsht = new GoalSnpsht();
                goalSnpsht.setSnpshtId(chkSet.getInt("SnapshotId"));
                goalSnpsht.setStartDate(chkSet.getTimestamp("StartDate"));
                goalSnpsht.setEndDate(chkSet.getTimestamp("EndDate"));
                goalSnpsht.setNotes(chkSet.getString("Notes"));
                goalSnpsht.setCostBenefit(chkSet.getString("RealizedBenefit"));
                gsList.add(goalSnpsht);
            }
            goalMaster.setGoalSnpshtList(gsList);

            result = CustomerServiceConstant.gson.toJson(goalMaster);
        }
        catch (SQLException ex) {
            CustomerServiceConstant.logger
                    .error("[GOALSERVICES]  Execption Occured while Executing Goal Services" + ex);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error("[GOALSERVICES]  Execption Occured " + e);
        }
        finally {
            // close connection
            try {
                dbExchange.close();
                dbCustomer.close();
            }
            catch (SQLException e) {
                CustomerServiceConstant.logger.error("[GOALSERVICES]  Execption Occured while closing session" + e);
            }

            CustomerServiceConstant.logger.info("[GOALSERVICES]  Session Closed sucessfully");
        }
        CustomerServiceConstant.logger.info("[GOALSERVICES]  GOAL Services Executed Sucessfully");
        return Response.ok().entity(result).build();
    }
}