package gov.nist.csd.pm.pdp.services;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import gov.nist.csd.pm.exceptions.PMException;

@Disabled
@TestInstance(Lifecycle.PER_CLASS)
public class TestAnalyticsService {


	@Test
	public void happyPath() throws PMException {
		Set<String> operation = new HashSet<>();
		operation.add("has access");
		AnalyticsService analyticsService = new AnalyticsService(23, "b50eb672-5e50-4f7f-8354-c7ba3423d41b");
		Set<String> per = analyticsService.getPermissions(22);
		assertEquals(per, operation);
	}

	@Test
	public void unhappyPath() throws PMException {
		AnalyticsService analyticsService = new AnalyticsService(24, "b50eb672-5e50-4f7f-8354-c7ba3423d41b");
		Set<String> per = analyticsService.getPermissions(22);
		assertEquals(per, null);
	}

}
