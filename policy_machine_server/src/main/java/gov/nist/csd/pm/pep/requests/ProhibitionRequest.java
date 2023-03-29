package gov.nist.csd.pm.pep.requests;

import gov.nist.csd.pm.common.util.OperationSet;

public class ProhibitionRequest {
	private long id;
	private String name;
	private String subject;
	private OperationSet operations;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public OperationSet getOperations() {
		return operations;
	}

	public void setOperations(OperationSet operations) {
		this.operations = operations;
	}
}
