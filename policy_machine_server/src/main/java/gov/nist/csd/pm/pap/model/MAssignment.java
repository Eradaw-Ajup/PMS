package gov.nist.csd.pm.pap.model;

import java.util.Objects;


import gov.nist.csd.pm.graph.model.relationships.Assignment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "assignment")
public class MAssignment extends Assignment {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
	private long assignmentID;
	
	@Column(name = "start_node_id")
	private long srcID;
	
	@Column(name = "end_node_id")
	private long trgID;
	
	@Column(name = "policy_id")
	private long policyID;
	
	@Column(name = "is_assignment_active")
	private boolean isAssignmentActive;
	
	@Column(name = "assignment_status")
	private String assignmentStatus;
	
	@Column(name = "owner")
	private String owner;

	public MAssignment(long sourceID, long targetID, boolean isAssignmentActive, String assignmentStatus,
			long policyID) {
		super(sourceID, targetID);
		this.srcID = sourceID;
		this.trgID = targetID;
		this.isAssignmentActive = isAssignmentActive;
		this.assignmentStatus = assignmentStatus;
		this.policyID = policyID;
	}
	public MAssignment(long id,long sourceID, long targetID, boolean isAssignmentActive, String assignmentStatus,
			long policyID) {
		
		super(sourceID, targetID);
		this.assignmentID = id;
		this.srcID = sourceID;
		this.trgID = targetID;
		this.isAssignmentActive = isAssignmentActive;
		this.assignmentStatus = assignmentStatus;
		this.policyID = policyID;
	}
	public boolean isAssignmentActive() {
		return isAssignmentActive;
	}

	public void setAssignmentActive(boolean isAssignmentActive) {
		this.isAssignmentActive = isAssignmentActive;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public long getPolicyID() {
		return policyID;
	}

	public void setPolicyID(long policyID) {
		this.policyID = policyID;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MAssignment)) {
			return false;
		}

		MAssignment assignment = (MAssignment) o;
		return this.getSourceID() == assignment.getSourceID() && this.getTargetID() == assignment.getTargetID();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSourceID(), getTargetID());
	}

	@Override
	public String toString() {
		return "sourceID:" + getSourceID() + "\ttargetID:" + getTargetID() + "\tpolicyID:" + policyID + "\tstatus:"
				+ assignmentStatus + "\tisAssignmentActive:" + isAssignmentActive;
	}

	public long getAssignmentID() {
		return assignmentID;
	}

	public void setAssignmentID(long assignmentID) {
		this.assignmentID = assignmentID;
	}

	public long getTrg_id() {
		return trgID;
	}

	public void setTrgID(long trgID) {
		this.trgID = trgID;
	}

	public long getSrcID() {
		return srcID;
	}

	public void setSrcID(long srcID) {
		this.srcID = srcID;
	}
}
