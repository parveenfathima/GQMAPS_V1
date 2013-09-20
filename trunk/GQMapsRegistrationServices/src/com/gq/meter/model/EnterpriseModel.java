/**
 * 
 */
package com.gq.meter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gq.meter.object.Enterprise;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.HibernateUtil;

/**
 * @author Chandru
 * 
 */
public class EnterpriseModel {

    /**
     * This method used to authenticate the user
     * 
     * @param authObject
     * @return
     * @throws Exception
     */
    Properties mailServerProperties;
    javax.mail.Session getMailSession;
    static javax.mail.Session generateMailMessage;

    public static final String SMTP_OUT_SERVER = "smtpout.secureserver.net";
    public static final String USER = "support@gquotient.com"; // godaddy domain
    public static final String PASSWORD = "G&uot!3nt1";

    public boolean authenticate(Enterprise authObject) throws Exception {
        boolean authValue = false;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM Enterprise WHERE user_id = :USER_ID and passwd = :PASSWORD";
            Query query = session.createQuery(hql);
            query.setParameter("USER_ID", authObject.getUserId());
            query.setParameter("PASSWORD", authObject.getPasswd());
            List<Enterprise> entAuthResult = query.list();
            if (entAuthResult.size() != 0) {
                authValue = true;
            }
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
        return authValue;
    }

    /**
     * This method used to fetch all the enterprise registered for GQMaps
     * 
     * @return
     * @throws Exception
     */
    public List<Enterprise> getAllEnterprises() throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM Enterprise";
            Query query = session.createQuery(hql);
            List<Enterprise> entMeterResult = query.list();
            return entMeterResult;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * This method used to create the new enterprise
     * 
     * @param entObject
     * @throws Exception
     */
    public void addEnterprise(Enterprise entObject) throws Exception {
        Session session = null;
        try {
            GQGateKeeperConstants.logger.info("1");
            session = HibernateUtil.getSessionFactory().openSession();
            GQGateKeeperConstants.logger.info("2");
            session.beginTransaction();
            GQGateKeeperConstants.logger.info("3");
            session.save(entObject);
            GQGateKeeperConstants.logger.info("4");
            session.getTransaction().commit();
            GQGateKeeperConstants.logger.info("5");

            String hql = "select max(sid),email from Enterprise ";
            Query query = session.createQuery(hql);
            List<Object[]> entMeterResult = query.list();

            List<Enterprise> entp = new ArrayList<Enterprise>();

            for (Object[] list : entMeterResult) {
                Enterprise e = new Enterprise();
                e.setSid((Short) list[0]);
                entp.add(e);
            }
            System.out.println("reeslur is " + entp.get(0).getSid());
            short sId = entp.get(0).getSid();
            registrationEmail(sId);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the enterprise ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * ON HOLD R8 NOW... WAITING FOR DB CHANGES
     * 
     * @param entObject
     * @throws Exception
     */
    public void updateEnterprise(Enterprise entObject) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Enterprise oldEntObject = (Enterprise) session.load(Enterprise.class, entObject.getSid());
            // only update the changed values
            // if (entObject.getEnterpriseId() == null && entObject.getUserId() == null && entObject.getPasswd() ==
            // null) {
            // throw new Exception("invalid enterprise data for update");
            // }
            oldEntObject.setEnterpriseId(entObject.getEnterpriseId());
            oldEntObject.seteName(entObject.geteName());
            oldEntObject.setUserId(entObject.getUserId());
            oldEntObject.setPasswd(entObject.getPasswd());
            oldEntObject.setNoOfEmpl(entObject.getNoOfEmpl());
            oldEntObject.setEntSqft(entObject.getEntSqft());
            oldEntObject.setEntAssetCount(entObject.getEntAssetCount());
            oldEntObject.setDcSqft(entObject.getDcSqft());
            oldEntObject.setDcAssetCount(entObject.getDcAssetCount());
            oldEntObject.setDcUsePctg(entObject.getDcUsePctg());
            oldEntObject.setDcTemp(entObject.getDcTemp());
            oldEntObject.setRegCmplt(entObject.getRegCmplt());

            session.getTransaction().commit();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the enterprise ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                throw new Exception(e);
            }
        }
    }

    public void updatePassword(Enterprise entObject) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Enterprise oldEntObject = (Enterprise) session.load(Enterprise.class, entObject.getSid());

            oldEntObject.setPasswd(entObject.getPasswd());

            if (entObject.getSecQtn1() != 0 && entObject.getSecQtn2() != 0) {
                oldEntObject.setSecQtn1(entObject.getSecQtn1());
                oldEntObject.setAns1(entObject.getAns1());
                oldEntObject.setSecQtn2(entObject.getSecQtn2());
                oldEntObject.setAns2(entObject.getAns2());
            }

            session.getTransaction().commit();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the enterprise ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                throw new Exception(e);
            }
        }
    }

    public List<Enterprise> registrationEmail(short sId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "select email FROM Enterprise where sid=:S_ID";
            Query query = session.createQuery(hql);
            query.setParameter("S_ID", sId);
            List<Enterprise> entMeterResult = query.list();
            String tomail = String.valueOf(entMeterResult.get(0));
            System.out.println("mail id" + tomail);
            mailServerProperties = System.getProperties();
            // step1
            System.out.println("\n 1st ===> setup Mail Server Properties..");

            mailServerProperties.setProperty("mail.transport.protocol", "smtp");
            mailServerProperties.setProperty("mail.host", SMTP_OUT_SERVER);
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.port", "25");
            mailServerProperties.setProperty("mail.user", USER);
            mailServerProperties.setProperty("mail.password", PASSWORD);
            System.out.println("Mail Server Properties have been setup successfully..");

            // Step2
            System.out.println("\n\n 2nd ===> get Mail Session..");
            generateMailMessage = javax.mail.Session.getDefaultInstance(mailServerProperties, null);
            // generateMailMessage.setDebug(true);
            Transport transport = generateMailMessage.getTransport("smtp");
            MimeMessage message = new MimeMessage(generateMailMessage);
            // message.setSentDate(new java.util.Date());
            message.setSubject("Thank You For Registering With GQMaps");
            message.setFrom(new InternetAddress(USER));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(tomail));
            message.setRecipient(Message.RecipientType.CC, new InternetAddress("support@gquotient.com"));

            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();
            String sb = "Dear Customer,<br><br> <p>Thank  you for registering for the gqmaps application.Your first step towards successfully managing and monitoring your IT infrastructure. </p><p>Your registration process will be complete after manual verification of a few details by a GQuotient expert.</p><p>You will be contacted shortly at the number you have registered with to complete the process within the next 48 business hours.</p><p>If you are not contacted within the above time interval, please contact technical support at support@gquotient.com<p>Please do not reply to this mail.</p><br>Regards,<br><br>GQMaps Support.";
            messageBodyPart.setContent(sb, "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            // Step3
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            transport.connect(SMTP_OUT_SERVER, USER, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
            return entMeterResult;
            // generateAndSendEmail();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the e-mailId ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

    public List<Enterprise> activationEmail(short sId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "select email,userId,passwd FROM Enterprise where sid=:S_ID";
            Query query = session.createQuery(hql);
            query.setParameter("S_ID", sId);
            List<Object[]> entMeterResult = query.list();

            List<Enterprise> entpList = new ArrayList<Enterprise>();

            for (Object[] list : entMeterResult) {
                Enterprise entp = new Enterprise();
                entp.setEmail((String) list[0]);
                entp.setUserId((String) list[1]);
                entp.setPasswd((String) list[2]);
                entpList.add(entp);
            }

            String tomail = String.valueOf(entpList.get(0).getEmail());
            String uName = String.valueOf(entpList.get(0).getUserId());
            String passwd = String.valueOf(entpList.get(0).getPasswd());
            mailServerProperties = System.getProperties();
            // step1
            System.out.println("\n 1st ===> setup Mail Server Properties..");

            mailServerProperties.setProperty("mail.transport.protocol", "smtp");
            mailServerProperties.setProperty("mail.host", SMTP_OUT_SERVER);
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.port", "25");
            mailServerProperties.setProperty("mail.user", USER);
            mailServerProperties.setProperty("mail.password", PASSWORD);
            System.out.println("Mail Server Properties have been setup successfully..");

            // Step2
            System.out.println("\n\n 2nd ===> get Mail Session..");
            generateMailMessage = javax.mail.Session.getDefaultInstance(mailServerProperties, null);
            // generateMailMessage.setDebug(true);
            Transport transport = generateMailMessage.getTransport("smtp");
            MimeMessage message = new MimeMessage(generateMailMessage);
            // message.setSentDate(new java.util.Date());
            message.setSubject("Welcome to GQMaps");
            message.setFrom(new InternetAddress(USER));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(tomail));
            message.setRecipient(Message.RecipientType.CC, new InternetAddress("support@gquotient.com"));

            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();
            String sb = "Dear Customer,<br><p>We are very pleased to welcome you to use the GQMaps application.The application can be accessed via http://www.gqexchange.com:8080/gqmaps</p> <br>User Id: "
                    + uName
                    + "<br>Password: "
                    + passwd
                    + "<br><br><p>Please change your password upon first login.</p><p>If you have any difficulties accessing the application, technical support can be reached via support@gquotient.com</p><p>Please do not reply to this mail.<br>Regards,<br>GQMaps Support.";
            messageBodyPart.setContent(sb, "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            // Step3
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            transport.connect(SMTP_OUT_SERVER, USER, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
            return entpList;
            // generateAndSendEmail();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the e-mail-Id ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * This method used to fetch all details for the registered enterprise in GQMaps
     * 
     * @param passwd
     * 
     * @return
     * @throws Exception
     */
    public List<Enterprise> getEnterpriseDetails(String userId, String passwd) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM Enterprise where  user_id=:UID and passwd=:PWD";
            Query query = session.createQuery(hql);
            query.setParameter("UID", userId);
            query.setParameter("PWD", passwd);

            List<Enterprise> entMeterResult = query.list();
            return entMeterResult;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }

    public List<Enterprise> getEnterprise(String entpId) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String hql = "FROM Enterprise where  enterprise_id=:E_ID";
            Query query = session.createQuery(hql);
            query.setParameter("E_ID", entpId);

            List<Enterprise> entMeterResult = query.list();
            return entMeterResult;
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the enterprises ", e);
            throw new Exception(e);
        }
        finally {
            try {
                if (session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }
            catch (Exception e) {
                GQGateKeeperConstants.logger.error("Exception occured while closing the session ", e);
                throw new Exception(e);
            }
        }
    }
}