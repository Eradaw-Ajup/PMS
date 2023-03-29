package gov.nist.csd.pm.pep.requests;

import java.util.Set;

public class AssociationJSON {
	private long source;
	private long target;
	private Set<String> operations;

	public AssociationJSON() {
	}

	public AssociationJSON(long source, long target, Set<String> operations) {
		super();
		this.source = source;
		this.target = target;
		this.operations = operations;
	}

	public long getSource() {
		return source;
	}

	public void setSource(long source) {
		this.source = source;
	}

	public long getTarget() {
		return target;
	}

	public void setTarget(long target) {
		this.target = target;
	}

	public Set<String> getOperations() {
		return operations;
	}

	public void setOperations(Set<String> operations) {
		this.operations = operations;
	}

	@Override
	public String toString() {
		return "AssociationJSON [source=" + source + ", target=" + target + ", operations=" + operations + "]";
	}


}
