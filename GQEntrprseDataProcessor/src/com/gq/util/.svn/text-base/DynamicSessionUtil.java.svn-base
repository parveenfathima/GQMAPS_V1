package com.gq.util;

import java.util.HashMap;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author parveen
 * @change sriram
 */
public class DynamicSessionUtil {

    private static HashMap<String, SessionFactory> SessionFactoryListMap = new HashMap<String, SessionFactory>(10);

    public static synchronized SessionFactory getSessionFactory(String dbInstanceName) {

        SessionFactory sessionFactory = null;
        String url = "jdbc:mysql://localhost:3306/" + dbInstanceName;

        if (SessionFactoryListMap.containsKey(dbInstanceName)) {
            GQEDPConstants.logger.info("Session Factory exists for " + dbInstanceName);
            sessionFactory = SessionFactoryListMap.get(dbInstanceName);
            try {
                if ((sessionFactory == null) || (sessionFactory.getCurrentSession() == null)) {
                    GQEDPConstants.logger.debug("Null session factory so newly creating one for .." + dbInstanceName);
                    createSessionFactory(dbInstanceName, url);
                }
            }
            catch(HibernateException he){
            	he.printStackTrace();
                GQEDPConstants.logger.debug("HibernateException so newly creating one for .." + dbInstanceName);

                try {
                	createSessionFactory(dbInstanceName, url);
                }
                catch(HibernateException he1){
                	he.printStackTrace();
                    GQEDPConstants.logger.error("HibernateException createSessionFactory .." + dbInstanceName);
                }
            }
        }
        else {
            GQEDPConstants.logger.debug("No entry in map yet for .." + dbInstanceName + " , creating one ...");
            createSessionFactory(dbInstanceName, url);
        }
        
        return SessionFactoryListMap.get(dbInstanceName);
    } // method end

    private static void createSessionFactory(String dbInstanceName, String url) {

		    Configuration configuration = new Configuration();
		    configuration.setProperty("hibernate.connection.url", url);
		    configuration.configure();
		    SessionFactory sessionFactory = configuration.buildSessionFactory();
		    GQEDPConstants.logger.debug(" Session Factory created for "+dbInstanceName);
		    SessionFactoryListMap.put(dbInstanceName, sessionFactory);
    } // method ends
}//class ends
