package gov.nist.csd.pm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import gov.nist.csd.pm.pip.db.DatabaseContext;

public class TestUtils {

    private static DatabaseContext dbCtx;

    public static DatabaseContext getDatabaseContext() throws IOException {
        if(dbCtx == null) {
            InputStream is = TestUtils.class.getClassLoader().getResourceAsStream("db.config");
            Properties props = new Properties();
            props.load(is);

            dbCtx = new DatabaseContext(
                    props.getProperty("host"),
                    Integer.valueOf(props.getProperty("port")),
                    props.getProperty("username"),
                    props.getProperty("password"),
                    props.getProperty("schema"),
                    props.getProperty("database")
            );
        }

        return dbCtx;
    }

    public static void main(String[] args) throws IOException {
		if(null != getDatabaseContext())
			System.out.println("ITs working");
	}
}
