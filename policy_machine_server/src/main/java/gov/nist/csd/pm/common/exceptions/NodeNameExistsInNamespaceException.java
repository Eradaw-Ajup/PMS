package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class NodeNameExistsInNamespaceException extends PmException {
    public NodeNameExistsInNamespaceException(String namespace, String nodeName) {
        super(Constants.ERR_NODE_NAME_EXISTS_IN_NAMESPACE, String.format("Node with name '%s' already exists with '%s' as its namespace property", nodeName, namespace));
    }
}
