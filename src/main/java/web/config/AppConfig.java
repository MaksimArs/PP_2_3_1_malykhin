package web.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan(value = "web")
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        dataSource.setInitialSize(Integer.valueOf(env.getRequiredProperty("jdbc.initialSize")));
        dataSource.setMinIdle(Integer.valueOf(env.getRequiredProperty("jdbc.minIdle")));
        dataSource.setMaxIdle(Integer.valueOf(env.getRequiredProperty("jdbc.maxIdle")));
        dataSource.setTimeBetweenEvictionRunsMillis(Integer.valueOf(env.getRequiredProperty("jdbc.time")));
        dataSource.setMinEvictableIdleTimeMillis(Integer.valueOf(env.getRequiredProperty("jdbc.minEvictable")));
        dataSource.setTestOnBorrow(Boolean.valueOf(env.getRequiredProperty("jdbc.test")));
        dataSource.setValidationQuery(env.getRequiredProperty("jdbc.validationQuery"));
        return dataSource;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManager() {
        LocalContainerEntityManagerFactoryBean en = new LocalContainerEntityManagerFactoryBean();
        en.setDataSource(getDataSource());

        Properties properties = new Properties();
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));

        en.setJpaProperties(properties);
        en.setPackagesToScan("web");
        en.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return en;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(getEntityManager().getObject());
        return manager;
    }
}
