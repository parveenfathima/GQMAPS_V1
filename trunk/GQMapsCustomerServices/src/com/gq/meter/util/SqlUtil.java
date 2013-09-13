/**
 * 
 */
package com.gq.meter.util;

import java.sql.DriverManager;

import java.sql.Connection;

/**
 * @author Rathish
 * 
 */
public class SqlUtil {
    static String entpId = "";

    public static Connection getExchangeConnection() throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/gqexchange";
        // String dbURL = "jdbc:mysql://192.168.1.95:3306/gqexchange";

        String username = "gqmaps";
        String password = "Ch1ca803ear$";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(dbURL, username, password);
        System.out.println("connparameters\t" + "DBURL\t" + dbURL);
        return conn;
    }

    public static Connection getCustomerConnection(String entpId) throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String dbURL1 = "jdbc:mysql://localhost:3306/gqm" + entpId;
        // String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqm" + entpId;

        String username = "gqmaps";
        String password = "Ch1ca803ear$";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(dbURL1, username, password);
        System.out.println("connparameters\t" + "DBURL\t" + dbURL1);
        return conn;
    }

    public static Connection getCustomerConnectionProcedureCall(String entpId) throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        // String dbURL1 = "jdbc:mysql://192.168.1.95:3306/gqm" + entpId
        // + "?user=gqmaps&password=Ch1ca803ear$&noAccessToProcedureBodies=true";
        String dbURL1 = "jdbc:mysql://localhost:3306/gqm" + entpId
                + "?user=gqmaps&password=Ch1ca803ear$&noAccessToProcedureBodies=true";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(dbURL1);
        System.out.println("connparameters\t" + "DBURL\t" + dbURL1);
        return conn;
    }
}
