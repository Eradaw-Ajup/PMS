package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
import gov.nist.csd.pm.graph.model.nodes.Node;

public class NodeIdExistsException extends PmException {
    public NodeIdExistsException(long id, Node node) {
        super(Constants.ERR_NODE_ID_EXISTS, "A node already exists with ID " + id + ": name=" + node.getName() + ", type=" + node.getType());
    }
}
