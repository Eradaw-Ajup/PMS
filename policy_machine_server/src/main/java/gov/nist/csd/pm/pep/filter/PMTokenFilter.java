package gov.nist.csd.pm.pep.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import gov.nist.csd.pm.common.exceptions.Errors;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.common.util.LOG;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.pep.response.ApiResponse;
import gov.nist.csd.pm.security.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;

@PMSecure
@Provider
public class PMTokenFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		final JwtTokenHelper jwtTokenHelper = JwtTokenHelper.getInstance();
		final String requestTokenHeader = request.getHeaderString("Authorization");

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenHelper.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				LOG.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				LOG.error("JWT Token has expired");
			}
		} else {
			LOG.warn("JWT Token does not begin with Bearer String");
		}

		System.out.println("username: " + username);
		System.out.println("token: " + jwtToken);
		System.out.println("validateToken: " + jwtTokenHelper.validateToken(jwtToken));
		// if token is valid configure Spring Security to manually set
		// authentication
		if (jwtTokenHelper.validateToken(jwtToken) && username != null && !username.isEmpty()) {
			LOG.info(username + " " + ApiResponse.SUCCESS_MSG);
		} else {
			LOG.error(username + " " + Errors.ERR_AUTHORIZATION);

			request.abortWith(ApiResponse.Builder
					.error(Errors.ERR_AUTHORIZATION, new PMAuthorizationException(Errors.ERR_AUTHORIZATION.name()))
					.build());
		}
	}
}
