package gov.nist.csd.pm.pap.model;

import java.util.Date;

public class Attempt {
	private long attemptID;
	private String attemptType;
	private String attemptStatus;
	private String createdBy;
	private Date createdAt;
	private Date updatedAt;

	public Attempt() {}

	public Attempt(long attemptID, String attemptType, String attemptStatus, String createdBy, Date createdAt) {
		this.attemptID = attemptID;
		this.attemptType = attemptType;
		this.attemptStatus = attemptStatus;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
	}

	public long getAttemptID() {
		return attemptID;
	}

	public void setAttemptID(long attemptID) {
		this.attemptID = attemptID;
	}

	public String getAttemptType() {
		return attemptType;
	}

	public void setAttemptType(String attemptType) {
		this.attemptType = attemptType;
	}

	public String getAttemptStatus() {
		return attemptStatus;
	}

	public void setAttemptStatus(String attemptStatus) {
		this.attemptStatus = attemptStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
