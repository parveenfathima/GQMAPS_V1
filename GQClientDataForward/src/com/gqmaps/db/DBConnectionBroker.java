/*
 * Created on May 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gqmaps.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Administrator
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style -
 *         Code Templates
 */
public class DBConnectionBroker {

    public static Connection getConnection() {
        Connection conn = null;
        String url = "jdbc:mysql://192.168.1.95:3306/gqexchange";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "gqexchange";
        String password = "gqe3010";
        try {

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);

        }

        catch (Exception e) {
            System.out.println("Exception is  " + e);
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection con) {

        try {
            if (con != null) con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
