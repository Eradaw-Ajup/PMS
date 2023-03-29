package gov.nist.csd.pm.pap.model;

import java.util.Objects;
import java.util.Set;

import gov.nist.csd.pm.graph.model.relationships.Association;

public class MAssociation extends Association {

	private static final long serialVersionUID = 1L;

	private long policyID;
	private boolean isAssociationActive;
	private String associationStatus;

	public MAssociation(long sourceID, long targetID, Set<String> operations, boolean isAssociationActive,
			String associationStatus, long policyID) {
		super(sourceID, targetID, operations);
		this.isAssociationActive = isAssociationActive;
		this.associationStatus = associationStatus;
		this.policyID = policyID;
	}

	public boolean isAssociationActive() {
		return isAssociationActive;
	}

	public void setAssociationActive(boolean isAssociationActive) {
		this.isAssociationActive = isAssociationActive;
	}

	public String getAssociationStatus() {
		return associationStatus;
	}

	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
	}

	public long getPolicyID() {
		return policyID;
	}

	public void setPolicyID(long policyID) {
		this.policyID = policyID;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MAssociation)) {
			return false;
		}

		MAssociation association = (MAssociation) o;
		return this.getSourceID() == association.getSourceID() && this.getTargetID() == association.getTargetID()
				&& this.getOperations().equals(association.getOperations());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSourceID(), getTargetID(), getOperations());
	}
}
