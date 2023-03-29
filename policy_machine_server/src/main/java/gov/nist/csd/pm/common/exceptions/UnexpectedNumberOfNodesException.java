package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class UnexpectedNumberOfNodesException extends PmException {
    public UnexpectedNumberOfNodesException() {
        super(Constants.ERR_UNEXPECTED_NUMBER_OF_NODES, "Expected one node but found multiple or none.");
    }
}
