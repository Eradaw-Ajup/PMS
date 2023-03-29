package gov.nist.csd.pm.ptl.model;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.exceptions.PMGraphException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pap.graph.GraphPAP;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MNode;

class TID {
	private long mTID;

	public TID() {
	}

	public TID(long tID) {
		this.mTID = tID;
	}

	public long getID() {
		return mTID;
	}

	public void setID(long tID) {
		this.mTID = tID;
	}

	public long increment() {
		return ++mTID;
	}
}

public class Translation {
	private List<TNode> nodes;
	private List<Assignment> assignments;

	public Translation() {
		this.nodes = new ArrayList<>();
		this.assignments = new ArrayList<>();
	}

	public List<TNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<TNode> nodes) {
		this.nodes = nodes;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public String convert(Set<MNode> nodes, Set<MAssignment> assignments, GraphPAP graphPAP)
			throws PMException, IOException, Exception {
		if (null == nodes || nodes.isEmpty()) {
			throw new IllegalArgumentException("Nodes cannot be null or empty during translation");
		} else if (null == assignments || nodes.isEmpty()) {
			throw new IllegalArgumentException("Assignments cannot be null or empty during translation");
		} else if (null == graphPAP) {
			throw new IllegalArgumentException("GraphPAP cannot be null during translation");
		}

		TNode tNode = null;

		for (MNode node : nodes) {
			if (node.getType() == NodeType.O) {
				System.out.println(
						"nodeID: " + node.getID() + "\tname: " + node.getName() + "\type: " + node.getType().name());
				Set<Long> parents = graphPAP.getParents(node.getID());
				if (null == parents || parents.isEmpty())
					tNode = new TNode(1, TNodeType.ROOT, 0, 0, null, node.getID());
				else
					tNode = new TNode(1, TNodeType.ROOT, parents.size(), parents.size(), null, node.getID());
				this.nodes.add(tNode);
			} else {
				Set<Long> parents = graphPAP.getParents(node.getID());
				if (null != parents && !parents.isEmpty()) {
					System.out.println("nodeID: " + node.getID() + "\tname: " + node.getName() + "\type: "
							+ node.getType().name());
					this.nodes.add(new TNode(node.getID(), TNodeType.NON_LEAF, parents.size(), parents.size(), null,
							node.getID()));
				} else {
					System.out.println("nodeID: " + node.getID() + "\tname: " + node.getName() + "\type: "
							+ node.getType().name());
					this.nodes.add(
							new TNode(node.getID(), TNodeType.LEAF, 1, 1, hashSHA256(node.getName()), node.getID()));
				}
			}
		}

		for (MAssignment assignment : assignments) {
			this.assignments.add(new Assignment(assignment.getTargetID(), assignment.getSourceID()));
		}

		for(Assignment assignment: this.assignments) {
			if(assignment.getSourceID() == tNode.getUniqueID())
				assignment.setSourceID(tNode.getNodeID());
			else if(assignment.getTargetID() == tNode.getUniqueID())
				assignment.setTargetID(tNode.getNodeID());
		}

		return new ObjectMapper().writeValueAsString(this);
	}

	public void hasNodes(long nodeID, GraphPAP graphPAP, TID tID, long parentID) throws PMException, Exception {
		if (0 == nodeID)
			return;

		MNode node = graphPAP.getNode(nodeID);
		if (null == node)
			throw new PMGraphException(String.format(MessageHelper.NODE_DOES_NOT_EXIST, nodeID));

		Set<Long> childerns = graphPAP.getChildren(nodeID);
		if (null == childerns || childerns.isEmpty())
			return;

		for (long id : childerns) {
			MNode child = graphPAP.getNode(id);
			if (child.getType() == NodeType.OA) {
				Set<Long> childs = graphPAP.getChildren(child.getID());
				if (!childs.isEmpty())
					hasNodes(child.getID(), graphPAP, tID, id);
				System.out.println("name: " + child.getName() + "\tid: " + tID);

				// If we are here then it means we have reached to the root of the tree
				this.nodes.add(
						new TNode(tID.increment(), TNodeType.LEAF, 1, 1, hashSHA256(child.getName()), child.getID()));
				this.assignments.add(new Assignment(tID.getID(), parentID));
			}
		}

	}

	private static String hashSHA256(String hashOf) throws Exception {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(hashOf.getBytes(StandardCharsets.UTF_8));
			return toHexString(encodedhash);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private static String toHexString(byte[] hash) {
		// Convert byte array into signum representation
		BigInteger number = new BigInteger(1, hash);

		// Convert message digest into hex value
		StringBuilder hexString = new StringBuilder(number.toString(16));

		// Pad with leading zeros
		while (hexString.length() < 64) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}
}
