package gov.nist.csd.pm.ptl.model;

public class TNode {
	/*
	 * Use: Entire data structures is based upon it Example: 1 2 3 4 5
	 *
	 * Indexing of nodes SHOULD be in this format 1, 2, 4, 3, 5
	 */
	private long nodeID;
	/*
	 * Use: On the basis of type the access policy data structure is built
	 */
	private TNodeType type;
	/*
	 * It is the count of child nodes purpose: policy instrumentation (generation of
	 * access tokens)
	 */
	private long users;
	/*
	 * How many access tokens are needed to generate Association count at business
	 * layer
	 */
	private long threshold;
	/*
	 * It is just a name of node
	 */
	private String attribute;

	private long uniqueID;

	private String nodeType;

	public TNode() {
	}

	public TNode(long nodeID, TNodeType type, long users, long threshold, String attribute, long uniqueID) {
		this.nodeID = nodeID;
		this.type = type;
		this.users = users;
		this.threshold = threshold;
		this.attribute = attribute;
		this.uniqueID = uniqueID;
	}

	public long getNodeID() {
		return nodeID;
	}

	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}

	public TNodeType getType() {
		return type;
	}

	public void setType(TNodeType type) {
		this.type = type;
	}

	public long getUsers() {
		return users;
	}

	public void setUsers(long users) {
		this.users = users;
	}

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
}
