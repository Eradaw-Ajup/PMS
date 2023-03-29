package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class NoUserParameterException extends PmException {
    public NoUserParameterException() {
        super(Constants.ERR_NO_USER_PARAMETER, "No user or user attribute was specified in the parameters, but one is required.");
    }
}
