package projectwork.starbank.service;

import org.springframework.stereotype.Service;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.rules.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationImpl implements RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationImpl(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    @Override
    public List<RecommendationDto> getRecommendations(String userId) {
        return ruleSets.stream().
                map(rule -> rule.elevator(userId)).
                filter(Optional::isPresent).
                map(Optional::get)
                .collect(Collectors.toList());
    }
}