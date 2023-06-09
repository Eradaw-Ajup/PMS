package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;

public class AssociationExistsException extends PmException {
    public AssociationExistsException(long uaId, long targetId) {
        super(Constants.ERR_ASSOCIATION_EXISTS, String.format("An association between %d and %d already exists.", uaId, targetId));
    }
}
