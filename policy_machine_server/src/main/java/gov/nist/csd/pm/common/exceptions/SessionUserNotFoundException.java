package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class SessionUserNotFoundException extends PmException {
    public SessionUserNotFoundException(String session) {
        super(Constants.ERR_SESSION_USER_NOT_FOUND, "Could not find a user for session " + session);
    }
}

