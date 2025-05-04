package projectwork.starbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectwork.starbank.model.DynamicRule;

import java.util.Optional;

/**
 * Spring Data JPA репозиторий для работы с динамическими правилами.
 */
@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {

    /**
     * Удаляет правило по идентификатору связанного продукта.
     *
     * @param productId идентификатор продукта
     */
    @Transactional
    void deleteByProductId(String productId);

    /**
     * Ищет правило по идентификатору связанного продукта.
     *
     * @param productId идентификатор продукта
     * @return найденное правило или пустой Optional, если правило не существует
     */
    Optional<DynamicRule> findByProductId(String productId);
}