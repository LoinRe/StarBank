package projectwork.starbank.rules;

import projectwork.starbank.dto.RecommendationDto;

import java.util.Optional;

/**
 * Интерфейс для наборов статических правил рекомендаций.
 * Каждый набор правил реализует свою логику для определения,
 * следует ли рекомендовать определенный продукт пользователю.
 */
public interface RecommendationRuleSet {
    /**
     * Выполняет логику набора правил для указанного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Optional, содержащий {@link RecommendationDto}, если правило сработало
     *         и продукт рекомендуется, или пустой Optional в противном случае.
     */
    Optional<RecommendationDto> elevator(String userId);
}
