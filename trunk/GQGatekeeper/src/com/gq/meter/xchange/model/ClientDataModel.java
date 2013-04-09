/**
 * 
 */
package com.gq.meter.xchange.model;

import java.util.Date;

import org.hibernate.Session;

import com.gq.meter.xchange.object.ClientData;
import com.gq.meter.xchange.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
public class ClientDataModel {

    public void saveClientData(int runId, String data, Date failedDate) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ClientData cData = new ClientData(runId, data, failedDate);
            session.save(cData);

        }
        catch (Exception e) {
            System.out.println("Exception occured while saving the client data");
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                    session.clear();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}