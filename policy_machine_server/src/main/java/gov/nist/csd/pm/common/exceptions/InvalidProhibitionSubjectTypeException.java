package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.common.constants.Constants;

public class InvalidProhibitionSubjectTypeException extends PmException {
    public InvalidProhibitionSubjectTypeException(String type){
        super(Constants.ERR_INVALID_PROHIBITION_SUBJECTTYPE, "Provided ProhibitionSubjectType '" + type + "' is not one of (UA, U, P)");
    }
}