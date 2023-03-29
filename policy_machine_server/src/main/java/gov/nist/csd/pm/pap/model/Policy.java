package gov.nist.csd.pm.pap.model;

import java.util.Date;

public class Policy {
	private long id;
	private String name;
	private String type;
	private String description;
	private String policyJSON;
	private String cryptoJSON;
	private String status;
	private String owner;
	private String createdAt;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPolicyJSON() {
		return policyJSON;
	}

	public void setPolicyJSON(String policyJSON) {
		this.policyJSON = policyJSON;
	}

	public String getCryptoJSON() {
		return cryptoJSON;
	}

	public void setCryptoJSON(String cryptoJSON) {
		this.cryptoJSON = cryptoJSON;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = Long.toString(createdAt.getTime());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
