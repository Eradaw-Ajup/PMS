package gov.nist.csd.pm.pep.requests;

import java.util.HashMap;
import java.util.Map;

public class CreateAttemptRequest {
	private String attempt;
	private Map<String, String> payload;

	public String getAttempt() {
		return attempt;
	}

	public void setAttempt(String attempt) {
		this.attempt = attempt;
	}

	public Map<String, String> getPayload() {
		if(null == payload)
			payload = new HashMap<>();
		return payload;
	}

	public void setPayload(Map<String, String> payload) {
		this.payload = payload;
	}
}
