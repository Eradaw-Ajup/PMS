package gov.nist.csd.pm.pep.resources;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.AttemptService;
import gov.nist.csd.pm.pep.response.ApiResponse;

@PMSecure
@Path("/attempt")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttemptResource {

	@POST
	public Response createAttempt(Map<String, String> request, @HeaderParam("Authorization") String token) throws PMException {
		AttemptService attemptService = new AttemptService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message("Attempt creation success").entity(attemptService.createAttempt(request)).build();
	}

	@Path("/{attemptID}")
	@GET
	public Response getAttempt(@PathParam("attemptID") long attemptID, @HeaderParam("Authorization") String token) throws PMException {
		AttemptService attemptService = new AttemptService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message("Attempt creation success").entity(attemptService.getAttempt(attemptID)).build();
	}
}
