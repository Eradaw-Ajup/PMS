package gov.nist.csd.pm.pap.crypto;

import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.exceptions.DBException;

public class CryptoPAP implements CryptoMachine {
	private final CryptoMachine db;

	public CryptoPAP(CryptoMachine db) {
		this.db = db;
	}

	public CryptoMachine getCryptoPAP() {
		return db;
	}

	@Override
	public void updateJSON(String json, long policyID, String owner, String status) throws DBException {
		db.updateJSON(json, policyID, owner, status);
	}

	@Override
	public String getJSON(long policyID, String owner, PolicyStatus status) throws DBException {
		return db.getJSON(policyID, owner, status);
	}
}
