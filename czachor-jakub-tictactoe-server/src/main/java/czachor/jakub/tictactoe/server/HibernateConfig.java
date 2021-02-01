package czachor.jakub.tictactoe.server;

import generic.online.game.server.gogs.PropertiesLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = Application.basePackage)
public class HibernateConfig extends HibernateJpaAutoConfiguration {
    private final PropertiesLoader propertiesLoader;

    @PostConstruct
    public void init() {
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan(Application.basePackage);
        factory.setJpaProperties(hibernateProperties());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factory;
    }

    @Bean
    public TransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    private DataSource dataSource() {
        Map<String, Object> p = propertiesLoader.getProperties("/application.properties", "jdbc.");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(p.get("driver.class.name").toString());
        dataSource.setUrl(p.get("url").toString());
        dataSource.setUsername(p.get("username").toString());
        dataSource.setPassword(p.get("password").toString());
        return dataSource;
    }

    private Properties hibernateProperties() {
        Map<String, Object> p = propertiesLoader.getProperties("/application.properties");
        Properties properties = new Properties();
        properties.put("hibernate.dialect", p.get("hibernate.dialect"));
        properties.put("hibernate.show_sql", p.get("hibernate.show_sql"));
        properties.put("hibernate.format_sql", p.get("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", p.get("hibernate.hbm2ddl.auto"));
        return properties;
    }
}
