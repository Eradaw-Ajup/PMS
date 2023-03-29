package gov.nist.csd.pm.pep.requests;

public class ConsentRequest {
	private long consentID;
	private long creatorID;
	private long consentOnID;
	private String consentors;
	private String prohibitions;
	private boolean isApproved;

	public long getConsentID() {
		return consentID;
	}

	public void setConsentID(long consentID) {
		this.consentID = consentID;
	}

	public long getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(long creatorID) {
		this.creatorID = creatorID;
	}

	public long getConsentOnID() {
		return consentOnID;
	}

	public void setConsentOnID(long consentOnID) {
		this.consentOnID = consentOnID;
	}

	public String getConsentors() {
		return consentors;
	}

	public void setConsentors(String consentors) {
		this.consentors = consentors;
	}

	public String getProhibitions() {
		return prohibitions;
	}

	public void setProhibitions(String prohibitions) {
		this.prohibitions = prohibitions;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
}
