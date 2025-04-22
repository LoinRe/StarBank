package projectwork.starbank.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration(exclude = {
        JdbcRepositoriesAutoConfiguration.class
})
@EnableJpaRepositories(
        basePackages = "projectwork.starbank.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class DataSourceConfiguration {

    @Bean(name = "h2DataSource")
    public HikariDataSource h2DataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:file:./transaction;ACCESS_MODE_DATA=r");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean(name = "recommendationsDataSource")
    public HikariDataSource recommendationsDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:file:./transaction;ACCESS_MODE_DATA=r");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }


    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(@Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "dataSource") // Стандартное имя для основного DataSource
    public HikariDataSource rulesDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/StarBankRules");
        dataSource.setUsername("User");
        dataSource.setPassword("123");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
} 