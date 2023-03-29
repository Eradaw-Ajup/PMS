package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;

public class InvalidPropertyException extends PmException {
    public InvalidPropertyException(String key, String value){
        super(Constants.ERR_INVALID_PROPERTY, "The property '" + key + "=" + value + "' is invalid");
    }

    public InvalidPropertyException(String message){
        super(Constants.ERR_INVALID_PROPERTY, message);
    }
}