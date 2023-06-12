package nl.han.rwd.srd.api.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class SRDAuthenticationDetails extends WebAuthenticationDetails
{
    private final String username;

    public SRDAuthenticationDetails(HttpServletRequest request)
    {
        super(request);
        this.username = request.getParameter("username");
    }

    public String getUsername()
    {
        return username;
    }
}
