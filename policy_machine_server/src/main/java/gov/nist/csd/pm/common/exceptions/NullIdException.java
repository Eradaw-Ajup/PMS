package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class NullIdException extends PmException {
    public NullIdException() {
        super(Constants.ERR_NULL_ID, "The server received a null id");
    }
}