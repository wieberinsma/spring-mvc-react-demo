package nl.han.rwd.srd;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

/**
 * {@link WebApplicationInitializer} | Bootstraps a Spring MVC web-application when Application-, ServletContext and
 * Servlet are properly configured. Context is configured using pre-determined @Configuration classes in a
 * hierarchical way (see @Import).
 *
 * Both SpringSecurityFilterChain and it's managed Spring filters are needed as Servlet filters. Note that the usage of
 * the term 'filter' is ambigiuous here.
 */
public class SpringMvcApplication implements WebApplicationInitializer
{
    @Override
    public void onStartup(final ServletContext servletContext)
    {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootAppConfig.class);

        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
                .addMappingForUrlPatterns(null, true, "/*");
        servletContext.addFilter("springSessionRepositoryFilter", DelegatingFilterProxy.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        ServletRegistration.Dynamic appServlet =
                servletContext.addServlet("dispatcher", new DispatcherServlet(new GenericWebApplicationContext()));
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
    }
}
