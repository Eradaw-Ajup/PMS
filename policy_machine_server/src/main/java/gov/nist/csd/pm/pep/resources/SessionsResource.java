package gov.nist.csd.pm.pep.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.LOG;
import gov.nist.csd.pm.common.util.annotations.Secured;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.SessionsService;
import gov.nist.csd.pm.pep.response.ApiResponse;
import gov.nist.csd.pm.security.JwtTokenHelper;

@Path("/sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionsResource {

	@Secured
	@GET
	public Response createSession(@HeaderParam("Authorization") String token) throws PMException {
		LOG.info("USER authenticated");
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", JWT.getUserName(token));
		return ApiResponse.Builder.success().entity(JwtTokenHelper.getInstance().generateToken(JWT.getSubject(token), claims)).build();
	}

	@Path("/{session}")
	@DELETE
	public Response deleteSession(@PathParam("session") String session) throws PMException {
		SessionsService sessionsService = new SessionsService();
		sessionsService.deleteSession(session);
		return ApiResponse.Builder.success().message(ApiResponse.DELETE_SESSION_SUCCESS).build();
	}

	@Path("/{session}")
	@GET
	public Response getSessionUser(@PathParam("session") String session) throws PMException {
		SessionsService sessionsService = new SessionsService();
		long userID = sessionsService.getSessionUserID(session);
		return ApiResponse.Builder.success().entity(userID).build();
	}
}
