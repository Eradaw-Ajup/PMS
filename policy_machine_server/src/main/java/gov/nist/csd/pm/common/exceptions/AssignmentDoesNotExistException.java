package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;

public class AssignmentDoesNotExistException extends PmException {
    public AssignmentDoesNotExistException(long childId, long parentId) {
        super(Constants.ERR_ASSIGNMENT_DOES_NOT_EXIST, String.format("An assignment between %d and %d does not exist.", childId, parentId));
    }
}
