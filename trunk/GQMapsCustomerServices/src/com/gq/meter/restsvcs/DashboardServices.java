package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.resultsetmapper.ReflectionResultSetMapper;
import net.sf.resultsetmapper.ResultSetMapper;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.object.Data;
import com.gq.meter.object.TaskAsset;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Path("/DashboardServices")
public class DashboardServices {

    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Path("/getdashboard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboardServices() throws ClassNotFoundException, SQLException {

        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqexchange";
        String username = "gqmaps";
        String password = "Ch1ca803ear$";
        String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqmaps";
        // String dbURL = "jdbc:mysql://localhost:3306/gqexchange";
        // String username = "root";
        // String password = "root";
        // String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqmaps";
        Connection dbCon = null;
        Connection dbCon1 = null;
        Statement stmt = null;
        ResultSet rs = null;
        String finalresult = "";
        int taskId = 0;
        String descr = "";
        String sql = "";
        String chartType = "";
        String[] columnHeader = null;
        String positionId = "";
        String xaxis;
        String yaxis;
        String dynamic = null;
        String relatedDb = null;
        TaskAsset taskAsset = new TaskAsset(taskId, descr, sql, dynamic, chartType, columnHeader, relatedDb, positionId);
        String result = "";
        PreparedStatement pstmt = null;
        String alist = "";
        ResultSet entpResultset;
        String blist = null;
        JSONObject json = new JSONObject();
        List<TaskAsset> sqllist = new ArrayList<TaskAsset>();
        List<Data> datalist = new ArrayList<Data>();
        ResultSetMapper<TaskAsset> resultSetMapper = new ReflectionResultSetMapper<TaskAsset>(TaskAsset.class);
        try {
            // getting database connection to MySQL server
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);
            dbCon1 = DriverManager.getConnection(dbURL1, username, password);
            String currentSql = "select * from task_asst";
            // getting PreparedStatment to execute query
            stmt = (Statement) dbCon.prepareStatement(currentSql);
            // Resultset returned by query
            rs = stmt.executeQuery(currentSql);
            while (rs.next()) {
                System.out.println("inside  while");
                taskId = rs.getInt("ts_id");
                descr = rs.getString("descr");
                sql = rs.getString("tsql");
                dynamic = rs.getString("dynamic");
                // String ctId = rs.getString("ct_id");
                String chartquery = "select ct_Id,descr from chart_type where ct_id=?";
                System.out.println("ct-id");
                taskAsset.setChartType(rs.getString("ct_id"));
                System.out.println("ct-id" + taskAsset.setChartType(rs.getString("ct_id")));
                // stmt = (Statement) dbCon.prepareStatement(chartquery);
                pstmt = (PreparedStatement) dbCon.prepareStatement(chartquery);
                pstmt.setString(1, taskAsset.setChartType(rs.getString("ct_id")));
                ResultSet chrttype = pstmt.executeQuery();
                while (chrttype.next()) {
                    chartType = chrttype.getString("ct_id");
                    String descr1 = chrttype.getString("descr");
                    System.out.println("charttype\t" + chartType + "descr\t" + descr1);
                }
                String[] colHeader = null;
                System.out.println("col header: " + rs.getString("col_hdr"));
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
                System.out.println("tsid\t" + taskId + "descr" + descr + "tsql" + sql + "colheader" + columnHeader
                        + "posid" + positionId);
                // TaskAsset sqldetails = new TaskAsset(taskId, descr, sql, dynamic, chartType, columnHeader, relateDb,
                // positionId);
                TaskAsset obj = new TaskAsset();

                obj.setTaskId(taskId);
                obj.setDescr(descr);
                obj.setSql(sql);
                obj.setDynamic(dynamic);
                obj.setChartType(chartType);
                if (colHeader != null) {
                    obj.setColumnHeader(colHeader);
                }
                else {
                    obj.setColumnHeader(colHeader);

                }

                obj.setRelatedDb(relatedDb);
                obj.setPositionId(positionId);
                // if (relatedDb.equalsIgnoreCase("m")) {
                // System.out.println("maps::::::::::");
                //
                // String entpquery = sql;
                // System.out.println("query is \t" + entpquery);
                // stmt = (Statement) dbCon1.prepareStatement(entpquery);
                // // Resultset returned by query
                // entpResultset = stmt.executeQuery(entpquery);
                // }
                // else {
                // System.out.println("inside else");
                // String entpquery = sql;
                // System.out.println("query is \t" + entpquery);
                // stmt = (Statement) dbCon.prepareStatement(entpquery);
                // // Resultset returned by query
                // entpResultset = stmt.executeQuery(entpquery);
                // }
                //
                // while (entpResultset.next()) {
                // System.out.println("inside second ");
                // Data data = new Data();
                // data.setName(entpResultset.getString(0));
                // data.setValue(entpResultset.getLong(0));
                // datalist.add(data);
                //
                // }
                sqllist.add(obj);

            }
            alist = gson.toJson(sqllist);
            blist = gson.toJson(datalist);

            System.out.println("final result is \t" + sqllist.size());

        }

        catch (SQLException ex) {
            System.out.println("exception occured" + ex);
        }

        finally {
            // close connection ,stmt and resultset here
            // dbCon1.close();
            System.out.println("connection state:\t" + dbCon.isClosed());
        }
        return Response.ok().entity(alist).build();

    }
}