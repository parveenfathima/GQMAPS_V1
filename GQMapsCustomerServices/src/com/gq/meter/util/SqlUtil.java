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
	
	// static values for Database connection
	//static String ExchangeURL = "jdbc:mysql://localhost:3306/gqexchange";
    //static String CustomerURL = "jdbc:mysql://localhost:3306/gqm";
    static String ExchangeURL = "jdbc:mysql://gqmaps.com:3306/gqexchange";
    static String CustomerURL = "jdbc:mysql://gqmaps.com:3306/gqm";
    static String driver = "com.mysql.jdbc.Driver";
    static String username = "gqmaps";
    static String password = "Ch1ca803ear$";
    
    //getting the gqexchange db connection
    public static Connection getExchangeConnection() throws Exception {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(ExchangeURL, username, password);
        return conn;
    }

    //getting the customer db connection
    public static Connection getCustomerConnection(String entpId) throws Exception {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(CustomerURL+ entpId, username, password);
        return conn;
    }

    //getting the customer db connectin for  procedure execution 
    //since no access permission allows to execute the procedure  this call is made
    public static Connection getCustomerConnectionProcedureCall(String entpId) throws Exception {
         String procedureURL = CustomerURL+ entpId
         + "?user=gqmaps&password=Ch1ca803ear$&noAccessToProcedureBodies=true";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(procedureURL);
        return conn;
    }
}
