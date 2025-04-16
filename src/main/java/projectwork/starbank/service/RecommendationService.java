package projectwork.starbank.service;

import projectwork.starbank.dto.RecommendationDto;

import java.util.List;

public interface RecommendationService {
    List<RecommendationDto> getRecommendations(String id);
}
