package gov.nist.csd.pm.pep.requests;

import java.util.HashMap;
import java.util.Map;

public class CreateNodeRequest {
	private long id;
	private String name;
	private String type;
	private String storeType;
	private Map<String, String> properties;
	private long policyID;
	private long attemptID;
	private long attributeID;
	private long authorityID;

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

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public Map<String, String> getProperties() {
		if (null == properties)
			properties = new HashMap<>();
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public long getPolicyID() {
		return policyID;
	}

	public void setPolicyID(long policyID) {
		this.policyID = policyID;
	}

	public long getAttemptID() {
		return attemptID;
	}

	public void setAttemptID(long attemptID) {
		this.attemptID = attemptID;
	}

	public long getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(long attributeID) {
		this.attributeID = attributeID;
	}

	public long getAuthorityID() {
		return authorityID;
	}

	public void setAuthorityID(long authorityID) {
		this.authorityID = authorityID;
	}
}
