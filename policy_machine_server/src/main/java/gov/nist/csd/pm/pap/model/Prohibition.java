package gov.nist.csd.pm.pap.model;

import java.util.Date;
import java.util.Objects;

import gov.nist.csd.pm.common.util.OperationSet;

/**
 * Object representing a Prohibition.
 */
public class Prohibition {

	private int id;
	private String name;
	private String subject;
	private OperationSet operations;
	private Date createdAt;
	private Boolean status;

	public Prohibition(int id, String name, String subject, OperationSet operations, Date createdAt, Boolean status) {
		this.name = name;
		this.subject = subject;
		this.createdAt = createdAt;
		this.status = status;
		this.id = id;
		this.operations = operations;
	}

	public Prohibition() {
	}

	public Prohibition(String name, String subject, OperationSet os) {
		this.name = name;
		this.subject = subject;
		this.operations = os;
	}

	/*
	 * Getters and Setters
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Prohibition)) {
			return false;
		}

		Prohibition p = (Prohibition) o;
		return this.getName().equals(p.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
