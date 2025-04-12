package projectwork.starbank.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendation(@PathVariable String userId){
        List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);
        return new RecommendationResponse(userId, recommendations);
    }

    public record RecommendationResponse(String user_id, List<RecommendationDto> recommendations) {}

}
