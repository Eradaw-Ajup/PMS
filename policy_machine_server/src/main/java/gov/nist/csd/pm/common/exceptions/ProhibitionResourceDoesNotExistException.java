package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;
public class ProhibitionResourceDoesNotExistException extends PmException {
    public ProhibitionResourceDoesNotExistException(String prohibtionName, long resourceId) {
        super(Constants.ERR_PROHIBITION_RESOURCE_DOES_NOT_EXIST, String.format("Prohibition with name '%s' does not have a resource with id %d", prohibtionName, resourceId));
    }
}



