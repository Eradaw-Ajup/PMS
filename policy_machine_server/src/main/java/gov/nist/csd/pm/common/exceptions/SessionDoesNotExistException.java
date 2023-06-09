package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class SessionDoesNotExistException extends PmException{
    public SessionDoesNotExistException(String sessionId){
        super(Constants.ERR_SESSION_DOES_NOT_EXIST, String.format("Session with id %s does not exist", sessionId));
    }
}
