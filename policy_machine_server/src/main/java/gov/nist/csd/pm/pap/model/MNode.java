package gov.nist.csd.pm.pap.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table (name = "node")
public class MNode extends Node {
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Map<Long, String> nodeType = new HashMap<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
	private long IDD;
	
	@Column(name = "name")
	private String NAME;
	
	@Column(name = "node_type_id")
	private long TYPE;
	
	@Column(name = "node_property")
	private String PROPERTIES;
	
	@Column(name = "owner")
	private String owner;
	
	@Column(name = "policy_id")
	private long policyID;
	
	@Column(name = "node_status")
	private String storeType;
	
	@Column(name = "is_node_active")
	private boolean isNodeActive;
	
	@Column(name = "attribute_id")
	private long attributeID;
	
	@Column(name = "authority_id")
	private long authorityID;

	public MNode() {
	}

	public MNode(long id, String name, NodeType type, Map<String, String> properties, boolean isNodeActive,
			String nodeStoreType, long policyID, String owner, long attributeID, long authorityID) {
		super(id, name, type, properties);
		this.IDD = id;
        this.NAME = name;
//        this.TYPE = type.name();
        for (Entry<Long, String> set : nodeType.entrySet()) {
			if (set.getValue() == type.name())
				this.TYPE = set.getKey();
		}
        try {
			this.PROPERTIES = objectMapper.writeValueAsString(properties == null ? new HashMap<>() : properties);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isNodeActive = isNodeActive;
		this.storeType = nodeStoreType;
		this.policyID = policyID;
		this.owner = owner;
		this.attributeID = attributeID;
		this.authorityID = authorityID;
	}
	public MNode(long id, String name, NodeType type, Map<String, String> properties, boolean isNodeActive,
			String nodeStoreType, long policyID, String owner, long attributeID, long authorityID, long typeID) {
		super(id, name, type, properties);
		this.IDD = id;
        this.NAME = name;
        this.TYPE = typeID;
        
        try {
			this.PROPERTIES = objectMapper.writeValueAsString(properties == null ? new HashMap<>() : properties);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isNodeActive = isNodeActive;
		this.storeType = nodeStoreType;
		this.policyID = policyID;
		this.owner = owner;
		this.attributeID = attributeID;
		this.authorityID = authorityID;
	}
	public MNode(String name, NodeType type, Map<String, String> properties, boolean isNodeActive,
			String nodeStoreType, long policyID, String owner, long attributeID, long authorityID, long typeID) {
		super(name, type, properties);
//		this.IDD = id;
        this.NAME = name;
        this.TYPE = typeID;
        
        try {
			this.PROPERTIES = objectMapper.writeValueAsString(properties == null ? new HashMap<>() : properties);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isNodeActive = isNodeActive;
		this.storeType = nodeStoreType;
		this.policyID = policyID;
		this.owner = owner;
		this.attributeID = attributeID;
		this.authorityID = authorityID;
	}
	public MNode( String name, NodeType type, Map<String, String> properties, boolean isNodeActive,
			String nodeStoreType, long policyID, String owner, long attributeID, long authorityID) {
		super( name, type, properties);
		this.NAME = name;
		for (Entry<Long, String> set : nodeType.entrySet()) {
			if (set.getValue().equals(type.name()))
				this.TYPE = set.getKey();
		}
        try {
			this.PROPERTIES = objectMapper.writeValueAsString(properties == null ? new HashMap<>() : properties);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isNodeActive = isNodeActive;
		this.storeType = nodeStoreType;
		this.policyID = policyID;
		this.owner = owner;
		this.attributeID = attributeID;
		this.authorityID = authorityID;
	}

	public MNode(MNode node) {
		super(node.getID(), node.getName(), node.getType(), node.getProperties());
		
		this.IDD = getIDD();
		this.NAME = getNAME();
        this.TYPE = getTYPE();
        this.PROPERTIES = getPROPERTIES();
        
		this.owner = node.getOwner();
		this.policyID = node.getPolicyID();
		this.storeType = node.getStoreType();
		this.isNodeActive = node.isNodeActive();
		this.attributeID = node.getAttributeID();
		this.authorityID = node.getAuthorityID();
	}

	public boolean isNodeActive() {
		return isNodeActive;
	}

	public void setNodeActive(boolean isNodeActive) {
		this.isNodeActive = isNodeActive;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public long getPolicyID() {
		return policyID;
	}

	public void setPolicyID(long policyID) {
		this.policyID = policyID;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof MNode) {
			MNode n = (MNode) o;
			return getID() == n.getID();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getID());
	}

	@Override
	public String toString() {
		String data = "node_id : " + getIDD() + ", " + 
		"node_type_id : " + getTYPE() + ", " +
		"policy_id : " + getPolicyID() + ", " + 
		"name : " + getName() + ", " + 
		"node_property : " + getPROPERTIES() + ", " + 
		"is_node_active : " + isNodeActive() + ", " + 
		"node_status : " + getStoreType() + ", " + 
		"authority_id : " + getAuthorityID() + ", " + 
		"attribute_id : " + getAttributeID() + ", " +
		"owner : " + getOwner() + " "
				;
		return data;
//		return getName() + ":" + getType().name() + ":" + getID() + ":" + getProperties();
	}

	public long getIDD() {
		return IDD;
	}

	public void setIDD(long iDD) {
		IDD = iDD;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public long getTYPE() {
		return TYPE;
	}

	public void setTYPE(long tYPE) {
		TYPE = tYPE;
	}

	public String getPROPERTIES() {
		return PROPERTIES;
	}

	public void setPROPERTIES(String pROPERTIES) {
		PROPERTIES = pROPERTIES;
	}
}
