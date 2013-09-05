package com.gq.meter.util;

import java.util.HashMap;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try { // Create the SessionFactory from hibernate.cfg.xml
            return new AnnotationConfiguration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("GQGatekeeper : Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static HashMap<String, SessionFactory> SessionFactoryListMap = new HashMap<String, SessionFactory>();

    public SessionFactory dynamicSessionFactory(String url) {
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", url);
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return sessionFactory;
    }
}
