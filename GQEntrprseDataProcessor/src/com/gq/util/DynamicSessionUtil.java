package com.gq.util;

import java.util.HashMap;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author parveen
 * @change sriram
 */
public class DynamicSessionUtil {

    private static HashMap<String, SessionFactory> SessionFactoryListMap = new HashMap<String, SessionFactory>(10);

    public static SessionFactory getSessionFactory(String dbInstanceName) {

        SessionFactory sessionFactory = null;
        String url = "jdbc:mysql://localhost:3306/" + dbInstanceName;

        if (SessionFactoryListMap.containsKey(dbInstanceName)) {
            GQEDPConstants.logger.info("Existing Session Factory");
            sessionFactory = SessionFactoryListMap.get(dbInstanceName);

            if ((sessionFactory == null) || (sessionFactory.getCurrentSession() == null)) {
                GQEDPConstants.logger.debug("Null session factory so newly creating a sessionfactory for that..");
                createSessionFactory(dbInstanceName, url);
            }
        }
        else {
            GQEDPConstants.logger.debug("No entry in map yet for .." + dbInstanceName);
            createSessionFactory(dbInstanceName, url);
        }
        return SessionFactoryListMap.get(dbInstanceName);
    } // method end

    private static void createSessionFactory(String dbInstanceName, String url) {

        GQEDPConstants.logger.debug("createSessionFactory...");
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", url);
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        GQEDPConstants.logger.debug("Session Factory created");
        SessionFactoryListMap.put(dbInstanceName, sessionFactory);
    }

}
