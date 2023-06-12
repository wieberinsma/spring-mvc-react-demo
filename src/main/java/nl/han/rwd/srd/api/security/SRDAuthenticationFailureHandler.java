package nl.han.rwd.srd.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SRDAuthenticationFailureHandler implements AuthenticationFailureHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(SRDAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException ex)
    {
        LOG.warn("Failed login by user: " + request.getParameter("username"));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
