package com.gq.dk.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gq.dk.exception.GQDKPMException;
import com.gq.dk.util.GQDKPMConstants;
import com.gq.dk.util.HibernateUtil;

public class GQDKPMDBManager {

	public static Enterprise getEnterprise(int enterpriseId) throws GQDKPMException {
		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		
		Session session = sf.openSession();
		Query gqdkEnterpriseDetails = session.getNamedQuery("enterpriseQuery");

		gqdkEnterpriseDetails.setInteger("eid", enterpriseId);

		List<Enterprise> enterpriseList = gqdkEnterpriseDetails.list();

		if ( ( enterpriseList == null ) || ( enterpriseList.size() == 0 ) ) {
			String msg = "No enterprise in the system matches the given enterprise id "+ enterpriseId;
			//GQDKPMConstants.logger.fatal(msg);
			//System.out.println(msg);
			session.close();
			throw new GQDKPMException(msg);
		}
		
		GQDKPMConstants.logger.info("size of the enterprise list is "+ enterpriseList.size());

		session.close();
		return enterpriseList.get(0);  
	}

	public static SystemProfile getProfileValue(String keyId) throws GQDKPMException {
		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		
		Session session = sf.openSession();
		Query gqdkQuery = session.getNamedQuery("profileGetQuery");

		gqdkQuery.setString("keyid", keyId);

		List<SystemProfile> profileList = gqdkQuery.list();

		if ( ( profileList == null ) || ( profileList.size() == 0 ) ) {
			String msg = "No value in the system profile table matches the given key "+ keyId;
			GQDKPMConstants.logger.warn(msg);
			System.out.println(msg);
			session.close();
			throw new GQDKPMException(msg);
		}
		
		GQDKPMConstants.logger.info("size of the system profile list is "+ profileList.size());
		session.close();

		return profileList.get(0);  
	}
	
	public static void main(String[] args) {
		try {
			SystemProfile sp = getProfileValue("GQ_SUPPORT_NUM");
			System.out.println("hone number is = "+ sp.getValue()); 
		} catch (GQDKPMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

} // class ends
