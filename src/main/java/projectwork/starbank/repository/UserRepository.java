package projectwork.starbank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectwork.starbank.dto.User;

import java.util.List;

/**
 * Репозиторий для доступа к данным пользователей.
 * Использует {@link JdbcTemplate} для выполнения SQL‑запросов к таблице users.
 */
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструирует репозиторий с указанным {@link JdbcTemplate}.
     *
     * @param jdbcTemplate JdbcTemplate, бинуемый под именем "recommendationsJdbcTemplate",
     *                     настроенный для подключения к базе рекомендаций
     */
    public UserRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Ищет всех пользователей с данным username.
     *
     * @param username логин пользователя для поиска
     * @return список {@link User} с указанным username; может быть пустым
     */
    public List<User> findByUsername(String username) {
        String sql = """
                SELECT id, username, first_name, last_name
                FROM users
                WHERE username = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name")
        ), username);
    }
}
