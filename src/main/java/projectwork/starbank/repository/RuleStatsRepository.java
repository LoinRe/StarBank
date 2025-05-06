package projectwork.starbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projectwork.starbank.model.RuleStats;


/**
 * Репозиторий Spring Data JPA для сущности {@link RuleStats}.
 */
public interface RuleStatsRepository extends JpaRepository<RuleStats, String> {
}
