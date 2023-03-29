package gov.nist.csd.pm.pap.model;

import java.util.Date;

public class Authority {
	private long authorityID;
	private String authorityType;
	private String name;
	private String apiEndpoint;
	private String secret;
	private boolean isActive;
	private Date createdAt;

	public long getAuthorityID() {
		return authorityID;
	}

	public void setAuthorityID(long authorityID) {
		this.authorityID = authorityID;
	}

	public String getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(String authorityType) {
		this.authorityType = authorityType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getApiEndpoint() {
		return apiEndpoint;
	}

	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Authority() {
	}

	public Authority(long authorityID, String type, String name, boolean isActive, Date createdAt) {
		this.authorityID = authorityID;
		this.authorityType = type;
		this.name = name;
		this.isActive = isActive;
		this.createdAt = createdAt;
	}

}
