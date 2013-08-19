package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
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
        String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqmaps";
        // String dbURL = "jdbc:mysql://localhost:3306/gqexchange";
        // String username = "root";
        // String password = "root";
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
        String str = null;
        double val = 0;
        Date dt = null;
        String chartData = "";
        double plainData = 0;
        String lineData = "";
        String plain = "";
        TaskAssist taskAssist = new TaskAssist(taskId, descr, sql, dynamic, chartType, columnHeader, relatedDb,
                positionId, data, chartData, plainData, lineData, plain);
        PreparedStatement pstmt = null;
        String alist = "";
        String result = "";
        ResultSet entpResultset;
        List<TaskAssist> sqllist = new ArrayList<TaskAssist>();
        DataTable linedata = new DataTable();
        DataTable bardata = new DataTable();

        CharSequence renderchart = null;
        TaskAssist jsonArrayList;
        String dynamicChar = "";
        try {
            // getting database connection to MySQL server
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            dbCon1 = DriverManager.getConnection(dbURL1, username, password);
            CustomerServiceConstant.logger.info("Executing the ");
            String currentSql = "select * from task_asst WHERE pos_id IS NOT NULL;";
            // getting PreparedStatment to execute query
            stmt = (Statement) dbCon.prepareStatement(currentSql);
            // Resultset returned by query
            rs = stmt.executeQuery(currentSql);
            while (rs.next()) {
                JSONArray chartArray = new JSONArray();

                // System.out.println("inside  while");
                taskId = rs.getInt("ts_id");
                descr = rs.getString("descr");
                sql = rs.getString("tsql");
                dynamic = rs.getString("dynamic");
                // String ctId = rs.getString("ct_id");
                String chartquery = "select ct_Id,descr from chart_type where ct_id=?";
                // System.out.println("ct-id");
                taskAssist.setChartType(rs.getString("ct_id"));
                // System.out.println("ct-id" + taskAssist.setChartType(rs.getString("ct_id")));
                // stmt = (Statement) dbCon.prepareStatement(chartquery);
                pstmt = (PreparedStatement) dbCon.prepareStatement(chartquery);
                pstmt.setString(1, taskAssist.setChartType(rs.getString("ct_id")));
                System.out.println("chart pstmt" + pstmt);
                ResultSet chrttype = pstmt.executeQuery();
                while (chrttype.next()) {
                    chartType = chrttype.getString("ct_id");
                    String descr1 = chrttype.getString("descr");
                    System.out.println("charttype\t" + chartType + "descr\t" + descr1);
                }
                String[] colHeader = null;
                // System.out.println("col header: " + rs.getString("col_hdr"));
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
                // System.out.println("tsid\t" + taskId + "descr" + descr + "tsql" + sql + "colheader" + columnHeader
                // + "posid" + positionId);
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
                // sqllist.add(obj);
                System.out.println("Dynamic value:" + dynamic);
                // if (dynamic.equals("y")) {
                // String entpquery = sql;
                //
                // System.out.println("dynamic queryis :\t" + entpquery + "entpid is::::\t" + entpId);
                // dynamicChar = entpquery.replace("__filter", "?");
                // String finalString = dynamicChar.replaceAll("[']", "");
                // System.out.println("after replace:::::\t" + finalString);
                //
                // if (relatedDb.equalsIgnoreCase("m")) {
                // // System.out.println("maps::::::::::");
                //
                // // System.out.println("query is \t" + entpquery);
                // // stmt = (Statement) dbCon1.prepareStatement(entpquery);
                // // // Resultset returned by query
                // // entpResultset = stmt.executeQuery(entpquery);
                //
                // pstmt = (PreparedStatement) dbCon1.prepareStatement(finalString);
                // System.out.println("before setstring:::");
                // pstmt.setString(1, entpId);
                // System.out.println("after setstring" + pstmt);
                // entpResultset = pstmt.executeQuery();
                // }
                // else {
                // // System.out.println("inside else");
                // System.out.println("dynamic queryis exchange :\t" + entpquery + "entpid is::::\t" + entpId);
                // pstmt = (PreparedStatement) dbCon.prepareStatement(finalString);
                // System.out.println("before setstring:::");
                // pstmt.setString(1, entpId);
                // System.out.println("after setstring" + pstmt);
                // entpResultset = pstmt.executeQuery();
                // }
                //
                // }
                // else {
                if (relatedDb.equalsIgnoreCase("m")) {
                    System.out.println("inside non dynamic if::::::::::");

                    String entpquery = sql;
                    // System.out.println("query is \t" + entpquery);
                    stmt = (Statement) dbCon1.prepareStatement(entpquery);
                    // Resultset returned by query
                    entpResultset = stmt.executeQuery(entpquery);
                }
                else {
                    System.out.println("inside non dynamic else::::::::::");
                    String entpquery = sql;
                    stmt = (Statement) dbCon.prepareStatement(entpquery);
                    // Resultset returned by query
                    entpResultset = stmt.executeQuery(entpquery);
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
                // }
                // find col count c =2;
                ResultSetMetaData rsmd = entpResultset.getMetaData();
                int count = rsmd.getColumnCount();

                // System.out.println("cout value is ::" + count);
                DataTable chartdata = new DataTable();

                if (chartType.equals("bar") || chartType.equals("pie")) {
                    ArrayList<ColumnDescription> colum = new ArrayList<ColumnDescription>();

                    // System.out.println("inside bar or  pie");

                    colum.add(new ColumnDescription(colHeader[0], ValueType.TEXT, colHeader[0]));
                    colum.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                    // System.out.println("clo1" + colHeader[0] + "col2" + colHeader[1]);
                    chartdata.addColumns(colum);
                }
                else if (chartType.equals("line")) {
                    ArrayList<ColumnDescription> colum = new ArrayList<ColumnDescription>();

                    // System.out.println("inside line");
                    // DataTable chartdata = new DataTable();

                    colum.add(new ColumnDescription(colHeader[0], ValueType.DATETIME, colHeader[0]));
                    colum.add(new ColumnDescription(colHeader[1], ValueType.NUMBER, colHeader[1]));
                    chartdata.addColumns(colum);
                }
                List<Data> cDataList = new ArrayList<Data>();

                while (entpResultset.next()) {

                    // System.out.println("result \t");
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
                            System.out.println("inside else if date" + cData.getName());
                        }
                        else {
                            // val = datavalue.setValue(entpResultset.getDouble(i));
                            cData.setValue(entpResultset.getDouble(i));
                            // System.out.println("inside else" + cData.setValue(entpResultset.getDouble(i)));
                        }

                    }

                    cDataList.add(cData);
                }

                // chart typr data convertion to be added<to-do>
                if (chartType.equals("bar") || chartType.equals("pie")) {
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
                        System.out.println("line values::::\t" + chartdata.toString());

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
                renderchart = JsonRenderer.renderDataTable(chartdata, true, true);
                obj.setChartData(renderchart);
                if (chartType.equals("plain")) {
                    System.out.println("cDatalistsize:" + cDataList.size());
                    System.out.println("cDatalistname:" + cDataList.get(0).getName());
                    System.out.println("cDatalistvalue:" + cDataList.get(0).getValue());
                    if (cDataList.size() > 0) {
                        if (cDataList.get(0).getName() != null && cDataList.get(0).getName() != " ") {
                            obj.setPlain(cDataList.get(0).getName());
                            System.out.println("inside if");
                            System.out.println("obj.getname::::::" + obj.getPlain());

                        }
                        else if (cDataList.get(0).getValue() != 0) {
                            System.out.println("inside else if");
                            obj.setPlainData(cDataList.get(0).getValue());
                            System.out.println("obj.getvalue::::::" + obj.getPlainData());

                        }
                    }
                    else {
                        obj.setPlainData(0);
                        System.out.println("inside else");
                    }
                    System.out.println("getplaindata: \t" + obj.getPlainData());

                }

                sqllist.add(obj);
            }

            JSONArray chartArray = new JSONArray();
            for (int i = 0; i < sqllist.size(); i++) {
                System.out.println("plaiin sqllist data:::\t" + sqllist.get(i).getPlainData());

                // jsonArrayList = sqllist.get(i);
                JSONObject json = new JSONObject();
                json.put("charttype", sqllist.get(i).getChartType());
                json.put("divId", sqllist.get(i).getPositionId());
                if (sqllist.get(i).getChartType().equals("plain")) {
                    System.out.println("chart type in last for" + json.get("charttype") + "plaindaata inside json:::\t"
                            + sqllist.get(i).getPlainData());
                    if (sqllist.get(i).getPlain() != null && sqllist.get(i).getPlain() != " ") {
                        json.put("data", sqllist.get(i).getPlain());
                        System.out.println("chart type in last for" + json.get("charttype")
                                + "plaindaata inside json:::\t" + sqllist.get(i).getPlain());
                    }
                    else if (sqllist.get(i).getPlainData() != 0) {
                        json.put("data", sqllist.get(i).getPlainData());
                        System.out.println("chart type in last for" + json.get("charttype")
                                + "plaindaata inside json:::\t" + sqllist.get(i).getPlainData());
                    }
                }
                // else if (sqllist.get(i).getChartType().equals("line")) {
                // json.put("data", sqllist.get(i).getLineData());
                // System.out.println("chart type in last for" + json.get("charttype") + "linedata inside json::::\t"
                // + sqllist.get(i).getLineData());
                // }
                else {
                    json.put("data", sqllist.get(i).getChartData());
                    System.out.println("chart type in last for" + json.get("charttype") + "chartdata inside json:::\t"
                            + sqllist.get(i).getChartData());
                }
                chartArray.put(json);
                // alist = gson.toJson(sqllist);

            }
            result = chartArray.toString();
        }

        catch (SQLException ex) {
            // System.out.println("exception occured" + ex);
        }

        finally {
            // close connection ,stmt and resultset here
            dbCon.close();
            dbCon1.close();
            // System.out.println("connection state:\t" + dbCon.isClosed());
        }
        return Response.ok().entity(result).build();

    }
}