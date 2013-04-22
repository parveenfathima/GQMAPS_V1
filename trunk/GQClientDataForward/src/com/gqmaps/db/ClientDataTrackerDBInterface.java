package com.gqmaps.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterResponse;
import com.gqmaps.util.ClientDataForwardDBConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

public class ClientDataTrackerDBInterface {

    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;

    private Gson gson = new GsonBuilder().create();

    private final Properties readProperties(String xmlFileName) throws Exception {

        Properties properties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);

        properties.loadFromXML(is);
        return properties;
    }

    public String getDeviceDetails() {
        ClientDataForwardDBConstants.logger.info("Get device method is called");

        StringBuilder responseString = new StringBuilder();

        try {
            connection = DBConnectionBroker.getConnection();
            statement = connection.createStatement();

            Properties properties = readProperties("ClientDataForwardSqls.xml");
            String currentDashboardSql = properties.getProperty("clientDataForward.sql");

            ResultSet rs = statement.executeQuery(currentDashboardSql);

            while (rs.next()) {

                String enterprise_id = rs.getString("enterpriseid");
                ClientDataForwardDBConstants.logger.info("Enterprise ID : " + enterprise_id);

                String enterprise_name = rs.getString("enterprisename");
                ClientDataForwardDBConstants.logger.info("Enterprise Name : " + enterprise_name);

                String fwd_url = rs.getString("forwardurl");
                ClientDataForwardDBConstants.logger.info("Forward Url : " + fwd_url);

                String client_data = rs.getString("json");
                ClientDataForwardDBConstants.logger.info("Client data to be send : " + client_data);
                GQMeterResponse gqMeterResponse = gson.fromJson(client_data, GQMeterResponse.class);

                ClientConfig config = new DefaultClientConfig();
                Client client = Client.create(config);

                WebResource service = client.resource(fwd_url);
                service = service.path("gqm-gqedp").path("gqentdataprocessor");

                Form form = new Form();
                form.add("gqMeterResponse", gson.toJson(gqMeterResponse));
                form.add("summary", "Sending the data from client data table to client url");

                Builder builder = service.type(MediaType.APPLICATION_JSON);
                ClientResponse response = builder.post(ClientResponse.class, form);

            } // while ends

            ClientDataForwardDBConstants.logger.info("Client data is successfully saved");

        }
        catch (SQLException e) {
            System.out.println("sqlexception");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("exception");
            e.printStackTrace();
        }
        finally {
            cleanupDBStuff();
        } // end of finally

        return responseString.toString();
    }

    private void cleanupDBStuff() {
        try {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // close connection
        DBConnectionBroker.closeConnection(connection);
    }

}
