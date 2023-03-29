package gov.nist.csd.pm.common.exceptions;
import gov.nist.csd.pm.common.constants.Constants;
public class ProhibitionNameExistsException extends PmException {
    public ProhibitionNameExistsException(String prohibitionName) {
        super(Constants.ERR_PROHIBITION_NAME_EXISTS, String.format("Prohibition with name '%s' already exists", prohibitionName));
    }
}
