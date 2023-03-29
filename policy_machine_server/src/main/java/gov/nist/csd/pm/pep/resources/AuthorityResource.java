package gov.nist.csd.pm.pep.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.AuthorityService;
import gov.nist.csd.pm.pep.requests.AuthorityRequest;
import gov.nist.csd.pm.pep.response.ApiResponse;

@PMSecure
@Path("/authority")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorityResource {
	// create, update, delete, read

	@POST
	public Response createAuthority(AuthorityRequest request, @HeaderParam("Authorization") String token) throws PMException {
		AuthorityService service = new AuthorityService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS)
				.entity(service.createAuthority(request.getType(), request.getName(), request.getApiEndpoint())).build();
	}

	@PUT
	public Response updateAuthority(AuthorityRequest request, @HeaderParam("Authorization") String token) throws PMException {
		AuthorityService service = new AuthorityService(JWT.getSubject(token));
		service.updateAuthority(request.getName(), request.getApiEndpoint(), request.getId());
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).build();
	}

	@Path("/{id}")
	@DELETE
	public Response deleteAuthority(@PathParam("id") long id, @HeaderParam("Authorization") String token) throws PMException {
		AuthorityService service = new AuthorityService(JWT.getSubject(token));
		service.deleteAuthority(id);
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).build();
	}

	@Path("/{id}")
	@GET
	public Response getAuthority(@PathParam("id") long id, @HeaderParam("Authorization") String token) throws PMException {
		AuthorityService service = new AuthorityService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).entity(service.getAuthority(id)).build();
	}
}
