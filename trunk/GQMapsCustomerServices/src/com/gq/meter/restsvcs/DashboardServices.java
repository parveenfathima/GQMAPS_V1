package com.gq.meter.restsvcs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.gq.meter.model.IpAddresValidator;
import com.gq.meter.object.ChartRowHolder;
import com.gq.meter.object.TaskAssist;
import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Path("/DashboardServices")
public class DashboardServices {

    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Path("/getdashboard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDashboardServices(@QueryParam("entpId") String entpId) {
        Connection dbExchange = null;
        Connection dbCustomer = null;
        Statement stmt = null;
        ResultSet rs = null;
        int taskId = 0;
        String descr = "";
        String sql = "";
        String chartType = "";
        String[] columnHeader = null;
        String positionId = "";
        String dynamicInput = null;
        String relatedDb = null;
        String chartData = "";
        double plainData = 0;
        String lineData = "";
        String plain = "";
        TaskAssist taskAssist = new TaskAssist(taskId, descr, sql, dynamicInput, chartType, columnHeader, relatedDb,
                positionId, chartData, plainData, lineData, plain);
        PreparedStatement prepareStmt = null;
        String result = "";
        ResultSet entpResultset;
        List<TaskAssist> sqllist = new ArrayList<TaskAssist>();
        CharSequence renderchart = null;
        String dynamicChar = "";
        try {
            // getting database connection to MySQL server
            dbExchange = SqlUtil.getExchangeConnection();
            dbCustomer = SqlUtil.getCustomerConnection(entpId);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  DataBase Connectivity established Sucessfully for GQEXCHANGE and Enterprise DataBase gqm"
                            + entpId);
            // dbCon2 = DriverManager.getConnection(dbUrl);
            // String currentSql = "select * from task_asst WHERE pos_id IS NOT NULL;";
            String taskAssistSql = " select * from task_asst WHERE pos_id Like 'div_%' and tsql not Like 'call%';";
            // getting PreparedStatment to execute query
            stmt = (Statement) dbExchange.prepareStatement(taskAssistSql);
            // Resultset returned by query
            rs = stmt.executeQuery(taskAssistSql);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  Query sucessfully Executed from the TaskAsst Table");
            while (rs.next()) {
                taskId = rs.getInt("ts_id");
                descr = rs.getString("descr");
                sql = rs.getString("tsql");
                dynamicInput = rs.getString("dynamic");
                // String ctId = rs.getString("ct_id");
                String chartTypeQuery = "select ct_Id,descr from chart_type where ct_id=?";
                taskAssist.setChartType(rs.getString("ct_id"));
                // System.out.println("ct-id" + taskAssist.setChartType(rs.getString("ct_id")));
                // stmt = (Statement) dbExchange.prepareStatement(chartquery);
                prepareStmt = (PreparedStatement) dbExchange.prepareStatement(chartTypeQuery);
                prepareStmt.setString(1, taskAssist.setChartType(rs.getString("ct_id")));
                ResultSet chartTypeSet = prepareStmt.executeQuery();
                CustomerServiceConstant.logger
                        .info("[DASHBOARDSERVICES]  Query sucessfully Executed from the ChartType Table to get the Chart Type and Descr");
                while (chartTypeSet.next()) {
                    chartType = chartTypeSet.getString("ct_id");
                    String descrp = chartTypeSet.getString("descr");
                    CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  ChartType \t" + chartType + "Descr\t"
                            + descrp);
                }
                String[] colHeader = null;
                if (rs.getString("col_hdr") != null && rs.getString("col_hdr").trim() != "") {
                    if ((rs.getString("col_hdr").split(",").length) >= 2) {
                        colHeader = rs.getString("col_hdr").split(",");
                    }
                    else {
                        colHeader[0] = "NA";
                        colHeader[1] = "NA";
                    }
                }
                relatedDb = rs.getString("relatd_db");
                positionId = rs.getString("pos_id");
                TaskAssist taskAssistObj = new TaskAssist();
                taskAssistObj.setTaskId(taskId);
                taskAssistObj.setDescr(descr);
                // obj.setSql(sql);
                // obj.setDynamic(dynamic);
                taskAssistObj.setChartType(chartType);
                if (colHeader != null) {
                    taskAssistObj.setColumnHeader(colHeader);
                }
                else {
                    taskAssistObj.setColumnHeader(colHeader);
                }
                taskAssistObj.setRelatedDb(relatedDb);
                taskAssistObj.setPositionId(positionId);
                if (dynamicInput.equals("y")) {
                    String entpquery = sql;
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Query Which has a dependency of Dynamic Value");
                    dynamicChar = entpquery.replace("__filter", "?");
                    String fillerString = dynamicChar.replaceAll("[']", "");
                    String resultString = fillerString.replaceAll("[\"]", "'");
                    if (!relatedDb.equalsIgnoreCase("e")) {
                        // System.out.println("maps::::::::::");

                        // System.out.println("query is \t" + entpquery);
                        // stmt = (Statement) dbCon1.prepareStatement(entpquery);
                        // // Resultset returned by query
                        // entpResultset = stmt.executeQuery(entpquery);
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query is Executing for the Enterprise");
                        prepareStmt = (PreparedStatement) dbCustomer.prepareStatement(resultString);

                        // based on the query requirement enterprise id positioning is differed.for eg: 1 query may need
                        // only 1 enterprise id other may need more than 1
                        int number = org.apache.commons.lang.StringUtils.countMatches(resultString, "?");
                        for (int i = 1; i <= number; i++) {
                            prepareStmt.setString(i, entpId);
                        }

                        entpResultset = prepareStmt.executeQuery();
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for the enterprise" + entpId);
                    }
                    else {
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query is ready to execute for the exchange with dynamic value");
                        prepareStmt = (PreparedStatement) dbExchange.prepareStatement(resultString);
                        int number = org.apache.commons.lang.StringUtils.countMatches(resultString, "?");

                        for (int i = 1; i <= number; i++) {
                            prepareStmt.setString(i, entpId);
                        }
                        entpResultset = prepareStmt.executeQuery();
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for the enterprise with Dynamic value");
                    }

                }
                else {
                    if (!relatedDb.equalsIgnoreCase("e")) {
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query is Executing with no Dynamic Value for the enterprise"
                                        + entpId);
                        stmt = (Statement) dbCustomer.prepareStatement(sql);
                        // Resultset returned by query
                        entpResultset = stmt.executeQuery(sql);
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for the enterprise" + entpId
                                        + "with no Dynamic value");
                    }
                    else {
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query will be Executing with no Dynamic Value for Exchange");
                        stmt = (Statement) dbExchange.prepareStatement(sql);
                        // Resultset returned by query
                        entpResultset = stmt.executeQuery(sql);
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for gqexchange with no Dynamic value");
                    }
                }
                // find col count c =2;
                ResultSetMetaData rsMetaData = entpResultset.getMetaData();
                int metaDataColumnCount = rsMetaData.getColumnCount();

                DataTable chartdata = new DataTable();

                if (chartType.equals("bar") || chartType.equals("pie")) {
                    ArrayList<ColumnDescription> pieBarColumn = new ArrayList<ColumnDescription>();
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Column Header is set for the BAR/PIE charts");
                    if (colHeader.length == 2) {
                        pieBarColumn.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
                        pieBarColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                    }
                    else if (colHeader.length == 3) {
                        pieBarColumn.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
                        pieBarColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                        pieBarColumn.add(new ColumnDescription(colHeader[2], ValueType.NUMBER, colHeader[2]));
                    }
                    chartdata.addColumns(pieBarColumn);
                }
                else if (chartType.equals("line")) {
                    ArrayList<ColumnDescription> lineColumn = new ArrayList<ColumnDescription>();
                    CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  column Header is set for the Line chart");
                    // System.out.println("inside line");
                    // DataTable chartdata = new DataTable();
                    if (colHeader.length <= 2) {
                        lineColumn.add(new ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0]));
                        lineColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                    }
                    else if (colHeader.length == 3) {
                        lineColumn.add(new ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0]));
                        lineColumn.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                        lineColumn.add(new ColumnDescription(colHeader[2], ValueType.NUMBER, colHeader[2]));
                    }
                    chartdata.addColumns(lineColumn);
                }
                List<ChartRowHolder> cDataList = new ArrayList<ChartRowHolder>();

                while (entpResultset.next()) {
                    CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Query is Executed for dynamic columns");
                    ChartRowHolder cData = new ChartRowHolder();
                    for (int i = 1; i <= metaDataColumnCount; i++) {
                        int type = rsMetaData.getColumnType(i);
                        if (type == Types.VARCHAR || type == Types.CHAR) {
                            // str = datavalue.setName(entpResultset.getString(i));
                        	if(positionId.equalsIgnoreCase("div_connectedWebsites")){
                        	    InetAddress addr = InetAddress.getByName(entpResultset.getString(i));// resolving ipaddress to
                                String host = addr.getCanonicalHostName();
                                host=IpAddresValidator.validateIpAddress(host);//validating the ipaddress
                                cData.setXaxis(host);
                        	}else{
                            cData.setXaxis(entpResultset.getString(i));
                            // System.out.println("inside if" + cData.setName(entpResultset.getString(i)));
                        	}
                        }
                        // else if (type == Types.DATE) {
                        else if (type == Types.TIMESTAMP) {
                            // dt = datavalue.setDate(entpResultset.getTimestamp(i));
                            // cData.setDate(entpResultset.getTimestamp(i));
                            cData.setXaxis(entpResultset.getString(i));
                            
                        }
                        else if (type == Types.DECIMAL) {
                            if (i == 2)
                                cData.setYaxis(entpResultset.getDouble(i));
                            else
                                cData.setZaxis(entpResultset.getDouble(i));

                        }
                        else {
                            // val = datavalue.setValue(entpResultset.getDouble(i));
                            cData.setYaxis(entpResultset.getDouble(i));
                            // System.out.println("inside else" + cData.setValue(entpResultset.getDouble(i)));
                        }
                    }
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Dynamic data's are being added to the List");
                    cDataList.add(cData);
                }

                // chart typr data convertion to be added<to-do>
                if (chartType.equals("bar") || chartType.equals("pie")) {
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Rows and Columns are being added for BAR/PIE charts");
                    for (int i = 0; i < cDataList.size(); i++) {
                        chartdata.addRowFromValues(cDataList.get(i).getXaxis(), cDataList.get(i).getYaxis(), cDataList
                                .get(i).getZaxis());
                    }
                    // System.out.println("\ndata1\t" + cDataList.get(i).getXaxis + "\tcdata2\t"
                    // + cDataList.get(i).getYaxis());
                }
                // json.put("chart data", JsonRenderer.renderDataTable(chartdata, true, true));

                else if (chartType.equals("line")) {
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Rows and Columns are being added to the Annotated TimeLine Charts");
                    for (int i = 0; i < cDataList.size(); i++) {
                        String other = cDataList.get(i).getXaxis();
                        int year = Integer.parseInt(other.substring(0, 4));
                        int month = Integer.parseInt(other.substring(5, 7)) - 1;
                        int date = Integer.parseInt(other.substring(8, 10));
                        int hours = Integer.parseInt(other.substring(11, 13));
                        int minutes = Integer.parseInt(other.substring(14, 16));
                        int seconds = Integer.parseInt(other.substring(17, 19));

                        calendar.set(year, month, date, hours, minutes, seconds);
                        chartdata.addRowFromValues(calendar, cDataList.get(i).getYaxis());
                    }
                }
                CustomerServiceConstant.logger
                        .info("[DASHBOARDSERVICES]  ROWS and COLUMNS are Added to the objects in the Arrow Format");
                renderchart = JsonRenderer.renderDataTable(chartdata, true, true);
                taskAssistObj.setChartData(renderchart);
                if (chartType.equals("plain")) {
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES] Dynamic column values which Are in the Form of Plain Text");
                    if (cDataList.size() > 0) {
                        if (cDataList.get(0).getXaxis() != null && cDataList.get(0).getXaxis() != " ") {
                            taskAssistObj.setPlain(cDataList.get(0).getXaxis());
                        }
                        else if (cDataList.get(0).getYaxis() != 0) {
                            taskAssistObj.setPlainData(cDataList.get(0).getYaxis());
                        }
                    }
                    else {
                        taskAssistObj.setPlainData(0);
                    }
                }
                CustomerServiceConstant.logger
                        .info("[DASHBOARDSERVICES]  Objects are constructed and Adde to the list");
                sqllist.add(taskAssistObj);
            }

            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Goal JSON is Ready t Return to the User");
            JSONObject chartTitle = new JSONObject();
            JSONArray chartDataArray = new JSONArray();
            for (int i = 0; i < sqllist.size(); i++) {
                // jsonArrayList = sqllist.get(i);
                JSONObject json = new JSONObject();
                json.put("charttype", sqllist.get(i).getChartType());
                json.put("divId", sqllist.get(i).getPositionId());
                if (sqllist.get(i).getChartType().equals("plain")) {
                    if (sqllist.get(i).getPlain() != null && sqllist.get(i).getPlain() != " ") {
                        json.put("data", sqllist.get(i).getPlain());
                    }
                    else if (sqllist.get(i).getPlainData() != 0) {
                        json.put("data", sqllist.get(i).getPlainData());
                    }
                }
                else if ((sqllist.get(i).getChartData().equals(null)) || sqllist.get(i).getChartData().equals("")) {
                    json.put("data", "nodata");
                }
                else {
                    json.put("data", sqllist.get(i).getChartData());
                }
                chartDataArray.put(json);
            }
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  Complete JSON is Constructed for the Dashboard Data");
            JSONArray finalResult = new JSONArray();
            finalResult.put(chartTitle);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  DashBoard sucessfully Executed By executing all the Dependencies");
            result = chartDataArray.toString();
        }

        catch (SQLException ex) {
            CustomerServiceConstant.logger
                    .error("[DASHBOARDSERVICES]  Execption Occured while Executing the DashBoard services" + ex);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Exception Occured in the Connection" + e);
        }

        finally {
            // close connection ,stmt and resultset here
            try {
                dbExchange.close();
                dbCustomer.close();
            }
            catch (SQLException e) {
                CustomerServiceConstant.logger
                        .info("[DASHBOARDSERVICES]  Exception Occured while closing the Connection" + e);
            }

            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Closing the DataBase Connection");
            // System.out.println("connection state:\t" + dbExchange.isClosed());
        }
        return Response.ok().entity(result).build();
    }

}