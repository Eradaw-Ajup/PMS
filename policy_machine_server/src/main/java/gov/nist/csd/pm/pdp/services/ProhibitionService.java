package gov.nist.csd.pm.pdp.services;

import java.util.List;

import gov.nist.csd.pm.common.util.OperationSet;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.model.Prohibition;

public class ProhibitionService extends Service {

	public ProhibitionService(String owner) throws PMException {
		super(owner);
	}

	public int add(String name, String subject, OperationSet operations) throws PMException {
		/*
		if (null == name) {
			throw new IllegalArgumentException("no name was provided when creating a prohibition");
		} else if (null == subject) {
			throw new IllegalArgumentException("no subject was provided when creating a prohibition");
		}

		OperationSet os = operations;
		if (os == null || os.isEmpty()) {
			throw new IllegalArgumentException("no operations were provided when creating a prohibition");
		}
		for (String i : operations) {
			if (!Operations.toOperations(i))
				throw new IllegalArgumentException("invalid operations were provided when creating a prohibition");
		}
		return store.add(prohibition);
		*/
		return 0;
	}

	public List<Prohibition> getAll() throws PMException {
		//return store.getAll();
		return null;
	}

	public Prohibition get(int prohibitionId) throws PMException {
		/*
		if (store.get(prohibitionId).getStatus() == false)
			throw new IllegalArgumentException("Invalid Id");
		return store.get(prohibitionId);
		*/
		return null;
	}

	public List<Prohibition> getProhibitionsFor(String prohibitionsubject) throws PMException {
		/*
		return store.getProhibitionsFor(prohibitionsubject);
		*/
		return null;
	}

	public void update(long prohibitionId, String name, String subject, OperationSet operations) throws PMException {
		/*
		if (store.get(prohibitionId) == null)
			throw new NoSuchElementException();
		if (store.get(prohibitionId).getStatus() == false)
			throw new NoSuchElementException();
		if (prohibition.getName() == null)
			throw new IllegalArgumentException("no name was provided when creating a prohibition");
		if (prohibition.getSubject() == null)
			throw new IllegalArgumentException("no subject was provided when creating a prohibition");
		OperationSet os = prohibition.getOperations();
		if (os == null || os.size() == 0)
			throw new IllegalArgumentException("no operations were provided when creating a prohibition");
		for (String i : prohibition.getOperations()) {
			if (Operations.toOperations(i) == null)
				throw new IllegalArgumentException("invalid operations were provided when creating a prohibition");
		}
		store.update(prohibitionId, prohibition);
		*/

	}

	public void delete(int prohibitionId) throws PMException {
		/*
		if (store.get(prohibitionId) == null)
			throw new IllegalArgumentException("Invalid Id");
		store.delete(prohibitionId);
		*/
	}
}
