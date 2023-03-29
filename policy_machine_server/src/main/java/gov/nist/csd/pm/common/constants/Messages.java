package gov.nist.csd.pm.common.constants;

public enum Messages {
	NOT_NOT_FOUND_MSG("node with ID %d could not be found");

	private String label;

	Messages(String label) {
		this.label = label;
	}
}
