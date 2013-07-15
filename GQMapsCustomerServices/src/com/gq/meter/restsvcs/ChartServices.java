/**
 * 
 */
package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.gq.meter.object.PieChart;
import com.gq.meter.util.CustomerServiceConstant;

/**
 * @author Rathish
 * 
 */
@Path("/chartservices")
public class ChartServices {

    @Path("/getPiechart")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPiechart() throws ClassNotFoundException, SQLException {
        CustomerServiceConstant.logger.info("Generating Charting informations for the enterprise");

        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqmaps";
        String username = "gqmaps";
        String password = "Ch1ca803ear$";

        /*
         * String dbURL = "jdbc:mysql://localhost:3306/gqmaps"; String username = "root"; String password = "root"
         */;

        Connection dbCon = null;
        Statement stmt = null;
        ResultSet rs = null;

        String query = "SELECT 'other',(SELECT SUM(a.mem_share_kb) AS s FROM comp_proc a,meter_run c "
                + "WHERE DATE(c.rec_dttm) = CURDATE() AND c.run_id = a.run_id) - (SELECT "
                + "SUM(s) FROM (SELECT a.proc_name, SUM(a.mem_share_kb) AS s FROM "
                + "comp_proc a, meter_run c WHERE DATE(c.rec_dttm) = CURDATE() "
                + "AND c.run_id = a.run_id AND a.asset_id = (SELECT asset_id "
                + "FROM (SELECT a.asset_id, AVG(a.cpu_load) FROM comp_snpsht a, meter_run c WHERE "
                + "DATE(c.rec_dttm) = CURDATE() AND c.run_id = a.run_id GROUP BY a.asset_id "
                + "ORDER BY 2 DESC LIMIT 1) kk) GROUP BY a.proc_name ORDER BY 2 DESC LIMIT 5) kk) AS memload "
                + "UNION SELECT a.proc_name, SUM(a.mem_share_kb) AS s FROM comp_proc a,meter_run c WHERE DATE(c.rec_dttm) = CURDATE() AND c.run_id = a.run_id AND a.asset_id = (SELECT asset_id "
                + "FROM (SELECT a.asset_id, AVG(a.cpu_load) FROM comp_snpsht a, meter_run c WHERE DATE(c.rec_dttm) = CURDATE() AND c.run_id = a.run_id GROUP BY a.asset_id ORDER BY 2 DESC "
                + "LIMIT 1) kk)GROUP BY a.proc_name ORDER BY 2 DESC LIMIT 5";

        List<PieChart> pieChart = new ArrayList<PieChart>();

        try {
            // getting database connection to MySQL server
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL, username, password);

            // getting PreparedStatment to execute query
            stmt = dbCon.prepareStatement(query);

            // List<PieChart> pieChart = new ArrayList<PieChart>();
            // Resultset returned by query
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("other");
                String memLoad = rs.getString("memload");
                System.out.println(name);
                System.out.println(memLoad);
                PieChart p = new PieChart();
                p.setName(name);
                p.setMemLoad(memLoad);
                pieChart.add(p);
            }

        }
        catch (SQLException ex) {
            System.out.println("exception occured" + ex);
        }
        finally {
            // close connection ,stmt and resultset here
        }
        // Returning all the enterprises in JSON format
        return CustomerServiceConstant.gson.toJson(pieChart);
    }

}
