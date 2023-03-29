package gov.nist.csd.pm.pep.requests;

import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.ptl.model.Translation;

public class AccessTokenRequest {
	private String type;
	private long attributeID;
	private long authorityID;
	private String secret;
	private String token;
	private String value;
	private Translation policy;
	private MNode node;
	private String policyType;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Translation getPolicy() {
		return policy;
	}

	public void setPolicy(Translation policy) {
		this.policy = policy;
	}

	public MNode getNode() {
		return node;
	}

	public void setNode(MNode node) {
		this.node = node;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
}
