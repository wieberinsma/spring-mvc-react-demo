package nl.han.rwd.srd;

import nl.han.rwd.srd.api.config.WebConfig;
import nl.han.rwd.srd.database.config.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"nl.han.rwd.srd.domain"})
@Import({PersistenceConfig.class, WebConfig.class})
public class RootAppConfig
{
}
