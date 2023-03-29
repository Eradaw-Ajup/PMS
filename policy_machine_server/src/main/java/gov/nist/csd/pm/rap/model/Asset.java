package gov.nist.csd.pm.rap.model;

import java.util.Date;

public class Asset {
	private long id;
	private String name;
	private String type;
	private String status;
	private String link;
	private String owner;
	private String createdAt;
	private boolean isActive;

	public Asset() {
	}

	public Asset(long id, String name, String type, String status, String link, String owner, Date createdAt,
			boolean isActive) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.status = status;
		this.link = link;
		this.owner = owner;
		this.createdAt = Long.toString(createdAt.getTime());
		this.isActive = isActive;
	}

	/*
	 * Getters and Setters
	 */
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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
}
