package gov.nist.csd.pm.pep.response;

import java.util.List;

public class PartialToken {
	private List<AccessToken> tokens;
	private List<Share> x_values;

	public List<AccessToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<AccessToken> tokens) {
		this.tokens = tokens;
	}

	public List<Share> getX_values() {
		return x_values;
	}

	public void setX_values(List<Share> x_values) {
		this.x_values = x_values;
	}
}
