package gov.nist.csd.pm.pap.authority;

import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.pap.model.Authority;

public class AuthorityDAOImpl implements AuthorityDAO {
	private AuthorityDAO dao;

	public AuthorityDAOImpl(AuthorityDAO dao) {
		this.dao = dao;
	}

	@Override
	public Map<Long, String> getAuthorityTypes() throws PIPException {
		return dao.getAuthorityTypes();
	}

	@Override
	public void updateAuthority(String name, String apiEndpoint, long id, boolean isActive) throws PIPException {
		dao.updateAuthority(name, apiEndpoint, id, isActive);
	}

	@Override
	public void deleteAuthority(long id) throws PIPException {
		dao.deleteAuthority(id);
	}

	@Override
	public Authority getAuthority(long id, boolean isActive) throws PIPException {
		return dao.getAuthority(id, isActive);
	}

	@Override
	public boolean exists(long id, boolean isActive) throws PIPException {
		return dao.exists(id, isActive);
	}

	@Override
	public long createAuthority(AuthorityType type, String name, String apiEndpoint, String secret, boolean isActive)
			throws PIPException {
		return dao.createAuthority(type, name, apiEndpoint, secret, isActive);
	}

	@Override
	public Set<Authority> getAuthorities(boolean isActive) throws PIPException {
		return dao.getAuthorities(isActive);
	}
}
