package com.gq.util;

import java.util.HashMap;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author parveen
 */
public class DynamicSessionUtil {

    public static HashMap<String, SessionFactory> SessionFactoryListMap = new HashMap<String, SessionFactory>();
    public static SessionFactory sessionFactory = null;

    public static SessionFactory dynamicSessionFactory(String dbInstanceName) {

        String url = "jdbc:mysql://localhost:3306/" + dbInstanceName + "?autoReconnect=true";
        if (SessionFactoryListMap.containsKey(dbInstanceName)) {

            GQEDPConstants.logger.info("Existing Session Factory");
            sessionFactory = SessionFactoryListMap.get(dbInstanceName);

        }

        if (sessionFactory == null || (!SessionFactoryListMap.containsKey(dbInstanceName))) {

            GQEDPConstants.logger.debug("Null session factory so newly creating a sessionfactory for that..");
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", url);
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
            SessionFactoryListMap.put(dbInstanceName, sessionFactory);
        }

        return sessionFactory;
    }
}
