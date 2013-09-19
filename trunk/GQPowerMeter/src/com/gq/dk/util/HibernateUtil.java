package com.gq.dk.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static SessionFactory sf = null;
	
	static{
		Configuration cfg = new Configuration().configure();
		sf = cfg.buildSessionFactory();
	}
	
	
	public static SessionFactory getSessionFactory(){
		return sf;
	}
	
	
	public static void shutDown(){  
        //closes caches and connections  
		getSessionFactory().close();  
    } 
	
}
