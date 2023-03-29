package gov.nist.csd.pm.pep.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import gov.nist.csd.pm.common.exceptions.Errors;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pep.response.ApiResponse;

@Provider
public class PMExceptionMapper implements ExceptionMapper<PMException> {
    @Override
    public Response toResponse(PMException e) {
        e.printStackTrace();

        Errors err = Errors.toException(e);

        return ApiResponse.Builder
                .error(err, e)
                .build();
    }
}
