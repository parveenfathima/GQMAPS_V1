package com.gq.meter.restsvcs;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.gq.meter.model.SysprofileModel;
import com.gq.meter.object.SysProfile;
import com.gq.meter.util.CustomerServiceUtils;

/**
 * @author rathishkumar
 * 
 */

@Path("/updcmpdur")
public class UpdateComputeDuration {
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateComputeDuration(@QueryParam("entpId") String entpId,
			@QueryParam("value") String value) {
		SysProfile sysObject = null;
		try {
			SysprofileModel sysModel = new SysprofileModel();
			int result = 0;
			CustomerServiceUtils.logger
					.info("changing the compute duration value for the Enterprise: gqm"
							+ entpId + "to " + value);
			result = sysModel.updateComputeDuration(entpId, value);
		} catch (Exception e) {
			e.printStackTrace();
			CustomerServiceUtils.logger
					.error("Error in changing the compute duration value for the Enterprise: gqm"
							+ entpId + "to " + value);
			return Response.status(400).build();
		}
		CustomerServiceUtils.logger
				.info("compute duration sucessfully changed for gqm" + entpId);
		return Response.ok("ok").build();

	}

}
