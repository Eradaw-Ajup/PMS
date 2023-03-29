package gov.nist.csd.pm.pep.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import gov.nist.csd.pm.pep.response.ApiResponse;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException e) {
        e.printStackTrace();
        return ApiResponse.Builder
                .error(e.getResponse().getStatus(), e.getMessage())
                .build();
    }
}
