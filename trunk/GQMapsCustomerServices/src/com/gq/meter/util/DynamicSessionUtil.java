package com.gq.meter.util;

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
        // String url = "jdbc:mysql://192.168.8.15:3306/" + dbInstanceName;

        if (SessionFactoryListMap.containsKey(dbInstanceName)) {
            sessionFactory = SessionFactoryListMap.get(dbInstanceName);

            if ((sessionFactory == null) || (sessionFactory.getCurrentSession() == null)) {
                createSessionFactory(dbInstanceName, url);
            }
        }
        else {
            createSessionFactory(dbInstanceName, url);
        }
        return SessionFactoryListMap.get(dbInstanceName);
    } // method end

    private static void createSessionFactory(String dbInstanceName, String url) {

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", url);
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        SessionFactoryListMap.put(dbInstanceName, sessionFactory);
    }

}
