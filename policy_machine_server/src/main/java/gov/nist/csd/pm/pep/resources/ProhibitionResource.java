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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.ProhibitionService;
import gov.nist.csd.pm.pep.requests.ProhibitionRequest;
import gov.nist.csd.pm.pep.response.ApiResponse;

@PMSecure
@Path("/prohibitions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProhibitionResource {

	@GET
	public Response getAll(@HeaderParam("Authorization") String token) throws PMException {
		ProhibitionService service = new ProhibitionService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(service.getAll()).build();
	}

	@POST
	public Response add(ProhibitionRequest request, @HeaderParam("Authorization") String token) throws PMException {
		ProhibitionService service = new ProhibitionService(JWT.getSubject(token));
		return ApiResponse.Builder.success()
				.entity(service.add(request.getName(), request.getSubject(), request.getOperations())).build();
	}

	@PUT
	public Response update(ProhibitionRequest request, @HeaderParam("Authorization") String token) throws PMException {
		ProhibitionService service = new ProhibitionService(JWT.getSubject(token));
		service.update(request.getId(), request.getName(), request.getSubject(), request.getOperations());
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).build();
	}

	@DELETE
	public Response delete(@QueryParam("prohibitionId") int prohibitionId, @HeaderParam("Authorization") String token)
			throws PMException {
		ProhibitionService service = new ProhibitionService(JWT.getSubject(token));
		service.delete(prohibitionId);
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).build();
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int prohibitionId, @HeaderParam("Authorization") String token)
			throws PMException {
		ProhibitionService service = new ProhibitionService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(service.get(prohibitionId)).build();
	}

	@GET
	@Path("/subject/{prohibitionSubject}")
	public Response getProhibitionsFor(@PathParam("prohibitionSubject") String prohibitionSubject,
			@HeaderParam("Authorization") String token) throws PMException {
		ProhibitionService service = new ProhibitionService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(service.getProhibitionsFor(prohibitionSubject)).build();
	}

}
