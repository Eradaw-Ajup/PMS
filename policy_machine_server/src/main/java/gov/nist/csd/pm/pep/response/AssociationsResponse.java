package gov.nist.csd.pm.pep.response;

import java.util.HashSet;
import java.util.Set;

import gov.nist.csd.pm.graph.model.nodes.Node;

public class AssociationsResponse {

	private Set<Node> nodes;
	private Set<String> associations;

	public AssociationsResponse()
	{
		associations = new HashSet<>();
		nodes = new HashSet<>();
	}


	public Set<Node> getNodes() {
		return nodes;
	}
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<String> getAssociations() {
		return associations;
	}

	public void setAssociations(Set<String> associations) {
		this.associations = associations;
	}
}
