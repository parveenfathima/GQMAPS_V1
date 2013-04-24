/**
 * 
 */
package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.EnterpriseModel;
import com.gq.meter.object.Enterprise;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/authenticate")
public class AuthenticationServices {

    /**
     * This method used to authenticate the user
     * 
     * @param authString
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(String authString) {
        Enterprise authObject = null;
        try {
            GQGateKeeperConstants.logger.info("authString : " + authString);
            authObject = GQRegistrationConstants.gson.fromJson(authString, Enterprise.class);
            EnterpriseModel entModel = new EnterpriseModel();
            boolean authValue = entModel.authenticate(authObject);
            if (authValue) {
                return Response.ok("success").build();
            }
            else {
                return Response.status(401).build();
            }
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(401).build();
        }
    }
}
