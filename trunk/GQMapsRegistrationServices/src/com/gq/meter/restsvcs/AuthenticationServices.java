/**
 * 
 */
package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
public class AuthenticationServices {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(String authString) {
        Enterprise entObject = null;
        try {
            GQRegistrationConstants.gson.toJson(authString);
            GQGateKeeperConstants.logger.info("Saving the new enterprise : " + entObject.getEnterpriseId());

            EnterpriseModel entmodel = new EnterpriseModel();
            entmodel.updateEnterprise(entObject);
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while creating the new enterprise", e);
            return Response.status(400).build();
        }
        return Response.ok("success").build();
    }

}
