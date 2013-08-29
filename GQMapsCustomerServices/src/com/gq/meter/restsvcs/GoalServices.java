package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.gq.meter.object.AssetData;
import com.gq.meter.object.Data;
import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.util.CustomerServiceConstant;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Path("/goalServices")
public class GoalServices {
    @Path("/goal")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGoals(@QueryParam("goalId") String goalId, @QueryParam("entpId") String entpId)
            throws SQLException, ClassNotFoundException, TypeMismatchException {
        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqexchange";
        String username = "gqmaps";
        String password = "Ch1ca803ear$";
        String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqm" + entpId;
        // public use
        // String dbURL = "jdbc:mysql://182.72.206.38:3306/gqexchange";
        // String username = "gqmaps";
        // String password = "Ch1ca803ear$";
        // String dbURL1 = "jdbc:mysql://182.72.206.38:3306/gqmaps";
        Connection dbCon = null;
        Connection dbCon1 = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet entpResultset = null;
        PreparedStatement pstmt;
        String goal_Id = "";
        String descr = "";
        String timeBound = "";
        int taskId = 0;
        int tsId = 0;
        String toolTip = "";
        String tsql = "";
        String dynamic = "";
        String ctId = "";
        String sql = "";
        String chartType = "";
        String[] columnHeader = null;
        String relatedDb = "";
        String positionId = "";
        List<Data> data = null;
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
        Goal goal = new Goal(goal_Id, taskId, chartData, plainData, plain, chartType, positionId, descr);
        List<Goal> sqlList = new ArrayList<Goal>();
        List<AssetData> assetArray = new ArrayList<AssetData>();
        List<GoalSnpsht> goalArray = new ArrayList<GoalSnpsht>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            dbCon1 = DriverManager.getConnection(dbURL1, username, password);
            sql = "select goal_id,descr,time_bound from goal where goal_id=?;";
            pstmt = (PreparedStatement) dbCon.prepareStatement(sql);
            pstmt.setString(1, goalId);
            rs = pstmt.executeQuery();
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the Goal Table");
            while (rs.next()) {
                goal_Id = rs.getString("goal_id");
                descr = rs.getString("descr");
                timeBound = rs.getString("time_bound");
                String tmpltSql = "select * from task_tmplt where goal_id=? and ts_id is not null;";
                pstmt = (PreparedStatement) dbCon.prepareStatement(tmpltSql);
                pstmt.setString(1, goal_Id);
                ResultSet tmpltSet = pstmt.executeQuery();
                CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the T askTmpltTable");
                while (tmpltSet.next()) {
                    goal_Id = tmpltSet.getString("goal_id");
                    taskId = tmpltSet.getInt("task_id");
                    descr = tmpltSet.getString("descr");
                    tsId = tmpltSet.getInt("ts_id");
                    if (tmpltSet.getString("tooltip") != null && tmpltSet.getString("tooltip").trim() != "") {
                        toolTip = tmpltSet.getString("tooltip");
                    }
                    else {
                        toolTip = "NA";
                    }
                    Goal obj = new Goal();
                    obj.setGoal_Id(goal_Id);
                    obj.setTaskId(taskId);
                    obj.setDescr(descr);
                    toolTip = tmpltSet.getString("tooltip");
                    String goalSql = "select * from task_asst where ts_id=?;";
                    pstmt = (PreparedStatement) dbCon.prepareStatement(goalSql);
                    pstmt.setInt(1, tsId);
                    ResultSet goalSet = pstmt.executeQuery();
                    CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the TaskAsst Table for "
                            + goal_Id + " Goal");
                    while (goalSet.next()) {
                        tsId = goalSet.getInt("ts_id");
                        descr = goalSet.getString("descr");
                        tsql = goalSet.getString("tsql");
                        dynamic = goalSet.getString("dynamic");
                        ctId = goalSet.getString("ct_id");
                        relatedDb = goalSet.getString("relatd_db");
                        String colHeader[] = null;
                        if (goalSet.getString("col_hdr") != null && goalSet.getString("col_hdr").trim() != "") {
                            if ((goalSet.getString("col_hdr").split(",").length) >= 2) {
                                colHeader = goalSet.getString("col_hdr").split(",");
                            }
                            else {
                                colHeader[0] = "NA";
                                colHeader[1] = "NA";
                            }
                        }
                        String chartquery = "select ct_Id,descr from chart_type where ct_id=? and ct_Id is not null;";
                        goal.setChartType(goalSet.getString("ct_id"));
                        pstmt = (PreparedStatement) dbCon.prepareStatement(chartquery);
                        pstmt.setString(1, goal.setChartType(goalSet.getString("ct_id")));
                        ResultSet chrttype = pstmt.executeQuery();
                        CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Executed for the chart Table");
                        while (chrttype.next()) {
                            chartType = chrttype.getString("ct_id");
                            String descr1 = chrttype.getString("descr");
                        }
                        positionId = goalSet.getString("pos_id");
                        obj.setPositionId(positionId);
                        obj.setChartType(chartType);
                        if (dynamic.equals("y")) {
                            String entpquery = tsql;
                            dynamicChar = entpquery.replace("__filter", "?");
                            String finalString = dynamicChar.replaceAll("[']", "");
                            String resultString = finalString.replaceAll("[\"]", "'");
                            CustomerServiceConstant.logger.info("[GOALSERVICES] Dynamic Filter Value replaced");
                            if (!relatedDb.equalsIgnoreCase("e")) {
                                CustomerServiceConstant.logger
                                        .info("[GOALSERVICES]  Query with Dynamic value executing for Enterprise"
                                                + entpId);
                                pstmt = (PreparedStatement) dbCon1.prepareStatement(resultString);
                                pstmt.setString(1, entpId);
                                entpResultset = pstmt.executeQuery();
                            }
                            else {
                                CustomerServiceConstant.logger
                                        .info("[GOALSERVICES]   Query with Dynamic value executing for Exchange");
                                pstmt = (PreparedStatement) dbCon.prepareStatement(resultString);
                                pstmt.setString(1, entpId);
                                entpResultset = pstmt.executeQuery();
                            }
                        }
                        else {
                            if (!relatedDb.equalsIgnoreCase("e")) {
                                CustomerServiceConstant.logger
                                        .info("[GOALSERVICES]   Query For non Dynamic value executing for Enterprise"
                                                + entpId);
                                String entpquery = tsql;
                                stmt = (Statement) dbCon1.prepareStatement(entpquery);
                                // Resultset returned by query
                                entpResultset = stmt.executeQuery(entpquery);
                            }
                            else {
                                CustomerServiceConstant.logger
                                        .info("[GOALSERVICES]   Query for non Dynamic value executing for Exchange");
                                String entpquery = tsql;
                                stmt = (Statement) dbCon.prepareStatement(entpquery);
                                // Resultset returned by query
                                entpResultset = stmt.executeQuery(entpquery);
                            }
                        }
                        ResultSetMetaData rsmd = entpResultset.getMetaData();
                        int count = rsmd.getColumnCount();
                        DataTable chartdata = new DataTable();
                        if (chartType.equals("bar") || chartType.equals("pie")) {
                            ArrayList<ColumnDescription> colum = new ArrayList<ColumnDescription>();
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]  Columns are being set for BAR/PIE charts");
                            colum.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
                            colum.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                            chartdata.addColumns(colum);
                        }
                        else if (chartType.equals("line")) {
                            ArrayList<ColumnDescription> colum = new ArrayList<ColumnDescription>();
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]  Columns are being set for Annotated TimeLine charts");
                            colum.add(new ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0]));
                            colum.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                            chartdata.addColumns(colum);
                        }
                        List<Data> cDataList = new ArrayList<Data>();
                        while (entpResultset.next()) {
                            Data cData = new Data();
                            for (int i = 1; i <= count; i++) {
                                int type = rsmd.getColumnType(i);
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
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]  Rows are being set for BAR/PIE charts");
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
                        obj.setChartData(renderchart);
                        if (chartType.equals("plain")) {
                            CustomerServiceConstant.logger
                                    .info("[GOALSERVICES]  Dynamic Data Rows with are Plain Text");
                            if (cDataList.size() > 0) {
                                if (cDataList.get(0).getName() != null && cDataList.get(0).getName() != " ") {
                                    obj.setPlain(cDataList.get(0).getName());
                                }
                                else if (cDataList.get(0).getValue() != 0) {
                                    obj.setPlainData(cDataList.get(0).getValue());
                                }
                            }
                            else {
                                obj.setPlainData(0);
                            }
                        }
                        CustomerServiceConstant.logger
                                .info("[GOALSERVICES]  All the Constructed Objects are added to list");
                        sqlList.add(obj);
                    }
                }
            }
            String assetSql = "select a.asset_id,a.ip_addr,b.servr_cost,b.monthly_rent from asset a, dev_ctlg b where a.ctlg_id = b.ctlg_id;";
            stmt = (Statement) dbCon1.prepareStatement(assetSql);
            ResultSet assetSet = stmt.executeQuery(assetSql);
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Query Sucessfully Executed for the Asset");
            while (assetSet.next()) {
                AssetData asset = new AssetData();
                asset_Id = assetSet.getString("asset_id");
                asset.setAsset_Id(asset_Id);
                System.out.println("getdata" + asset.getAsset_Id());
                if (assetSet.getString("ip_addr") != null && assetSet.getString("ip_addr").trim() != "") {
                    ipAddr = assetSet.getString("ip_addr");
                }
                else {
                    ipAddr = "NA";
                }
                asset.setIpAddr(ipAddr);
                serverCost = assetSet.getDouble("servr_cost");
                asset.setServerCost(serverCost);
                if (assetSet.getInt("monthly_rent") != 0) {
                    monthlyRent = assetSet.getInt("monthly_rent");
                }
                else {
                    monthlyRent = 0;
                }
                asset.setMonthlyRent(monthlyRent);
                assetArray.add(asset);
            }
            JSONArray assetJsonArray = new JSONArray();
            JSONObject assetTitle = new JSONObject();
            for (int i = 0; i < assetArray.size(); i++) {
                JSONObject assetJson = new JSONObject();
                assetJson.put("AssetId", assetArray.get(i).getAsset_Id());
                assetJson.put("IpAddress", assetArray.get(i).getIpAddr());
                assetJson.put("ServerCost", assetArray.get(i).getServerCost());
                assetJson.put("MonthlyRent", assetArray.get(i).getMonthlyRent());
                assetJsonArray.put(assetJson);
            }
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Asset JSON is Constructed");
            String queryCheck = "select snpsht_id as SnapshotId, start_date as StartDate, end_date as EndDate, notes as Notes,cost_benefit as RealizedBenefit from goal_snpsht;";
            Statement checkStmt = (Statement) dbCon.createStatement();
            ResultSet chkSet = checkStmt.executeQuery(queryCheck);
            CustomerServiceConstant.logger.info("[GOALSERVICES] Query Sucessfully Executed for the Goal Snapshot");
            while (chkSet.next()) {
                GoalSnpsht goalSnpsht = new GoalSnpsht();
                goalSnpsht.setSnpshtId(chkSet.getInt("SnapshotId"));
                goalSnpsht.setStartDate(chkSet.getTimestamp("StartDate"));
                goalSnpsht.setEndDate(chkSet.getTimestamp("EndDate"));
                goalSnpsht.setNotes(chkSet.getString("Notes"));
                goalSnpsht.setCostBenefit(chkSet.getString("RealizedBenefit"));
                goalArray.add(goalSnpsht);
            }
            JSONArray goalJsonArray = new JSONArray();
            JSONObject goalTitle = new JSONObject();
            for (int i = 0; i < goalArray.size(); i++) {
                JSONObject goalJson = new JSONObject();
                goalJson.put("SnapshotId", goalArray.get(i).getSnpshtId());
                goalJson.put("StartDate", goalArray.get(i).getStartDate());
                goalJson.put("EndDate", goalArray.get(i).getEndDate());
                goalJson.put("Notes", goalArray.get(i).getNotes());
                goalJson.put("RealizedBenefit", goalArray.get(i).getCostBenefit());
                goalJsonArray.put(goalJson);
            }
            goalTitle.put("GoalSnapshotData", goalJsonArray);
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Goal JSON Constructed");
            assetTitle.put("AssetData", assetJsonArray);
            JSONArray chartArray = new JSONArray();
            JSONObject chartTitle = new JSONObject();
            for (int i = 0; i < sqlList.size(); i++) {
                JSONObject json = new JSONObject();
                json.put("charttype", sqlList.get(i).getChartType());
                json.put("divId", sqlList.get(i).getPositionId());
                json.put("taskId", sqlList.get(i).getTaskId());
                json.put("descr", sqlList.get(i).getDescr());
                if (sqlList.get(i).getChartType().equals("plain")) {
                    if (sqlList.get(i).getPlain() != null && sqlList.get(i).getPlain() != " ") {
                        json.put("data", sqlList.get(i).getPlain());
                    }
                    else if (sqlList.get(i).getPlainData() != 0) {
                        json.put("data", sqlList.get(i).getPlainData());
                    }
                }
                else if ((sqlList.get(i).getChartType().equals(null))) {
                    json.put("data", "nodata");
                }
                else if (sqlList.get(i).getChartType().equals("line")) {
                    json.put("data", sqlList.get(i).getChartData());
                    System.out.println("chart type in last for" + json.get("charttype") + "linedata inside json::::\t"
                            + sqlList.get(i).getChartData());
                }
                else {
                    json.put("Data", sqlList.get(i).getChartData());
                }
                chartArray.put(json);
            }
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Final JSon is Constructed");
            chartTitle.put("ChartData", chartArray);
            JSONArray finalResult = new JSONArray();
            finalResult.put(chartTitle);
            finalResult.put(assetTitle);
            finalResult.put(goalTitle);
            result = finalResult.toString();
        }
        catch (SQLException ex) {
            CustomerServiceConstant.logger.error("[GOALSERVICES]  Execption Occured while Executing Goal Services");
        }
        finally {
            // close connection ,stmt and resultset here
            dbCon.close();
            dbCon1.close();
            CustomerServiceConstant.logger.info("[GOALSERVICES]  Session Closed sucessfully");
        }
        CustomerServiceConstant.logger.info("[GOALSERVICES]  GOAL Services Executed Sucessfully");
        return Response.ok().entity(result).build();
    }
}
