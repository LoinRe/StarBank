package projectwork.starbank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendation(@PathVariable String userId){
        logger.info("Received request for recommendations for user: {}", userId);
        List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);
        logger.info("Returning {} recommendations for user: {}", recommendations.size(), userId);
        return new RecommendationResponse(userId, recommendations);
    }

    public record RecommendationResponse(String user_id, List<RecommendationDto> recommendations) {}

}
