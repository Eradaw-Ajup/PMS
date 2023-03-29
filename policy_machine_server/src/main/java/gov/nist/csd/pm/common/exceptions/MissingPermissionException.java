package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;

public class MissingPermissionException extends PmException {
    public MissingPermissionException(String message) {
        super(Constants.ERR_MISSING_PERMISSIONS, message);
    }
}
