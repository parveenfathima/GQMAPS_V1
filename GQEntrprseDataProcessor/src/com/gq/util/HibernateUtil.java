package com.gq.util;

import java.util.HashMap;

import org.hibernate.SessionFactory;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class HibernateUtil {

    public SessionFactory sessionFactory;

    public static HashMap<String, SessionFactory> SessionFactoryListMap = new HashMap<String, SessionFactory>();

    public SessionFactory dynamicSessionFactory(String url) {
        SessionFactory sessionFactory = null;
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.setProperty("hibernate.connection.url", url);
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }
}
