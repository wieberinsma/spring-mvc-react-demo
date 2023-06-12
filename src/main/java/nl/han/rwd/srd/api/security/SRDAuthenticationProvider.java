package nl.han.rwd.srd.api.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom authentication to allow for expansion (e.g. pre-authentication checks such as MFA) of the default
 * authentication of Spring's {@link org.springframework.security.web.SecurityFilterChain}.
 * <p>
 * Authentication is here delegated to the default class, but failure will result in an exception to prevent a token
 * from being returned. User authorities are filled from
 * {@link nl.han.rwd.srd.domain.user.impl.service.UserDetailsServiceImpl}.
 * Exceptions are cleaned from session during a new authentication in {@link SRDAuthenticationSuccessHandler}.
 */
public class SRDAuthenticationProvider extends DaoAuthenticationProvider
{
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException
    {
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(result.getPrincipal(), result.getCredentials(),
                result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
