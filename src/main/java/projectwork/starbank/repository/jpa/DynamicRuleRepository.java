package projectwork.starbank.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectwork.starbank.model.DynamicRule;

import java.util.Optional;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {
    @Transactional
    void deleteByProductId(String productId);
    Optional<DynamicRule> findByProductId(String productId);
} 