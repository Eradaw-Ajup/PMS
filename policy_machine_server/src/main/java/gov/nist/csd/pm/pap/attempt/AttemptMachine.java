package gov.nist.csd.pm.pap.attempt;

import gov.nist.csd.pm.common.exceptions.DBException;
import gov.nist.csd.pm.pap.model.Attempt;

public interface AttemptMachine {

	Attempt createAttempt(String attemptType, String attemptStatus, String createdBy) throws DBException;

	void updateAttempt(long attemptID, String attemptStatus, String createdBy) throws DBException;

	Attempt getAttempt(long attemptID) throws DBException;
}
