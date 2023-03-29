package gov.nist.csd.pm.pep.requests;


import java.util.List;
import java.util.Set;


public class CreateProhibitionRequest {
	private String name;
	private boolean intersection;
	private Set<String> operations;
	private ProhibitionSubject subject;
	private List<ProhibitionNode> nodes;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIntersection() {
		return intersection;
	}
	public void setIntersection(boolean intersection) {
		this.intersection = intersection;
	}
	public Set<String> getOperations() {
		return operations;
	}
	public void setOperations(Set<String> operations) {
		this.operations = operations;
	}
	public List<ProhibitionNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<ProhibitionNode> nodes) {
		this.nodes = nodes;
	}
	public ProhibitionSubject getSubject() {
		return subject;
	}
	public void setSubject(ProhibitionSubject subject) {
		this.subject = subject;
	}
}
