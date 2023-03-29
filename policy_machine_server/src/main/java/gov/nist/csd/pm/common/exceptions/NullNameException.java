package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class NullNameException extends PmException {
    public NullNameException() {
        super(Constants.ERR_NULL_NAME, "The server received a null name");
    }
}
