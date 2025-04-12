package projectwork.starbank.rules;

import projectwork.starbank.dto.RecommendationDto;

import java.util.Optional;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> elevator(String userId);
}
