package gov.nist.csd.pm.pep.response;

import java.util.List;
import java.util.Set;

import gov.nist.csd.pm.pep.requests.ProhibitionNode;
import gov.nist.csd.pm.pep.requests.ProhibitionSubject;

public class ProhibitionResponse {
	private String name;
    private ProhibitionSubject subject;
    private List<ProhibitionNode> nodes;
    private Set<String> operations;
    private boolean intersection;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProhibitionSubject getSubject() {
		return subject;
	}
	public void setSubject(ProhibitionSubject subject) {
		this.subject = subject;
	}
	public List<ProhibitionNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<ProhibitionNode> nodes) {
		this.nodes = nodes;
	}
	public Set<String> getOperations() {
		return operations;
	}
	public void setOperations(Set<String> operations) {
		this.operations = operations;
	}
	public boolean isIntersection() {
		return intersection;
	}
	public void setIntersection(boolean intersection) {
		this.intersection = intersection;
	}
}
