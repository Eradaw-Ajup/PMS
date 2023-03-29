package gov.nist.csd.pm.rap.resources;

import java.util.Map;

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

import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pep.response.ApiResponse;
import gov.nist.csd.pm.rap.model.Asset;
import gov.nist.csd.pm.rap.services.ResourceAccessPointService;

@PMSecure
@Path("/assets")
public class ResourceAccessPointResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAsset(Asset asset, @HeaderParam("Authorization") String token) throws PMException {
		ResourceAccessPointService rap = new ResourceAccessPointService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(rap.createAsset(asset)).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAsset(Asset asset, @HeaderParam("Authorization") String token) throws PMException {
		ResourceAccessPointService rap = new ResourceAccessPointService(JWT.getSubject(token));
		rap.updateAsset(asset);
		return ApiResponse.Builder.success().message(ApiResponse.UPDATE_ASSET_SUCCESS).build();
	}

	@Path("/{id}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAsset(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException {
		ResourceAccessPointService rap = new ResourceAccessPointService(JWT.getSubject(token));
		rap.deleteAsset(id);
		return ApiResponse.Builder.success().message(ApiResponse.DELETE_ASSET_SUCCESS).build();
	}

	@Path("/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAsset(@PathParam("id") long id, @HeaderParam("Authorization") String token) throws PMException {
		ResourceAccessPointService rap = new ResourceAccessPointService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(rap.getAsset(id)).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAssets(@HeaderParam("Authorization") String token) throws PMException {
		ResourceAccessPointService rap = new ResourceAccessPointService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(rap.getAssets()).build();
	}

	@Path("/link")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response linkAsset(Map<String, String> request, @HeaderParam("Authorization") String token)
			throws PMException {
		ResourceAccessPointService rap = new ResourceAccessPointService(JWT.getSubject(token));
		return ApiResponse.Builder.success()
				.entity(rap.linkAsset(Long.parseLong(request.get("assetID")), Long.parseLong(request.get("policyID"))))
				.build();
	}
}
