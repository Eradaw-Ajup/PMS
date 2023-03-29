package gov.nist.csd.pm.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LOG {
	private static Logger mLogger;

	private LOG() {
	}

	public static Logger getInstance() {
		if (null == mLogger)
			mLogger = LogManager.getLogger("POLICY_MACHINE");
		return mLogger;
	}

	public static void info(String msg) {
		mLogger.info(msg);
	}

	public static void warn(String msg) {
		mLogger.warn(msg);
	}

	public static void error(String msg) {
		mLogger.error(msg);
	}

	public static void debug(String msg) {
		mLogger.debug(msg);
	}
}
