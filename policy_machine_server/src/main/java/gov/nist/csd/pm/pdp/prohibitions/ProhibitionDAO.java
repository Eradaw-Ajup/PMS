package gov.nist.csd.pm.pdp.prohibitions;

import java.util.List;

import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.pap.model.Prohibition;

public class ProhibitionDAO implements ProhibitionMachine {

	ProhibitionMachine dao;

	public ProhibitionDAO(ProhibitionMachine dao) {
		this.dao = dao;
	}

	@Override
	public int add(Prohibition prohibition) throws PIPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Prohibition> getAll() throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Prohibition get(int prohibitionId) throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Prohibition> getProhibitionsFor(String subject) throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(int prohibitionId, Prohibition prohibition) throws PIPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int prohibitionId) throws PIPException {
		// TODO Auto-generated method stub
	}
}
