package nl.han.rwd.srd.domain.user.impl.service;

import nl.han.rwd.srd.database.model.UserEntity;
import nl.han.rwd.srd.domain.user.spec.repository.UserRepository;
import nl.han.rwd.srd.domain.user.spec.service.SecurityService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Spring-default username/password authentication requires a custom-defined user details implementation to be able
 * to provide your own authorisation (authorities of type String) and a Spring
 * {@link org.springframework.security.core.userdetails.User} to persist authorities between requests.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Inject
    private UserRepository userRepository;

    @Inject
    private SecurityService securityService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null)
        {
            throw new BadCredentialsException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(userEntity.getUsername(),
                userEntity.getPassword(),
                getGrantedAuthorities(userEntity));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(UserEntity userEntity)
    {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String authority : securityService.getUserAuthorities(securityService.mapToAuthUser(userEntity)))
        {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        return grantedAuthorities;
    }

}
