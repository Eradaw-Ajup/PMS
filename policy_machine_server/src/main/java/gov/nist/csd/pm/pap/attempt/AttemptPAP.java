package gov.nist.csd.pm.pap.attempt;

import gov.nist.csd.pm.common.exceptions.DBException;
import gov.nist.csd.pm.pap.model.Attempt;

public class AttemptPAP implements AttemptMachine {

	private AttemptMachine db;

	public AttemptPAP(AttemptMachine db) {
		this.db = db;
	}

	public AttemptMachine getAttemptMachine() {
		return db;
	}

	@Override
	public Attempt createAttempt(String attemptType, String attemptStatus, String createdBy) throws DBException {
		return db.createAttempt(attemptType, attemptStatus, createdBy);
	}

	@Override
	public void updateAttempt(long attemptID, String attemptStatus, String createdBy) throws DBException {
		db.updateAttempt(attemptID, attemptStatus, createdBy);
	}

	@Override
	public Attempt getAttempt(long attemptID) throws DBException {
		return db.getAttempt(attemptID);
	}
}
