/**
 * 
 */
package com.gq.meter.restsvcs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gq.meter.model.SecurityQuestModel;
import com.gq.meter.object.Protocol;
import com.gq.meter.util.GQGateKeeperConstants;
import com.gq.meter.util.GQRegistrationConstants;

/**
 * @author Chandru
 * 
 */
@Path("/secQuest")
public class SecurityQuestionServices {

    /**
     * This method used to fetch all the protocols
     * 
     * @return
     */
    @Path("/getSecQuestions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSecQuestions() {
        GQGateKeeperConstants.logger.info("Generating all the Security Questions list from GQGatekeeper");
        SecurityQuestModel secQuestModel = new SecurityQuestModel();
        List<Protocol> secQuestResult = null;
        try {
            secQuestResult = secQuestModel.getAllSecQuestions();
        }
        catch (Exception e) {
            GQGateKeeperConstants.logger.error("Exception occured while fetching the Security Questions list ", e);
            return Response.status(400).build();
        }
        // Returning all the enterprises in JSON format
        return Response.ok(GQRegistrationConstants.gson.toJson(secQuestResult)).build();
    }

}
