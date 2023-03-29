package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;

public class ConfigurationException extends PmException {
    public ConfigurationException(String message) {
        super(Constants.ERR_CONFIGURATION, message);
    }
}
