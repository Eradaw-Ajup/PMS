package gov.nist.csd.pm.pep.resources;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.common.util.annotations.Secured;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.AnalyticsService;
import gov.nist.csd.pm.pep.response.ApiResponse;

@Path("/analytics")
@PMSecure
public class AnalyticsResource {

    @Path("/pos")
    @Secured
    @GET
    public Response getPos(@HeaderParam("Authorization") String token) throws PMException {
    	AnalyticsService analyticsService = new AnalyticsService(0, JWT.getSubject(token));

    	return ApiResponse.Builder
                .success()
                .entity(analyticsService.getPos())
                .build();
    }

    @Path("/{targetID}/permission/{userID}")
    @GET
    public Response getPermissions(@PathParam("targetID") long targetID, @PathParam("userID") long userID,
    							   @HeaderParam("Authorization") String token) throws PMException {
    	AnalyticsService analyticsService = new AnalyticsService(userID, JWT.getSubject(token));
    	Set<String> permissions = analyticsService.getPermissions(targetID);
        return ApiResponse.Builder
                .success()
                .entity(permissions)
                .build();
    }

    @Path("/explain")
    @GET
    public Response explain(@QueryParam("user") long userID,
                            @QueryParam("target") long targetID,
                            @HeaderParam("Authorization") String token) throws PMException {
    	AnalyticsService analyticsService = new AnalyticsService(0, JWT.getSubject(token));
    	Map<String, List<gov.nist.csd.pm.audit.model.Path>> explain = analyticsService.explain(userID, targetID);
        return ApiResponse.Builder
                .success()
                .entity(explain)
                .build();
    }
}
