package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class NodeNotFoundException extends PmException {
    public NodeNotFoundException(long id) {
        super(Constants.ERR_NODE_NOT_FOUND, String.format("Node with id %d could not be found", id));
    }

    public NodeNotFoundException(String nodeName){
        super(Constants.ERR_NODE_NOT_FOUND, String.format("Node with name %s could not be found", nodeName));
    }
}
