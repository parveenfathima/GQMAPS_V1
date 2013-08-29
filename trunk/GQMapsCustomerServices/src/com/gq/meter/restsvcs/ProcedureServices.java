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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.util.CustomerServiceConstant;
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
    public Response getProcedure(@QueryParam("entpId") String entpId) throws ClassNotFoundException {
        String dbURL = "jdbc:mysql://192.168.1.95:3306/gqm" + entpId
                + "?user=gqmaps&password=Ch1ca803ear$&noAccessToProcedureBodies=true";
        // String dbURL = "jdbc:mysql://192.168.1.95:3306/gqmaps";
        Connection dbCustomer = null;
        Statement stmt = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        double output = 0;
        String result = null;
        try {
            // getting database connection to MySQL server
            Class.forName("com.mysql.jdbc.Driver");
            dbCustomer = DriverManager.getConnection(dbURL);
            String sql = "{call calc_pue(?,?)}";
            pstmt = (CallableStatement) dbCustomer.prepareCall(sql);
            pstmt.setString(1, "all");
            pstmt.setString(2, "@it");
            System.out.println("pstmt" + pstmt);
            pstmt.execute();
            pstmt.registerOutParameter(2, Types.DOUBLE);
            output = pstmt.getDouble(2);
            System.out.println("outsize" + output);
            result = Double.toString(output);
        }
        catch (SQLException e) {
            CustomerServiceConstant.logger.info("[PROCEDURESERVICES]  Exception Occured during the Connection" + e);
        }
        return Response.ok(result).build();

    }
}
