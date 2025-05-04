package projectwork.starbank.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для агрегирования транзакционных данных пользователя.
 * Использует {@link JdbcTemplate} и кэширование для повышения производительности.
 */
@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструирует репозиторий с указанным JdbcTemplate.
     *
     * @param jdbcTemplate шаблон для выполнения SQL‑запросов, бинуемый под именем
     *                     "recommendationsJdbcTemplate"
     */
    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяет, есть ли у пользователя хотя бы одна транзакция указанного типа продукта.
     * Результат кэшируется под именем "userOf".
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return {@code true}, если количество транзакций > 0; иначе {@code false}
     */
    @Cacheable(value = "userOf", key = "#userId + '-' + #productType")
    public boolean userHasProductType(String userId, String productType) {
        String sql = """
                    SELECT COUNT(*) FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
        return count != null && count > 0;
    }

    /**
     * Возвращает суммарную сумму пополнений (DEPOSIT) по продуктам указанного типа.
     * Результат кэшируется под именем "sumByType".
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return сумма всех пополнений
     */
    @Cacheable(value = "sumByType", key = "#userId + '-' + #productType + '-' + \'DEPOSIT\'")
    public double getSumDepositsByProductType(String userId, String productType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
                """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
    }

    /**
     * Возвращает суммарную сумму списаний (WITHDRAW) по продуктам указанного типа.
     * Результат кэшируется под именем "sumByType".
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return сумма всех списаний
     */
    @Cacheable(value = "sumByType", key = "#userId + '-' + #productType + '-' + \'WITHDRAW\'")
    public double getSumWithdrawalsByProductType(String userId, String productType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
                """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
    }

    /**
     * Возвращает общее число транзакций пользователя по продуктам указанного типа.
     * Результат кэшируется под именем "userTransactionCount".
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return общее количество транзакций
     */
    @Cacheable(value = "userTransactionCount", key = "#userId + '-' + #productType")
    public int userTransactionCount(String userId, String productType) {
        String sql = """
                    SELECT COUNT(*) FROM transactions t 
                    JOIN products p ON t.product_id = p.id 
                    WHERE t.user_id = ? AND p.type = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    /**
     * Возвращает суммарную сумму транзакций указанного типа для продуктов указанного типа.
     * Результат кэшируется под именем "sumByType".
     *
     * @param userId          идентификатор пользователя
     * @param productType     тип продукта
     * @param transactionType тип транзакции ("DEPOSIT", "WITHDRAW" и т.п.)
     * @return сумма транзакций указанного типа
     */
    @Cacheable(value = "sumByType", key = "#userId + '-' + #productType + '-' + #transactionType")
    public double getSumByTransactionType(String userId, String productType, String transactionType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount), 0) FROM transactions t 
                    JOIN products p ON t.product_id = p.id 
                    WHERE t.user_id = ? AND p.type = ? AND t.type = ? 
                """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType, transactionType);
    }
}

