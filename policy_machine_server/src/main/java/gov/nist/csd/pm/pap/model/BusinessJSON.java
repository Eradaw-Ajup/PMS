package gov.nist.csd.pm.pap.model;

import java.util.List;

import gov.nist.csd.pm.graph.model.relationships.Association;

public class BusinessJSON {
	private List<MNode> nodes;
	private List<List<Long>> assignments;
	private List<Association> associations;

	public List<MNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<MNode> nodes) {
		this.nodes = nodes;
	}

	public List<List<Long>> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<List<Long>> assignments) {
		this.assignments = assignments;
	}

	public List<Association> getAssociations() {
		return associations;
	}

	public void setAssociations(List<Association> associations) {
		this.associations = associations;
	}
}
