package gov.nist.csd.pm.pdp.services;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.authority.AuthorityType;
import gov.nist.csd.pm.pap.model.Authority;

public class AuthorityService extends Service {

	public AuthorityService() {

	}

	public AuthorityService(String owner) throws PMException {
		super(owner);
	}

	private static String generateRandomString() {
		final int LEN = 300;
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < LEN; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}
		return sb.toString();
	}

	public Map<String, Object> createAuthority(String type, String name, String apiEndpoint) throws PMException {
		if (null == AuthorityType.toAuthorityType(type)) {
			throw new IllegalArgumentException(MessageHelper.AUTHORITY_TYPE_DOES_NOT_EXISTS);
		} else if (null == name) {
			throw new IllegalArgumentException(MessageHelper.NAME_CANNOT_BE_NULL);
		} else if (null == apiEndpoint || apiEndpoint.isEmpty()) {
			throw new IllegalArgumentException(MessageHelper.INVALID_AUTHORITY_API_END);
		}

		Map<String, Object> response = new HashMap<>();
		String secret = generateRandomString();
		long authorityID = getAuthorityDAO().createAuthority(AuthorityType.toAuthorityType(type), name, apiEndpoint,
				secret, true);
		response.put("authorityID", authorityID);
		response.put("secret", secret);
		return response;
	}

	public void updateAuthority(String name, String apiEndpoint, long id) throws PMException {
		if (null == name) {
			throw new IllegalArgumentException(MessageHelper.NAME_CANNOT_BE_NULL);
		} else if (0 == id || exists(id)) {
			throw new IllegalArgumentException(MessageHelper.AUTHORITY_DOES_NOT_EXIST);
		} else if (null == apiEndpoint || apiEndpoint.isEmpty()) {
			throw new IllegalArgumentException(MessageHelper.INVALID_AUTHORITY_API_END);
		}
		getAuthorityDAO().updateAuthority(name, apiEndpoint, id, true);
	}

	public void deleteAuthority(long id) throws PMException {
		if (0 == id || exists(id)) {
			throw new IllegalArgumentException(MessageHelper.AUTHORITY_DOES_NOT_EXIST);
		}
		getAuthorityDAO().deleteAuthority(id);
	}

	public Authority getAuthority(long id) throws PMException {
		if (0 == id || !exists(id)) {
			throw new IllegalArgumentException(MessageHelper.AUTHORITY_DOES_NOT_EXIST);
		}
		return getAuthorityDAO().getAuthority(id, true);
	}

	public Set<Authority> getAuthorities() throws PMException {
		return getAuthorityDAO().getAuthorities(true);
	}

	public boolean exists(long id) throws PMException {
		return getAuthorityDAO().exists(id, true);
	}
}
