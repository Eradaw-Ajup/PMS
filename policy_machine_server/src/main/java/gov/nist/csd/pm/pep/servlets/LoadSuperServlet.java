package gov.nist.csd.pm.pep.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.ConfigurationService;

public class LoadSuperServlet extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        try {
            ConfigurationService service = new ConfigurationService();
           // service.loadSuper();

            response.sendRedirect(request.getContextPath() + "/index.jsp?display=block&result=success&message=Super+loaded+successfully");
        }
        catch (PMException e) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?display=block&result=danger&message=" + e.getMessage().replaceAll(" ", "+"));
        }
    }
}