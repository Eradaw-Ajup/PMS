package gov.nist.csd.pm.common.util;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.csd.pm.common.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JWT {

	private static final String SUBJECT = "sub";
	private static final String EMAIL = "email";
	private static final String USERNAME ="preferred_username";

	private static Map<String, String> getClaims(String jwt)
			throws JsonParseException, JsonMappingException, java.io.IOException {
		String claims = jwt.split("\\.")[1];
		String jsonClaims = new String(Base64.getDecoder().decode(claims));
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonClaims, new TypeReference<Map<String, Object>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getToken(final String token) {
		return token.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();
	}

	public static String getSubject(final String token) {
		String sub = null;

		try {
			sub = JWT.getClaims(JWT.getToken(token)).get(SUBJECT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sub;
	}

	public static String getEmail(final String token) {
		String email = null;
		try {
			email = JWT.getClaims(JWT.getToken(token)).get(EMAIL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return email;
	}

	public static String getUserName(final String token) {
		try {
			return JWT.getClaims(JWT.getToken(token)).get(USERNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean isExpired(Jws<Claims> jwt) {
		Date expired = jwt.getBody().getExpiration();
		return expired.before(new Date());
	}
}
