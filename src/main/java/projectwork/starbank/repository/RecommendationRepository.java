package projectwork.starbank.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Проверка: есть ли у пользователя хотя бы один продукт указанного типа
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

    // Сумма всех пополнений по продуктам определённого типа
    @Cacheable(value = "sumByType", key = "#userId + '-' + #productType + '-' + #transactionType")
    public double getSumDepositsByProductType(String userId, String productType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
                """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
    }

    // Сумма всех трат по продуктам определённого типа
    @Cacheable(value = "sumByType", key = "#userId + '-' + #productType + '-' + #transactionType")
    public double getSumWithdrawalsByProductType(String userId, String productType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
                """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
    }

    @Cacheable(value = "userTransactionCount", key = "#userId + '-' + #productType")
    public int userTransactionCount(String userId, String productType) {
        String sql = """
                    SELECT COUNT(*) FROM transactions t 
                    JOIN products p ON t.product_id = p.id 
                    WHERE t.user_id = ? AND p.type = ? W
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

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

