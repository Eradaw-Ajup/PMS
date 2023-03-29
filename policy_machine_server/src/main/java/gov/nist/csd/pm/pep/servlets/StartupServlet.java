package gov.nist.csd.pm.pep.servlets;

import javax.servlet.http.HttpServlet;

import gov.nist.csd.pm.common.util.LOG;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;

public class StartupServlet extends HttpServlet {

	/**
	 * NCSC POLICY SYSTEM INIT
	 */
	private static final long serialVersionUID = 7976878904766289208L;

	@Override
	public void init() {
		try {
			if(null == LOG.getInstance())
				throw new PMException("Failed to Init Logger");
			PAP.getPAP();
		} catch (PMException e) {
			e.printStackTrace();
		}
	}
}
