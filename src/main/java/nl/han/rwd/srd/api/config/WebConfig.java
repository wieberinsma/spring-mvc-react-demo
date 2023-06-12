package nl.han.rwd.srd.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.FlushMode;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Configuration for the web (domain) resources and services.
 */
@Configuration
@EnableWebMvc
@Import(WebSecurityConfig.class)
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3600, flushMode = FlushMode.IMMEDIATE)
@ComponentScan(basePackages = {"nl.han.rwd.srd.api.exception", "nl.han.rwd.srd.domain.user.impl.controller"})
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**");
    }

    /**
     * Resource handlers and locations are required for mapping any of the resources referenced in Java. To be able to
     * directly reference (i.e. from source) files from a certain type, they must be included here. For that reason CSS
     * files are included to allow both referencing from HTML and bundling via Webpack.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/**.html", "/**.css", "/**.js", "/**.map")
                .addResourceLocations("classpath:/public/html/", "classpath:/static/css/", "classpath:/static/built/");
    }

    @Bean
    public ViewResolver internalResourceViewResolver()
    {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setSuffix(".html");
        return bean;
    }

    /**
     * Required to work around an issue with Spring's Session cookie config not allowed to be retrieved from servlet
     * context in Tomcat. See
     * {@link org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration}.
     */
    @Bean
    public CookieSerializer cookieSerializer()
    {
        DefaultCookieSerializer cs = new DefaultCookieSerializer();

        cs.setCookieName("JSESSIONID");
        cs.setDomainName("");
        cs.setCookiePath("/");
        cs.setUseHttpOnlyCookie(true);
        cs.setSameSite("None");

        return cs;
    }

    /**
     * Required for Hibernate validation exception response mapping.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
