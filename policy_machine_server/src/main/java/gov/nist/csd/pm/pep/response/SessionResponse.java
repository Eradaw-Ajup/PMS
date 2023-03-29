package gov.nist.csd.pm.pep.response;

public class SessionResponse
{
	private long nodeID;
	private String sessionID;

	public SessionResponse() { }
	public SessionResponse(String sessionID, long nodeID)
	{
		this.sessionID = sessionID;
		this.nodeID = nodeID;
	}

	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public long getNodeID() {
		return nodeID;
	}
	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}
}
