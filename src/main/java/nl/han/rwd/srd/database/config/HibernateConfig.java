package nl.han.rwd.srd.database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * {@link EnableTransactionManagement} | For Transactional functionality of database Entities
 * {@link javax.persistence.EntityManager} | To allow fine-grained control over entity management of Hibernate (ORM)
 * {@link JpaTransactionManager} | Required implementation for JPA (javax.persistence) under Spring, supports JPA and
 * direct JDBC datasource access. Datasource must be same as entityManagerFactory (which is default). Here declared
 * for visibility
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig
{
    @Inject
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("nl.han.rwd.srd.database.model");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
        return entityManagerFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager()
    {
        EntityManagerFactory entityManagerFactory = entityManagerFactory().getObject();

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
