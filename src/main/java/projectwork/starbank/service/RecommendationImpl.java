package projectwork.starbank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.rules.RecommendationRuleSet;
import projectwork.starbank.rules.DynamicRuleEngine;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Реализация сервиса {@link RecommendationService} для генерации персональных рекомендаций.
 * Объединяет результаты статических правил ({@link RecommendationRuleSet}) и динамических правил
 * (загружаемых через {@link DynamicRuleService} и выполняемых через {@link DynamicRuleEngine}).
 * Также использует {@link RuleStatsService} для сбора статистики по динамическим правилам.
 */
@Service
public class RecommendationImpl implements RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationImpl.class);
    private final List<RecommendationRuleSet> ruleSets;

    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleEngine dynamicRuleEngine;
    private final RuleStatsService ruleStatsService;

    /**
     * Конструктор сервиса рекомендаций.
     *
     * @param ruleSets Список наборов статических правил.
     * @param dynamicRuleService Сервис для доступа к динамическим правилам.
     * @param dynamicRuleEngine Движок для выполнения динамических правил.
     * @param ruleStatsService Сервис для сбора статистики по правилам.
     */
    public RecommendationImpl(
            List<RecommendationRuleSet> ruleSets,
            DynamicRuleService dynamicRuleService,
            DynamicRuleEngine dynamicRuleEngine,
            RuleStatsService ruleStatsService
    ) {
        this.ruleSets = ruleSets;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleEngine = dynamicRuleEngine;
        this.ruleStatsService = ruleStatsService;
        logger.info("RecommendationImpl initialized with {} static rule sets.", ruleSets.size());
    }

    /**
     * Генерирует список рекомендаций для пользователя.
     * Сначала применяются статические наборы правил, затем динамические правила.
     * Результаты объединяются в один список.
     *
     * @param userId Идентификатор пользователя.
     * @return Список DTO рекомендованных продуктов.
     */
    @Override
    public List<RecommendationDto> getRecommendations(String userId) {
        logger.debug("Generating recommendations for user: {}", userId);

        // 1. Применение статических правил
        List<RecommendationDto> staticRecommendations = ruleSets.stream()
                .map(ruleSet -> {
                    logger.trace("Evaluating static rule set: {} for user: {}", ruleSet.getClass().getSimpleName(), userId);
                    return ruleSet.elevator(userId);
                })
                .filter(optionalRecommendation -> {
                    boolean present = optionalRecommendation.isPresent();
                    if (present) {
                        logger.trace("Static rule resulted in a recommendation for user: {}", userId);
                    }
                    return present;
                })
                .map(Optional::get)
                .toList();

        logger.debug("Generated {} static recommendations for user: {}", staticRecommendations.size(), userId);

        // 2. Применение динамических правил
        List<RecommendationDto> dynamicRecommendations = dynamicRuleService.getAll().stream()
                .filter(ruleDto -> {
                    logger.trace("Evaluating dynamic rule for product: {} for user: {}", ruleDto.getProductId(), userId);
                    // Выполнение MVEL выражения
                    boolean evaluationResult = dynamicRuleEngine.evaluate(userId, ruleDto.getRule());
                    if (evaluationResult) {
                        logger.trace("Dynamic rule for product {} evaluated to true.", ruleDto.getProductId());
                        // Сбор статистики по сработавшему правилу
                        ruleStatsService.increment(ruleDto.getProductId());
                    }
                    return evaluationResult;
                })
                .map(ruleDto -> { // Преобразуем DTO правила (DynamicRuleDto) в DTO рекомендации (RecommendationDto)
                    logger.trace("Mapping dynamic rule DTO to recommendation DTO for product: {}", ruleDto.getProductId());
                    // Создание DTO рекомендации на основе данных из DTO правила
                    return new RecommendationDto(
                            ruleDto.getProductId(),
                            ruleDto.getProductName(),
                            ruleDto.getProductText()
                    );
                })
                .toList();

        logger.debug("Generated {} dynamic recommendations for user: {}", dynamicRecommendations.size(), userId);

        // 3. Объединение результатов
        List<RecommendationDto> combinedRecommendations = Stream.concat(staticRecommendations.stream(), dynamicRecommendations.stream())
                .toList();

        logger.info("Total recommendations generated for user {}: {}. Static: {}, Dynamic: {}",
                userId, combinedRecommendations.size(), staticRecommendations.size(), dynamicRecommendations.size());

        return combinedRecommendations;
    }
}