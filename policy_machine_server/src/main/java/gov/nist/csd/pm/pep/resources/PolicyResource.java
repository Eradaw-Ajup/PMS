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

import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.model.Policy;
import gov.nist.csd.pm.pdp.services.PolicyService;
import gov.nist.csd.pm.pep.requests.PublishPolicyRequest;
import gov.nist.csd.pm.pep.response.ApiResponse;

@PMSecure
@Path("/policies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PolicyResource {

	@Path("/import")
	@POST
	public Response importPolicy(Policy request, @QueryParam("policyName") String policyName,
			@HeaderParam("Authorization") String token) throws PMException {
		PolicyService ps = new PolicyService(JWT.getSubject(token));
		ps.importPolicy(request, policyName);
		return ApiResponse.Builder.success().entity("policy created successfully").build();
	}

	@Path("/export/{policyID}")
	@GET
	public Response exportPolicy(@PathParam("policyID") long policyID, @HeaderParam("Authorization") String token)
			throws PMException {
		PolicyService ps = new PolicyService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(ps.exportPolicy(policyID)).build();
	}

	@Path("/publish")
	@PUT
	public Response publishPolicy(PublishPolicyRequest request, @HeaderParam("Authorization") String token)
			throws PMException, Exception {
		PolicyService ps = new PolicyService(JWT.getSubject(token));
		ps.publish(request.getPolicyID(), request.getAttemptID(), request.getName(), request.getDescription());
		return ApiResponse.Builder.success().message(ApiResponse.POLICY_PUBLISH_SUCCESS).build();
	}

	@GET
	public Response getPolicies(@QueryParam("status") String status, @HeaderParam("Authorization") String token)
			throws PMException {
		PolicyService ps = new PolicyService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(ps.getPolicies(status)).build();
	}

	@Path("/{policyID}")
	@GET
	public Response getPolicy(@PathParam("policyID") long policyID, @HeaderParam("Authorization") String token)
			throws PMException {
		PolicyService ps = new PolicyService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(ps.getPolicy(policyID)).build();
	}

	@Path("/cancel")
	@DELETE
	public Response cancelPolicy(Policy policy, @HeaderParam("Authorization") String token) throws PMException {
		PolicyService ps = new PolicyService(JWT.getSubject(token));
		ps.cancel(policy);
		return ApiResponse.Builder.success().message("policy deleted").build();
	}
}
