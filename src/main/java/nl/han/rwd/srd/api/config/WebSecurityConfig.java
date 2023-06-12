package nl.han.rwd.srd.api.config;

import nl.han.rwd.srd.api.security.SRDAccessDeniedHandler;
import nl.han.rwd.srd.api.security.SRDAuthenticationDetailsSource;
import nl.han.rwd.srd.api.security.SRDAuthenticationFailureHandler;
import nl.han.rwd.srd.api.security.SRDAuthenticationProvider;
import nl.han.rwd.srd.api.security.SRDAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

/**
 * Global method security allows the use of {@link org.springframework.security.access.prepost.PreAuthorize} to control
 * user authorization per endpoint.
 *
 * Session management is restricted to one session per user, to prevent fiddling with user sessions for the purposes of
 * this application being a demo.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"nl.han.rwd.srd.api.security"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private SRDAuthenticationDetailsSource authDetailsSource;

    @Inject
    private SRDAccessDeniedHandler accessDeniedHandler;

    @Inject
    private SRDAuthenticationSuccessHandler successHandler;

    @Inject
    private SRDAuthenticationFailureHandler failureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider()
    {
        SRDAuthenticationProvider authProvider = new SRDAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * For cleaning up old sessions in the database using a default scheduler.
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher()
    {
        return new HttpSessionEventPublisher();
    }

    /**
     * Default configuration example. Not strictly required here as view is served statically from the same URL.
     */
    @Bean
    public CorsConfigurationSource corsConfiguration()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("/*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Stores session ID in X-AUTH-TOKEN header instead of in session cookie.
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver()
    {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    /**
     * Required to allow username / password authentication where a password is stored encrypted, see
     * {@link nl.han.rwd.srd.domain.user.impl.controller.RegistrationController}.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Note: Using 'anyRequest().authenticated()' (as per default usage) results in incorrect MIME-type. Therefore, the
     * `/private` endpoint is explicitly defined.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        http
            .authenticationProvider(authProvider())
                .formLogin()
                    .loginPage("/index")
                    .loginProcessingUrl("/login")
                        .permitAll()
                .authenticationDetailsSource(authDetailsSource)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
            .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
            .and()
                .authorizeRequests()
                    .antMatchers("/index")
                        .permitAll()
                    .antMatchers("/private")
                        .authenticated()
            .and()
                .sessionManagement()
                    .maximumSessions(1)
                    .and()
                        .sessionFixation()
                            .migrateSession()
            .and()
                .cors()
                    .configurationSource(corsConfiguration())
            .and()
                .csrf()
                    .disable()
                .requestCache()
                    .disable();
    }
}

