package gov.nist.csd.pm.pep.resources;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.CryptoService;
import gov.nist.csd.pm.pep.requests.TokenRequest;
import gov.nist.csd.pm.pep.response.ApiResponse;
import gov.nist.csd.pm.ptl.model.Translation;

@Path("/crypto")
@PMSecure
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CryptoResource {

	@Path("/access-token")
	@POST
	public Response getAccessToken(TokenRequest request, @HeaderParam("Authorization") String token)
			throws PMException, IOException, InterruptedException {
		CryptoService cs = new CryptoService(JWT.getSubject(token));
		return ApiResponse.Builder.success()
				.entity(cs.getAccessToken(request.getNodeID(), request.getToken(), request.getValue())).build();
	}

	@Path("/crypto-json/{id}")
	@GET
	public Response getCryptoJSON(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException {
		CryptoService cs = new CryptoService(JWT.getSubject(token));
		return ApiResponse.Builder
				.success().message(MessageHelper.SUCCESS).entity(new Gson().fromJson(cs.getJSON(id), Translation.class)).build();
	}

	@Path("/setup/{id}")
	@GET
	public Response getSetupJSON(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException, IOException, InterruptedException {
		CryptoService cs = new CryptoService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).entity(cs.getSetupJSON(id)).build();
	}

	@Path("/shares/{id}")
	@GET
	public Response getShares(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException, IOException, InterruptedException {
		CryptoService cs = new CryptoService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).entity(cs.getShares(id)).build();
	}

	@Path("/partial-token/{id}")
	@GET
	public Response getPartialToken(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException, IOException, InterruptedException {
		CryptoService cs = new CryptoService(JWT.getSubject(token));
		return ApiResponse.Builder.success().message(MessageHelper.SUCCESS).entity(cs.getPartialToken(id)).build();
	}
}
