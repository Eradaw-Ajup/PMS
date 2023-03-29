package gov.nist.csd.pm.pap.authority;

import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.pap.model.Authority;

public interface AuthorityDAO {

	Map<Long, String> getAuthorityTypes() throws PIPException;

	long createAuthority(AuthorityType type, String name, String apiEndpoint, String secret, boolean isActive) throws PIPException;

	void updateAuthority(String name, String apiEndpoint, long id, boolean isActive) throws PIPException;

	void deleteAuthority(long id) throws PIPException;

	Authority getAuthority(long id, boolean isActive) throws PIPException;

	Set<Authority> getAuthorities(boolean isActive) throws PIPException;

	boolean exists(long id, boolean isActive) throws PIPException;
}
