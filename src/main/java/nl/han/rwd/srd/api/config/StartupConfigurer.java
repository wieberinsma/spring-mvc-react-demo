package nl.han.rwd.srd.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionTrackingMode;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

/**
 * Intended to be used to initialize additional web-intended specifications / configurations after the servlet context
 * is initialized but before Spring-based configurations have occurred. This example illustrates a statically shared
 * driver class that is stored _before_ Spring's {@link nl.han.rwd.srd.database.config.PersistenceConfig} requires it.
 * This could be useful in case of a requirement for alternating drivers in a DTAP environment.
 */
@WebListener
public class StartupConfigurer implements ServletContextListener
{
    private static final Logger LOG = LoggerFactory.getLogger(StartupConfigurer.class);

    private static Properties dbProperties;

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        setDbProperties();
        setSessionConfig(sce);

        ServletContextListener.super.contextInitialized(sce);
    }

    private void setDbProperties()
    {
        dbProperties = new Properties();

        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("database.properties"))
        {
            dbProperties.load(input);
            loadDbDriver();
        }
        catch (IOException ex)
        {
            LOG.error("Failed to load database properties", ex);
        }
    }

    private void loadDbDriver()
    {
        try
        {
            Class.forName(dbProperties.getProperty("driver"));
        }
        catch (ClassNotFoundException ex)
        {
            LOG.error("Failed to load database driver", ex);
        }
    }

    /**
     * Overrule use of URL to track sessions, defaulting to COOKIE only. For other configurations, see
     * {@link WebConfig}.
     */
    private void setSessionConfig(ServletContextEvent sce)
    {
        sce.getServletContext().setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
    }

    public static Properties getDbProperties()
    {
        return dbProperties;
    }
}
