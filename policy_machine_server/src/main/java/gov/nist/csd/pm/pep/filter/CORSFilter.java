package gov.nist.csd.pm.pep.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

   @Override
   public void filter(final ContainerRequestContext requestContext,
                      final ContainerResponseContext responseContext) throws IOException {
	   requestContext.getHeaders();
       responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
       responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, Content-Hash, _HttpMethod, X-has-more, X-limit, X-offset, X-query");
       responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
       responseContext.getHeaders().add("Cache-Control", "no-store, max-age=0");
       responseContext.getHeaders().add("X-Content-Type-Options", "nosniff");
       responseContext.getHeaders().add("X-XSS-Protection", "0");
       responseContext.getHeaders().add("Pragma", "no-cache");
   }
}