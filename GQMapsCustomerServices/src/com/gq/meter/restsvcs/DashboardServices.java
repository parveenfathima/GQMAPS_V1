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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.gq.meter.object.Data;
import com.gq.meter.object.TaskAssist;
import com.gq.meter.util.CustomerServiceConstant;
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
    public Response getDashboardServices(@QueryParam("entpId") String entpId) throws ClassNotFoundException,
            SQLException, TypeMismatchException {

        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqexchange";
        String username = "gqmaps";
        String password = "Ch1ca803ear$";
        String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqm" + entpId;
        // String dbUrl = "jdbc:mysql://192.168.1.95:3306/gqm" + entpId
        // + "?user=gqmaps&password=Ch1ca803ear$&noAccessToProcedureBodies=true";

        // System.out.println("procedure url is\t" + dbUrl);
        // String dbURL = "jdbc:mysql://localhost:3306/gqexchange";
        // String username = "root";
        // String password = "itech";
        // String dbURL1 = "jdbc:mysql://localhost:3306/gqmaps";
        Connection dbCon = null;
        Connection dbCon1 = null;
        Statement stmt = null;
        ResultSet rs = null;
        int taskId = 0;
        String descr = "";
        String sql = "";
        String chartType = "";
        String[] columnHeader = null;
        String positionId = "";
        String dynamic = null;
        String relatedDb = null;
        List<Data> data = null;
        String chartData = "";
        double plainData = 0;
        String lineData = "";
        String plain = "";
        TaskAssist taskAssist = new TaskAssist(taskId, descr, sql, dynamic, chartType, columnHeader, relatedDb,
                positionId, data, chartData, plainData, lineData, plain);
        PreparedStatement pstmt = null;
        String result = "";
        ResultSet entpResultset;
        List<TaskAssist> sqllist = new ArrayList<TaskAssist>();
        CharSequence renderchart = null;
        String dynamicChar = "";
        try {
            // getting database connection to MySQL server
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            dbCon1 = DriverManager.getConnection(dbURL1, username, password);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  DataBase Connectivity established Sucessfully for GQEXCHANGE and Enterprise DataBase gqm"
                            + entpId);
            // dbCon2 = DriverManager.getConnection(dbUrl);
            // String currentSql = "select * from task_asst WHERE pos_id IS NOT NULL;";
            String currentSql = " select * from task_asst WHERE pos_id Like 'div_%' and tsql not Like 'call%';";
            // getting PreparedStatment to execute query
            stmt = (Statement) dbCon.prepareStatement(currentSql);
            // Resultset returned by query
            rs = stmt.executeQuery(currentSql);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  Query sucessfully Executed from the TaskAsst Table");
            while (rs.next()) {
                taskId = rs.getInt("ts_id");
                descr = rs.getString("descr");
                sql = rs.getString("tsql");
                dynamic = rs.getString("dynamic");
                // String ctId = rs.getString("ct_id");
                String chartquery = "select ct_Id,descr from chart_type where ct_id=?";
                taskAssist.setChartType(rs.getString("ct_id"));
                // System.out.println("ct-id" + taskAssist.setChartType(rs.getString("ct_id")));
                // stmt = (Statement) dbCon.prepareStatement(chartquery);
                pstmt = (PreparedStatement) dbCon.prepareStatement(chartquery);
                pstmt.setString(1, taskAssist.setChartType(rs.getString("ct_id")));
                ResultSet chrttype = pstmt.executeQuery();
                CustomerServiceConstant.logger
                        .info("[DASHBOARDSERVICES]  Query sucessfully Executed from the ChartType Table to get the Chart Type and Descr");
                while (chrttype.next()) {
                    chartType = chrttype.getString("ct_id");
                    String descr1 = chrttype.getString("descr");
                    CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  ChartType \t" + chartType + "Descr\t"
                            + descr1);
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
                TaskAssist obj = new TaskAssist();
                obj.setTaskId(taskId);
                obj.setDescr(descr);
                // obj.setSql(sql);
                // obj.setDynamic(dynamic);
                obj.setChartType(chartType);
                if (colHeader != null) {
                    obj.setColumnHeader(colHeader);
                }
                else {
                    obj.setColumnHeader(colHeader);
                }
                obj.setRelatedDb(relatedDb);
                obj.setPositionId(positionId);
                if (dynamic.equals("y")) {
                    String entpquery = sql;
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Query Which has a dependency of Dynamic Value");
                    dynamicChar = entpquery.replace("__filter", "?");
                    String finalString = dynamicChar.replaceAll("[']", "");
                    String resultString = finalString.replaceAll("[\"]", "'");
                    if (!relatedDb.equalsIgnoreCase("e")) {
                        // System.out.println("maps::::::::::");

                        // System.out.println("query is \t" + entpquery);
                        // stmt = (Statement) dbCon1.prepareStatement(entpquery);
                        // // Resultset returned by query
                        // entpResultset = stmt.executeQuery(entpquery);
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query is Executing for the Enterprise");
                        pstmt = (PreparedStatement) dbCon1.prepareStatement(resultString);
                        pstmt.setString(1, entpId);
                        entpResultset = pstmt.executeQuery();
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for the enterprise" + entpId);
                    }
                    else {
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query is ready to execute for the exchange with dynamic value");
                        pstmt = (PreparedStatement) dbCon.prepareStatement(resultString);
                        pstmt.setString(1, entpId);
                        entpResultset = pstmt.executeQuery();
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for the enterprise with Dynamic value");
                    }

                }
                else {
                    if (!relatedDb.equalsIgnoreCase("e")) {
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query is Executing with no Dynamic Value for the enterprise"
                                        + entpId);
                        String entpquery = sql;
                        stmt = (Statement) dbCon1.prepareStatement(entpquery);
                        // Resultset returned by query
                        entpResultset = stmt.executeQuery(entpquery);
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for the enterprise" + entpId
                                        + "with no Dynamic value");
                    }
                    else {
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query will be Executing with no Dynamic Value for Exchange");
                        String entpquery = sql;
                        stmt = (Statement) dbCon.prepareStatement(entpquery);
                        // Resultset returned by query
                        entpResultset = stmt.executeQuery(entpquery);
                        CustomerServiceConstant.logger
                                .info("[DASHBOARDSERVICES]  Query sucessfully Executed for gqexchange with no Dynamic value");
                        // if (dynamic.equals("y")) {
                        // System.out.println("dynamic queryis :\t" + entpquery + "entpid is::::\t" + entpId);
                        // dynamicChar = entpquery.replace("__filter", "?");
                        // String finalString = dynamicChar.replaceAll("[-+.^:']", "");
                        // System.out.println("after replace:::::\t" + finalString);
                        // pstmt = (PreparedStatement) dbCon1.prepareStatement(finalString);
                        // System.out.println("before setstring:::");
                        // pstmt.setString(1, entpId);
                        // System.out.println("after setstring" + pstmt);
                        // entpResultset = pstmt.executeQuery();
                        // }
                        // else {
                        // System.out.println("after constructed" + pstmt);
                        // stmt = (Statement) dbCon.prepareStatement(entpquery);
                        // System.out.println("pstmt" + pstmt);
                        // // Resultset returned by query
                        // entpResultset = stmt.executeQuery(entpquery);
                        // System.out.println("query executed");
                        // }
                    }
                }
                // find col count c =2;
                ResultSetMetaData rsmd = entpResultset.getMetaData();
                int count = rsmd.getColumnCount();

                DataTable chartdata = new DataTable();

                if (chartType.equals("bar") || chartType.equals("pie")) {
                    ArrayList<ColumnDescription> colum = new ArrayList<ColumnDescription>();
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Column Header is set for the BAR/PIE charts");
                    colum.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
                    colum.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                    chartdata.addColumns(colum);
                }
                else if (chartType.equals("line")) {
                    ArrayList<ColumnDescription> colum = new ArrayList<ColumnDescription>();
                    CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  column Header is set for the Line chart");
                    // System.out.println("inside line");
                    // DataTable chartdata = new DataTable();

                    colum.add(new ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0]));
                    colum.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                    chartdata.addColumns(colum);
                }
                List<Data> cDataList = new ArrayList<Data>();

                while (entpResultset.next()) {
                    CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Query is Executed for dynamic columns");
                    Data cData = new Data();
                    for (int i = 1; i <= count; i++) {
                        int type = rsmd.getColumnType(i);
                        if (type == Types.VARCHAR || type == Types.CHAR) {
                            // str = datavalue.setName(entpResultset.getString(i));
                            cData.setName(entpResultset.getString(i));
                            // System.out.println("inside if" + cData.setName(entpResultset.getString(i)));
                        }
                        // else if (type == Types.DATE) {
                        else if (type == Types.TIMESTAMP) {
                            // dt = datavalue.setDate(entpResultset.getTimestamp(i));
                            // cData.setDate(entpResultset.getTimestamp(i));
                            cData.setName(entpResultset.getString(i));
                        }
                        else {
                            // val = datavalue.setValue(entpResultset.getDouble(i));
                            cData.setValue(entpResultset.getDouble(i));
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
                        chartdata.addRowFromValues(cDataList.get(i).getName(), cDataList.get(i).getValue());
                    }
                    // System.out.println("\ndata1\t" + cDataList.get(i).getName() + "\tcdata2\t"
                    // + cDataList.get(i).getValue());
                }
                // json.put("chart data", JsonRenderer.renderDataTable(chartdata, true, true));

                else if (chartType.equals("line")) {
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES]  Rows and Columns are being added to the Annotated TimeLine Charts");
                    for (int i = 0; i < cDataList.size(); i++) {
                        String other = cDataList.get(i).getName();
                        int year = Integer.parseInt(other.substring(0, 4));
                        int month = Integer.parseInt(other.substring(5, 7));
                        int date = Integer.parseInt(other.substring(8, 10));
                        int hours = Integer.parseInt(other.substring(11, 13));
                        int minutes = Integer.parseInt(other.substring(14, 16));
                        int seconds = Integer.parseInt(other.substring(17, 19));
                        // System.out.println("year\t" + year + "month\t" + month + "date\t" + date + "hours\t" +
                        // hours
                        // + "minutes\t" + minutes + "seconds\t" + seconds);

                        calendar.set(year, month, date, hours, minutes, seconds);
                        chartdata.addRowFromValues(calendar, cDataList.get(i).getValue());

                        // JSONObject json = new JSONObject();
                        // json.put("date", cDataList.get(i).getName());
                        // json.put("value", cDataList.get(i).getValue());
                        // chartArray.put(json);

                    }
                    // datavalue.setChartData(JsonRenderer.renderDataTable(chartdata, true, true));
                }

                // if (chartType.equals("bar") || chartType.equals("pie")) {
                // renderchart = JsonRenderer.renderDataTable(chartdata, true, true);
                // obj.setChartData(renderchart);
                // }
                // else if (chartType.equals("line")) {
                // System.out.println("line values: before adding to obj:::\t" + chartArray.toString());
                // renderchart = JsonRenderer.renderDataTable(chartdata, true, true);
                // obj.setChartData(renderchart);
                // // obj.setLineData(chartArray.toString());
                // // System.out.println("getlinedata: \t" + obj.getLineData());
                // }
                CustomerServiceConstant.logger
                        .info("[DASHBOARDSERVICES]  ROWS and COLUMNS are Added to the objects in the Arrow Format");
                renderchart = JsonRenderer.renderDataTable(chartdata, true, true);
                obj.setChartData(renderchart);
                if (chartType.equals("plain")) {
                    CustomerServiceConstant.logger
                            .info("[DASHBOARDSERVICES] Dynamic column values which Are in the Form of Plain Text");
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
                        .info("[DASHBOARDSERVICES]  Objects are constructed and Adde to the list");
                sqllist.add(obj);
            }

            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Goal JSON is Ready t Return to the User");
            JSONObject chartTitle = new JSONObject();
            JSONArray chartArray = new JSONArray();
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
                // else if (sqllist.get(i).getChartType().equals("line")) {
                // json.put("data", sqllist.get(i).getLineData());
                // System.out.println("chart type in last for" + json.get("charttype") + "linedata inside json::::\t"
                // + sqllist.get(i).getLineData());
                // }
                else if ((sqllist.get(i).getChartData().equals(null)) || sqllist.get(i).getChartData().equals("")) {
                    json.put("data", "nodata");
                }
                else {
                    json.put("data", sqllist.get(i).getChartData());
                }
                chartArray.put(json);
            }
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  Complete JSON is Constructed for the Dashboard Data");
            // chartTitle.put("chartData", chartArray);
            JSONArray finalResult = new JSONArray();
            finalResult.put(chartTitle);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  DashBoard sucessfully Executed By executing all the Dependencies");
            result = chartArray.toString();
        }

        catch (SQLException ex) {
            CustomerServiceConstant.logger
                    .error("[DASHBOARDSERVICES]  Execption Occured while Executing the DashBoard services" + ex);
        }

        finally {
            // close connection ,stmt and resultset here
            dbCon.close();
            dbCon1.close();
            CustomerServiceConstant.logger.info("[DASHBOARDSERVICES]  Closing the DataBase Connection");
            // System.out.println("connection state:\t" + dbCon.isClosed());
        }
        return Response.ok().entity(result).build();
    }
}