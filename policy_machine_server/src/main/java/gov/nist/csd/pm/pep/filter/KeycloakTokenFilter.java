package gov.nist.csd.pm.pep.filter;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import gov.nist.csd.pm.common.constants.Constants;
import gov.nist.csd.pm.common.exceptions.Errors;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.common.exceptions.PMConfigurationException;
import gov.nist.csd.pm.common.util.annotations.Secured;
import gov.nist.csd.pm.pep.response.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Secured
@Provider
public class KeycloakTokenFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null) {
			requestContext.abortWith(ApiResponse.Builder
					.error(Errors.ERR_AUTHORIZATION, new PMAuthorizationException(Errors.ERR_AUTHORIZATION.name()))
					.build());
		} else {
			Properties properties = null;
			try {
				properties = loadProperties();
			} catch (PMConfigurationException e1) {
				e1.printStackTrace();
			}
			String rsaPublicKey = properties.getProperty("public_key");
			String token = authorizationHeader.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();

			// decode the public key
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
			PublicKey publicKey = null;

			try {
				KeyFactory kf = KeyFactory.getInstance("RSA");
				// created the public key object of the given key
				publicKey = kf.generatePublic(keySpec);
				Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);

			} catch (Exception e) {
				requestContext.abortWith(ApiResponse.Builder
						.error(Errors.ERR_AUTHORIZATION, new PMAuthorizationException(Errors.ERR_AUTHORIZATION.name()))
						.build());
			}
		}
	}

	/**
	 * Read fields from property file.
	 */
	private Properties loadProperties() throws PMConfigurationException {
		Properties props = new Properties();
		InputStream is;

		try {
			is = getClass().getClassLoader().getResourceAsStream("keyManager.config");
			if (is == null) {
				throw new PMConfigurationException("/resource/keycloak.config does not exist");
			}
			props.load(is);

		} catch (IOException e) {
			throw new PMConfigurationException(e.getMessage());
		}
		return props;
	}
}
