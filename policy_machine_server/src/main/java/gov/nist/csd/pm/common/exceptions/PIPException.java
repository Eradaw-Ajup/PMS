package gov.nist.csd.pm.common.exceptions;

import gov.nist.csd.pm.exceptions.PMException;

public class PIPException extends PMException {
    private String source;

    public PIPException(String msg) {
        super(msg);
    }

    public PIPException(String source, String s) {
        this(String.format("%s, %s", source, s));
        this.source = source;
    }

    public String getSource() {
        return source;
    }

}
