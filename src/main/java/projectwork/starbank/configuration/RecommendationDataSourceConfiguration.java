package projectwork.starbank.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class RecommendationDataSourceConfiguration {

    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource(@Value("${application.recommendations-db.url}") String recommendationsUrl) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(recommendationsUrl);
        config.setDriverClassName("org.h2.Driver");
        config.setReadOnly(true);

        Logger logger = LoggerFactory.getLogger(HikariDataSource.class);
        config.setInitializationFailTimeout(30000);
        config.setRegisterMbeans(true);

        HikariDataSource dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection()) {
            logger.info("H2 Connection OK! ReadOnly: {}", conn.isReadOnly());
        } catch (SQLException e) {
            logger.error("Failed to connect to H2 database", e);
            throw new IllegalStateException("H2 connection failed", e);
        }
        return new HikariDataSource(config);
    }

    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }
}
