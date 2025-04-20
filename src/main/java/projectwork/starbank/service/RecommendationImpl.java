package projectwork.starbank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.rules.RecommendationRuleSet;
import projectwork.starbank.rules.DynamicRuleEngine;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecommendationImpl implements RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationImpl.class);
    private final List<RecommendationRuleSet> ruleSets;

    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleEngine dynamicRuleEngine;
    public RecommendationImpl(
            List<RecommendationRuleSet> ruleSets,
            DynamicRuleService dynamicRuleService,
            DynamicRuleEngine dynamicRuleEngine
    ) {
        this.ruleSets = ruleSets;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleEngine = dynamicRuleEngine;
        logger.info("RecommendationImpl initialized with {} static rule sets.", ruleSets.size());
    }

    @Override
    public List<RecommendationDto> getRecommendations(String userId) {
        logger.debug("Generating recommendations for user: {}", userId);

        List<RecommendationDto> staticRecommendations = ruleSets.stream()
                .map(ruleSet -> {
                    logger.trace("Evaluating static rule set: {} for user: {}", ruleSet.getClass().getSimpleName(), userId);
                    return ruleSet.elevator(userId);
                })
                .filter(optionalRecommendation -> {
                    boolean present = optionalRecommendation.isPresent();
                    if (present) {
                        logger.trace("Static rule resulted in a recommendation for user: {}", userId);
                    } else {
                    }
                    return present;
                })
                .map(Optional::get)
                .toList();

        logger.debug("Generated {} static recommendations for user: {}", staticRecommendations.size(), userId);

        List<RecommendationDto> dynamicRecommendations = dynamicRuleService.getAll().stream()
                .filter(ruleDto -> {
                    logger.trace("Evaluating dynamic rule for product: {} for user: {}", ruleDto.getProductId(), userId);
                    boolean evaluationResult = dynamicRuleEngine.evaluate(userId, ruleDto.getRule());
                    if (evaluationResult) {
                        logger.trace("Dynamic rule for product {} evaluated to true.", ruleDto.getProductId());
                    } else {
                    }
                    return evaluationResult;
                })
                .map(ruleDto -> { // Преобразуем DTO правила (DynamicRuleDto) в DTO рекомендации (RecommendationDto)
                    logger.trace("Mapping dynamic rule DTO to recommendation DTO for product: {}", ruleDto.getProductId());

                    return new RecommendationDto(
                            ruleDto.getProductId(),
                            ruleDto.getProductName(),
                            ruleDto.getProductText()
                    );
                })
                .toList();

        logger.debug("Generated {} dynamic recommendations for user: {}", dynamicRecommendations.size(), userId);

        List<RecommendationDto> combinedRecommendations = Stream.concat(staticRecommendations.stream(), dynamicRecommendations.stream())
                .toList();

        logger.info("Total recommendations generated for user {}: {}. Static: {}, Dynamic: {}",
                userId, combinedRecommendations.size(), staticRecommendations.size(), dynamicRecommendations.size());

        return combinedRecommendations;
    }
}