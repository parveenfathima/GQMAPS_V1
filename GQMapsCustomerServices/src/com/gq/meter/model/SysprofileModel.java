/**
 * 
 */
package com.gq.meter.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gq.meter.object.SysProfile;
import com.gq.meter.util.CustomerServiceUtils;
import com.gq.meter.util.DynamicSessionUtil;

/**
 * @author rathishkumar
 * 
 */

public class SysprofileModel {
	SessionFactory sessionFactory = null;

	// method introduced for changing the compute duration for particular
	// enterprise
	public int updateComputeDuration(String entpId, String value)
			throws Exception {

		Session session = null;
		Transaction tx = null;

		try {
			String dbInstanceName = "gqm" + entpId;

			// Create a session factory for requesting enterprise
			sessionFactory = DynamicSessionUtil
					.getSessionFactory(dbInstanceName);

			// create a session to start a transaction
			session = sessionFactory.getCurrentSession();
			tx = session.beginTransaction();

			String hql = "UPDATE SysProfile SET value =:VALUE WHERE keyy ='COMPUTE_DURATION'";
			Query query = session.createQuery(hql);
			query.setParameter("VALUE", value);
			int result = query.executeUpdate();
			return result;

		} catch (Exception e) {
			CustomerServiceUtils.logger.error(
					"Exception occured while changing the compute duratioin for enterprise  gqm"
							+ entpId, e);
			throw new Exception(e);

		} finally {

			try {
				// commit the unit of work
				tx.commit();

			} catch (Exception e) {
				e.printStackTrace();
				CustomerServiceUtils.logger
						.error("Exception occured while commiting the changes in database ",
								e);
			}
		}
	}
}
