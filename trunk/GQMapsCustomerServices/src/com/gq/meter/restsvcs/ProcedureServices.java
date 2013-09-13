/**
 * 
 */
package com.gq.meter.restsvcs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.util.CustomerServiceConstant;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Statement;

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

        Connection dbCustomer = null;
        Connection dbExchange = null;
        CallableStatement pstmt = null;
        Statement stmt = null;
        double output = 0;
        String result = "";
        String tsql = "";
        ResultSet rs = null;
        try {
            // getting database connection to MySQL server
            dbExchange = SqlUtil.getExchangeConnection();
            dbCustomer = SqlUtil.getCustomerConnectionProcedureCall(entpId);
            String sql = "select * from task_asst WHERE pos_id Like 'div_%' and tsql Like 'call%';";
            stmt = (Statement) dbExchange.createStatement();
            // Resultset returned by query
            rs = stmt.executeQuery(sql);
            CustomerServiceConstant.logger
                    .info("[DASHBOARDSERVICES]  Query sucessfully Executed from the TaskAsst Table");
            while (rs.next()) {
                tsql = rs.getString("tsql");
            }
            System.out.println("procedure sql is+" + tsql);
            String charreplace = tsql.replace("__string_in__", "?");
            String finalquery = charreplace.replace("__double_out__", "?");
            System.out.println("final query is" + finalquery);
            pstmt = (CallableStatement) dbCustomer.prepareCall(finalquery);
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
            CustomerServiceConstant.logger.error("[PROCEDURESERVICES]  Exception Occured during the Connection" + e);
        }
        catch (Exception e) {
            CustomerServiceConstant.logger.error("[PROCEDURESERVICES]  Exception Occured during the Connection" + e);
        }
        return Response.ok(result).build();

    }
}
