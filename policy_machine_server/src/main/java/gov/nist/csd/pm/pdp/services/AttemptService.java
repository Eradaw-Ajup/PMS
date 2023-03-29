package gov.nist.csd.pm.pdp.services;

import java.util.HashMap;
import java.util.Map;

import gov.nist.csd.pm.common.constants.AttemptStatus;
import gov.nist.csd.pm.common.constants.AttemptType;
import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.PolicyType;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.model.Attempt;

public class AttemptService extends Service {

	public AttemptService(String owner) throws PMException {
		super(owner);
	}

	public Map<String, Object> createAttempt(Map<String, String> request) throws PMException {
		if (null == AttemptType.toAttemptType(request.get("attempt")))
			throw new IllegalArgumentException(String.format(MessageHelper.ATTEMPT_NOT_FOUND, request.get("attempt")));
		else if (null == PolicyType.toPolicyType(request.get("policyType")))
			throw new IllegalArgumentException(
					String.format(MessageHelper.POLICY_TYPE_NOT_FOUND, request.get("policyType")));
		Map<String, Object> map = new HashMap<>();

		Attempt attempt = getAttemptPAP().createAttempt(request.get("attempt"), AttemptStatus.ACTIVE.name(), getOwner());
		map.put("attempt", attempt);
		switch (AttemptType.toAttemptType(request.get("attempt"))) {
		case CREATE_POLICY:
			long policyID = getPolicyPAP().createPolicy(PolicyType.toPolicyType(request.get("policyType")),
					PolicyStatus.IN_ACTIVE.name(), getOwner(), 0, 0);
			long relID = getPolicyPAP().createPolicyAttemptReln(policyID, attempt.getAttemptID());

			if (0 == relID) {
				// remove the entry
				throw new PMException(MessageHelper.ATTEMPT_FAILURE);
			}

			map.put("policyID", policyID);
			break;
		case ADD_ATTRIBUTE:
			break;
		case REVOKE_ATTRIBUTE:
			break;
		case INCREASE_THRESHOLD:
			break;
		case SECURE_ASSET:
			break;
		case ACCESS_ASSET:
			break;
		case GET_ACCESS_TOKEN:
			break;
		default:
			break;
		}
		return map;
	}

	public Attempt getAttempt(long attemptID) throws PMException {
		if (0 == attemptID) {
			throw new IllegalArgumentException(String.format(MessageHelper.ATTEMPT_WITH_ID_NOT_FOUND, attemptID));
		}
		return getAttemptPAP().getAttempt(attemptID);
	}

	public void updateAttempt(long attemptID, String attemptStatus) throws PMException {
		if (0 == attemptID) {
			throw new IllegalArgumentException(String.format(MessageHelper.ATTEMPT_WITH_ID_NOT_FOUND, attemptID));
		} else if (null == AttemptStatus.toAttemptStatus(attemptStatus)) {
			throw new IllegalArgumentException(MessageHelper.ATTEMPT_STATUS_DOES_NOT_EXISTS);
		}
		getAttemptPAP().updateAttempt(attemptID, attemptStatus, getOwner());
	}
}
