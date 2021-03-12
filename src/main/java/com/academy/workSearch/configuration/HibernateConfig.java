package com.academy.workSearch.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class HibernateConfig {

    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan("com.academy.workSearch.model");
        return sessionFactoryBean;
    }


    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(SHOW_SQL, Objects.requireNonNull(environment.getProperty("hibernate.show_sql")));
        properties.put(HBM2DDL_AUTO, Objects.requireNonNull(environment.getProperty("hibernate.hbm2ddl.auto")));
        properties.put(DIALECT, Objects.requireNonNull(environment.getProperty("hibernate.dialect")));
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("postgresql.driver")));
        dataSource.setUrl(Objects.requireNonNull(environment.getProperty("postgresql.url")));
        dataSource.setUsername(Objects.requireNonNull(environment.getProperty("postgresql.user")));
        dataSource.setPassword(Objects.requireNonNull(environment.getProperty("postgresql.password")));
        return dataSource;
    }
}
