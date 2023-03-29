package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class NullTypeException extends PmException {
    public NullTypeException() {
        super(Constants.ERR_NULL_TYPE, "The server received a null type");
    }
}
