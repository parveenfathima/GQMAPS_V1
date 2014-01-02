package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.gq.meter.object.ChartRowHolder;
import com.gq.meter.object.Goal;
import com.gq.meter.object.GoalMaster;
import com.gq.meter.object.GoalSnpsht;
import com.gq.meter.object.TemplateTaskDetails;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
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
    public Response getGoals(@QueryParam("goalId") String goalId, @QueryParam("entpId") String entpId,
            @QueryParam("goalInputs") String goalInputs) {

        Connection dbExchange = null;
        Connection dbCustomer = null;
        ResultSet rs = null;
        PreparedStatement prepareStmt;
        String masterGoalSql = "";
        String result = "";

        GoalMaster goalMaster = new GoalMaster();
        try {
            // get db connections
            dbExchange = SqlUtil.getExchangeConnection();
            dbCustomer = SqlUtil.getCustomerConnection(entpId);

            // 1.build the goal object
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

            // 2.build goal snapshot

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

            // 3.Task Template details for the particular task

            String taskTmpltQuery = "SELECT  gstc.snpsht_id , gstc.apply_date , gstc.cost_benefit , "
                    + " gstc.usr_notes , gstc.sys_notes , tt.task_id , tt.descr , tt.ts_id , tt.tooltip "
                    + " FROM task_tmplt tt left join  (select gs.snpsht_id , tc.apply_date , tc.cost_benefit ,"
                    + " tc.usr_notes , tc.sys_notes "
                    + " , tc.task_id FROM task_chklst tc  , goal_snpsht gs where gs.goal_id = '" + goalId
                    + "' and gs.enterprise_id = '" + entpId
                    + "' and gs.end_date is null and gs.snpsht_id = tc.snpsht_id "
                    + " ) gstc on tt.task_id = gstc.task_id where tt.goal_id = '" + goalId + "'";

            Statement statement = (Statement) dbExchange.createStatement();

            CustomerServiceConstant.logger.info("finaltskquery::" + taskTmpltQuery);
            ResultSet taskTmpltset = statement.executeQuery(taskTmpltQuery);

            List<TemplateTaskDetails> templateTaskDetails = new ArrayList<TemplateTaskDetails>(10);

            while (taskTmpltset.next()) {

                CustomerServiceConstant.logger.info("ss1 === taskTmpltset.getInt(ts_id) = " + taskTmpltset.getInt("ts_id") + 
                		" , goialid = "+ goalId + " , eid = " + entpId + " , ginputs = " + goalInputs);
                List<String> cddetails = getChartData(taskTmpltset.getInt("ts_id"), goalId, entpId, goalInputs,
                        dbExchange, dbCustomer);

                templateTaskDetails.add(new TemplateTaskDetails(taskTmpltset.getInt("snpsht_id"), taskTmpltset
                        .getTimestamp("apply_date"), taskTmpltset.getInt("cost_benefit"), taskTmpltset
                        .getString("usr_notes"), taskTmpltset.getString("sys_notes"), taskTmpltset.getInt("task_id"),
                        taskTmpltset.getString("descr"), taskTmpltset.getInt("ts_id"), taskTmpltset
                                .getString("tooltip"), cddetails.get(2), cddetails.get(0), cddetails.get(1)));
            }

            goalMaster.setTemplateTaskDetails(templateTaskDetails);

            CustomerServiceConstant.logger.info("Asset JSON is Constructed");

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

    private List<String> getChartData(int ts_id, String goalId, String entpId, String goalInputs,
            Connection dbExchange, Connection dbCustomer) throws Exception {

        List<String> retCData = new ArrayList<String>(3);

        // chart Data for goals
        Statement stmt = null;
        PreparedStatement prepareStmt;
        String taskSql = "";
        String dynamicInput = "";
        String ctId = "";
        String relatedDb = "";
        ResultSet entpResultset = null;
        CharSequence renderchart;
        String chartJson = "";
        ResultSet goalTaskAsstSet = null;

        // task asst sql execution begins to construct the chart data
        if (ts_id == 0) { // meaning we dont want to execute anything but just display static data
            // from template task table itself
            retCData.add("plain");
            retCData.add("notused");
            retCData.add(" ");

            return retCData;
        }
        else {
            String goalTaskAsstSql = "select * from task_asst where ts_id = ?;";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalTaskAsstSql);
            prepareStmt.setInt(1, ts_id);
            CustomerServiceConstant.logger.info(" Query Executed for the TaskAsst Table for " + goalId + " Goal");
            goalTaskAsstSet = prepareStmt.executeQuery();

            if (!goalTaskAsstSet.next()) {
                retCData.add(ctId);
                retCData.add("notused");
                retCData.add(" ");

                return retCData;
            }
        }

        // descr = goalTaskAsstSet.getString("descr");
        taskSql = goalTaskAsstSet.getString("tsql");
        dynamicInput = goalTaskAsstSet.getString("dynamic");
        ctId = goalTaskAsstSet.getString("ct_id"); // pie bar etc
        relatedDb = goalTaskAsstSet.getString("relatd_db");
        String colHeader[] = null;

        // first arg is chart type
        retCData.add(ctId);

        // second arg is position id - not used in this screen , so we set not used but used in dashboard
        retCData.add("notused");

        if (goalTaskAsstSet.getString("col_hdr") != null && goalTaskAsstSet.getString("col_hdr").trim() != "") {
            if ((goalTaskAsstSet.getString("col_hdr").split(",").length) >= 2) {
                colHeader = goalTaskAsstSet.getString("col_hdr").split(",");
            }
            else {
                colHeader[0] = "NA";
                colHeader[1] = "NA";
            }
        }

        // goal input
        CustomerServiceConstant.logger.info(" Dynamic Inputs passed for processing " + goalInputs);
        Map<String, String> goalInputMap = new HashMap<String, String>(5);
        if(goalInputs!=" " && goalInputs!=null){
        String[] inputs = goalInputs.split("~");
       
        for (String s : inputs) {
            String split[] = s.split("\\=");
            goalInputMap.put(split[0], split[1]);
        }
        }
        // replacing the sql with fillers we have in the hashmap
        if (dynamicInput.equals("y")) {
            Iterator<Entry<String, String>> it = goalInputMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                CustomerServiceConstant.logger.info(" Input values from map " + pairs.getKey() + " = "
                        + pairs.getValue());

                 CustomerServiceConstant.logger.info(pairs.getKey() + " = " + pairs.getValue());
                taskSql = taskSql.replaceAll(pairs.getKey(), pairs.getValue());
            }
        }

        // check whether dynamic input is required for the task

        if (relatedDb.equalsIgnoreCase("e")) {
            CustomerServiceConstant.logger.info(" Query for non Dynamic value executing for Exchange");
            stmt = (Statement) dbExchange.prepareStatement(taskSql);
            // Resultset returned by query entpResultset = stmt.executeQuery(tsql); } }
            entpResultset = stmt.executeQuery(taskSql);
        }
        else {
            CustomerServiceConstant.logger.info(" Query For non Dynamic value executing for Enterprise" + entpId);
            stmt = (Statement) dbCustomer.prepareStatement(taskSql); // Resultset returned by query
            CustomerServiceConstant.logger.info("tsql:: " + taskSql);
            entpResultset = stmt.executeQuery(taskSql);
        }
        // determine the number of columns will be used for charts
        if (!entpResultset.isBeforeFirst()) {
            retCData.add("");

            return retCData;
        }

        ResultSetMetaData rsMetaData = (ResultSetMetaData) entpResultset.getMetaData();
        int metaDataColumnCount = rsMetaData.getColumnCount();
        DataTable dataTable = new DataTable();

        if (ctId.equals("bar") || ctId.equals("pie")) {
            ArrayList<ColumnDescription> pieBarColumn = new ArrayList<ColumnDescription>();
            CustomerServiceConstant.logger.info(" Columns are being set for BAR/PIE charts");
            pieBarColumn.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
            pieBarColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
            dataTable.addColumns(pieBarColumn);
        }
        else if (ctId.equals("line")) {
            ArrayList<ColumnDescription> lineColumn = new ArrayList<ColumnDescription>();
            CustomerServiceConstant.logger.info(" Columns are being set for Annotated TimeLine charts");
            lineColumn.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
            lineColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
            dataTable.addColumns(lineColumn);
        }

        // dynamic rows for the chart

        List<ChartRowHolder> cDataList = new ArrayList<ChartRowHolder>();

        if (ctId.equals("bar") || ctId.equals("pie") || ctId.equals("line")) {

            while (entpResultset.next()) {
                ChartRowHolder cData = new ChartRowHolder();
                for (int i = 1; i <= metaDataColumnCount; i++) {
                    int type = rsMetaData.getColumnType(i);
                    if (type == Types.VARCHAR || type == Types.CHAR) {
                        cData.setXaxis(entpResultset.getString(i));
                    }
                    else if (type == Types.TIMESTAMP) {
                        cData.setXaxis(entpResultset.getString(i));
                    }
                    else {
                        cData.setYaxis(entpResultset.getDouble(i));
                    }
                }
                CustomerServiceConstant.logger.info(" Dynamic Column data's are added to the List ");
                cDataList.add(cData);
            }
        }
        else if (ctId.equals("plain")) {
            CustomerServiceConstant.logger.info(" Dynamic Data Rows with are Plain Text");
            // 3rd arg is the actual google chart json
            while (entpResultset.next()) {
            	retCData.add(entpResultset.getString(1)); // we expect only one row one column
            }
        }
        else if (ctId.equals("html")) {			// todo - make all these html / plain etc to constants - ss dec 31,2013
            CustomerServiceConstant.logger.info(" Dynamic Data Rows with html chosen - ss ");
            // 3rd arg is the actual google chart json
            ResultSetMetaData rsmd = entpResultset.getMetaData();

            if ( rsmd == null ) {
            	retCData.add("no result to be returned from html type.."); 
            	return retCData;
            }
            
            int columnsNumber = rsmd.getColumnCount();
            CustomerServiceConstant.logger.info(" Dynamic Data Rows with html chosen , columns found - "+ columnsNumber);

            StringBuilder sb = new StringBuilder(2000);
            sb.append("<table border=1>");
            
            // lets add the table header from the result set meta data
            sb.append("<tr bgcolor='#FF0000'>");
            for (int i=1 ; i < columnsNumber+1 ;i++){
            	sb.append("<td>");
            	sb.append(rsmd.getColumnLabel(i));
            	sb.append("</td>");
            }
            sb.append("</tr>");
            
            while (entpResultset.next()) {
            	CustomerServiceConstant.logger.info("===ss inside result set while loop ");
                sb.append("<tr>");
            	
            	for (int i=1 ; i < columnsNumber+1 ;i++){    // IMPORTANT : RESULT SET COLUMNS START WITH 1 AS FIRST SUBSCRIPT
            		CustomerServiceConstant.logger.info("columns - "+ i);
            		
                    sb.append("<td>");

            		if ( ( rsmd.getColumnType(i) == Types.VARCHAR ) ||  ( rsmd.getColumnType(i) == Types.CHAR ) ) {
            			sb.append(entpResultset.getString(i));
            		}
            		else if ( rsmd.getColumnType(i) == Types.INTEGER ) {
            			sb.append(entpResultset.getLong(i));
            		}
               		else if ( rsmd.getColumnType(i) == Types.DATE ) {
            			sb.append(entpResultset.getDate(i));
            		}
               		else if ( rsmd.getColumnType(i) == Types.TIMESTAMP ) {
            			sb.append(entpResultset.getTimestamp(i));
            		}
            		else if ( rsmd.getColumnType(i) == Types.DOUBLE ) {
            			sb.append(entpResultset.getDouble(i));
            		}
            		else if ( rsmd.getColumnType(i) == Types.LONGNVARCHAR ) {
            			sb.append(entpResultset.getString(i));
            		}
            		else {
            			sb.append("Unknown data type <"+  rsmd.getColumnType(i) +">");
            		}
                    sb.append("</td>");
            	}
                sb.append("</tr>");
            }
            sb.append("</table>");
            CustomerServiceConstant.logger.info("===ss html string is === " + sb.toString());
            retCData.add(sb.toString()); // we send the table
        } // html type ends
        

        // adding the rows to the chart
        if (ctId.equals("bar") || ctId.equals("pie")) {
            CustomerServiceConstant.logger.info(" Rows are being set for BAR/PIE charts");
            for (int i = 0; i < cDataList.size(); i++) {
                dataTable.addRowFromValues(cDataList.get(i).getXaxis(), cDataList.get(i).getYaxis());
            }
            renderchart = JsonRenderer.renderDataTable(dataTable, true, true);
            chartJson = CustomerServiceConstant.gson.toJson(renderchart);

            // 3rd arg is the actual google chart json
            retCData.add(chartJson);
        }
        else if (ctId.equals("line")) {
            CustomerServiceConstant.logger.info(" Rows are being set for AnnotatedTimeLine charts");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            for (int i = 0; i < cDataList.size(); i++) {
                String other = cDataList.get(i).getXaxis();
                int year = Integer.parseInt(other.substring(0, 4));
                int month = Integer.parseInt(other.substring(5, 7)) - 1;
                int date = Integer.parseInt(other.substring(8, 10));
                int hours = Integer.parseInt(other.substring(11, 13));
                int minutes = Integer.parseInt(other.substring(14, 16));
                int seconds = Integer.parseInt(other.substring(17, 19));
                calendar.set(year, month, date, hours, minutes, seconds);
            	DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//used for converting date to String format
            	DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	Date currDate = inputFormat.parse(other);
            	String outputText = outputFormat.format(currDate);
                String newDate="new Date("+outputText+")";
                dataTable.addRowFromValues(outputText, cDataList.get(i).getYaxis());
            }
            renderchart = JsonRenderer.renderDataTable(dataTable, true, true);
            chartJson = CustomerServiceConstant.gson.toJson(renderchart);
            // 3rd arg is the actual google chart json
            retCData.add(chartJson);
        }

        CustomerServiceConstant.logger.info(" ArrowFormat data's are being constructed");

        return retCData;

        // contains the data required for charts

    } // end of method
} // end of class

