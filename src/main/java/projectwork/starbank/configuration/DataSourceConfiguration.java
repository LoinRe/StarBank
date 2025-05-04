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

/**
 * Настраивает два источника данных:
 * <ul>
 *   <li>PostgreSQL (основной, для JPA‑репозиториев правил)</li>
 *   <li>H2 (встроенный, для истории рекомендаций через JdbcTemplate)</li>
 * </ul>
 *
 * Для основного DataSource помечается аннотацией {@link Primary}.
 */
@Configuration
@EnableAutoConfiguration(exclude = {
        JdbcRepositoriesAutoConfiguration.class // Исключаем автоконфигурацию JDBC репозиториев, т.к. используем JPA
})
@EnableJpaRepositories(
        basePackages = "projectwork.starbank.repository", // Сканируем репозитории в этом пакете
        entityManagerFactoryRef = "entityManagerFactory", // Используем EntityManagerFactory по умолчанию
        transactionManagerRef = "transactionManager"      // Используем TransactionManager по умолчанию
)
public class DataSourceConfiguration {

    /**
     * Создает и настраивает H2 DataSource.
     * Используется для хранения истории выдачи рекомендаций.
     *
     * @return настроенный H2 DataSource
     */
    @Bean(name = "h2DataSource")
    public HikariDataSource h2DataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        // TODO: Убедиться, что этот DataSource действительно нужен.
        dataSource.setJdbcUrl("jdbc:h2:file:./transaction;ACCESS_MODE_DATA=r");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    /**
     * H2 DataSource для JDBC‑шаблона рекомендаций.
     *
     * @return H2 DataSource для таблиц рекомендаций
     */
    @Bean(name = "recommendationsDataSource")
    public HikariDataSource recommendationsDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:file:./transaction;ACCESS_MODE_DATA=r");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }


    /**
     * JdbcTemplate для работы с H2 DataSource рекомендаций.
     *
     * @param dataSource H2 DataSource, бинуемый по имени "recommendationsDataSource"
     * @return JdbcTemplate для работы с таблицами рекомендаций
     */
    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(@Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Основной DataSource для подключения к PostgreSQL.
     * Используется JPA‑репозиториями для хранения и загрузки динамических правил.
     *
     * @return настроенный PostgreSQL DataSource
     */
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