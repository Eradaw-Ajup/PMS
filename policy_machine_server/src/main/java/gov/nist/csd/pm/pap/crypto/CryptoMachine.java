package gov.nist.csd.pm.pap.crypto;

import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.exceptions.DBException;

public interface CryptoMachine {
	//CRYPTO JSON
	void updateJSON(String json, long policyID, String owner, String status) throws DBException;

	String getJSON(long policyID, String owner, PolicyStatus status) throws DBException;
}
