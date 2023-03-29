package gov.nist.csd.pm.pep.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import gov.nist.csd.pm.pep.response.ApiResponse;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        e.printStackTrace();
        return ApiResponse.Builder
                .error(e.hashCode(), e.getMessage())
                .build();
    }
}

