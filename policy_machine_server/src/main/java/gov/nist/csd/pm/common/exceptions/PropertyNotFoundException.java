package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class PropertyNotFoundException extends PmException {
    public PropertyNotFoundException(long nodeId, String propKey) {
        super(Constants.ERR_PROPERTY_NOT_FOUND, String.format("Node with ID = %d does not have a property with key '%s'", nodeId, propKey));
    }
}

