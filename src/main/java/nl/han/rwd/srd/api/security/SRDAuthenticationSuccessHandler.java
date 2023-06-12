package nl.han.rwd.srd.api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.han.rwd.srd.api.model.LoginResponse;
import nl.han.rwd.srd.domain.user.spec.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

/**
 * Redirects the succeeded authentication to the proper URL using a predefined JSON to be used by React to delegate it
 * properly. Note that no actual redirect occurs, as React simply manipulates a single-page DOM.
 */
@Component
public class SRDAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(SRDAuthenticationSuccessHandler.class);

    @Inject
    private SecurityService securityService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException
    {
        handle(request, response);
    }

    /**
     * Initialize a new session with default authentication attributes. Cleans up previous log failures to prevent
     * authentication implementation interfering with old data.
     *
     * Response is mapped to a JSON String for (de)serialisation compatibility in the fetch API.
     */
    private void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String authUser = securityService.getAuthUsername();

        session.setMaxInactiveInterval(60 * 30);
        securityService.updateAuthenticationAttributes(session, authUser);
        securityService.clearAuthenticationAttributes(session,
                Collections.singletonList(WebAttributes.AUTHENTICATION_EXCEPTION));

        if (!response.isCommitted() && StringUtils.hasLength(sessionId))
        {
            response.setStatus(HttpServletResponse.SC_OK);
            String jsonResponse = parseLoginResponse(authUser);
            response.getWriter().write(jsonResponse);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.error("Response already committed or session not properly created, could not return.");
        }
    }

    private String parseLoginResponse(String authUser)
    {
        String jsonResponse = "";
        LoginResponse loginResponse = mapToLoginResponse(authUser);

        final ObjectMapper jacksonMapper = new ObjectMapper();
        try
        {
            jsonResponse = jacksonMapper.writeValueAsString(loginResponse);
        }
        catch (JsonProcessingException e)
        {
            LOG.error("Failed to parse login response as String.");
        }

        return jsonResponse;
    }

    /**
     * Session token and ID are returned in response headers and should be accessed only from there.
     */
    private LoginResponse mapToLoginResponse(String authUser)
    {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(authUser);
        loginResponse.setAction("LOGIN");
        loginResponse.setRedirectUrl("private");
        return loginResponse;
    }
}
