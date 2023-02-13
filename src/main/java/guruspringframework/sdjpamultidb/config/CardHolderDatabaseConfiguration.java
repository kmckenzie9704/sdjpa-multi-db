package guruspringframework.sdjpamultidb.config;

import com.zaxxer.hikari.HikariDataSource;
import guruspringframework.sdjpamultidb.domain.cardholder.CreditCardHolder;
import guruspringframework.sdjpamultidb.domain.creditcard.CreditCard;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@EnableJpaRepositories(basePackages = "guruspringframework.sdjpamultidb.repositories.cardholder",
        entityManagerFactoryRef = "cardHolderManagerFactory", transactionManagerRef = "cardHolderTransactionManager")
@Configuration
public class CardHolderDatabaseConfiguration {
    @Bean
    @ConfigurationProperties("spring.cardholder.datasource")
    public DataSourceProperties cardHolderDatabaseProperties()
    {
        return new DataSourceProperties();
    }
    @Bean
    @ConfigurationProperties("spring.cardholder.datasource.hikari")
    public DataSource cardHolderDataSource(@Qualifier("cardHolderDatabaseProperties") DataSourceProperties cardHolderDatabaseProperties) {

        return cardHolderDatabaseProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean cardHolderManagerFactory
            (@Qualifier("cardHolderDataSource") DataSource cardHolderDataSource, EntityManagerFactoryBuilder builder) {

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");

        LocalContainerEntityManagerFactoryBean efb =
                builder.dataSource(cardHolderDataSource).packages(CreditCardHolder.class).persistenceUnit("cardholder").build();

        efb.setJpaProperties(props);

        return efb;
    }

    @Bean
    public PlatformTransactionManager cardHolderTransactionManager
            (@Qualifier("cardHolderManagerFactory") LocalContainerEntityManagerFactoryBean cardHolderManagerFactory){
        return new JpaTransactionManager(cardHolderManagerFactory.getObject());
    }

}
