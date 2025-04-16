package projectwork.starbank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Проверка: есть ли у пользователя хотя бы один продукт указанного типа
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
    public double getSumDepositsByProductType(String userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
        """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
    }

    // Сумма всех трат по продуктам определённого типа
    public double getSumWithdrawalsByProductType(String userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
        """;
        return jdbcTemplate.queryForObject(sql, Double.class, userId, productType);
    }
}
