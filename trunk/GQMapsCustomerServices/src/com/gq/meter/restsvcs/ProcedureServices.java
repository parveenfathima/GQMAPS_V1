/**
 * 
 */
package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mysql.jdbc.CallableStatement;

/**
 * @author GQ
 * 
 */
@Path("/procedure")
// no generic code as of now have to write in future
public class ProcedureServices {
    @Path("/getproc")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProcedure() throws ClassNotFoundException {
        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqmaps?user=gqmaps&password=Ch1ca803ear$&noAccessToProcedureBodies=true";
        // String dbURL = "jdbc:mysql://192.168.1.95:3306/gqmaps";
        String username = "gqmaps";
        String password = "Ch1ca803ear$";
        Connection dbCon = null;
        Connection dbCon1 = null;
        Statement stmt = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        double out = 0;
        String result = null;
        try {
            // getting database connection to MySQL server
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL);
            String sql = "call calc_pue(?,?)";
            pstmt = (CallableStatement) dbCon.prepareCall(sql);

            pstmt.setString(1, "all");
            pstmt.setString(2, "@it");
            System.out.println("pstmt" + pstmt);
            pstmt.execute();
            pstmt.registerOutParameter(2, Types.DOUBLE);
            out = pstmt.getDouble(2);
            System.out.println("outsize" + out);
            result = Double.toString(out);
        }
        catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return Response.ok(result).build();

    }
}
