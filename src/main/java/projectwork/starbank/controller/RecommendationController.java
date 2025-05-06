package projectwork.starbank.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.service.RecommendationService;

import java.util.List;

/**
 * REST‑контроллер для получения персональных рекомендаций банковских продуктов.
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    /**
     * Конструирует контроллер с указанным сервисом рекомендаций.
     *
     * @param recommendationService сервис для формирования рекомендаций
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Возвращает рекомендации для пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return {@link RecommendationResponse} с user_id и списком рекомендованных продуктов
     */
    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendation(@PathVariable String userId) {
        List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);
        return new RecommendationResponse(userId, recommendations);
    }

    /**
     * DTO для ответа контроллера рекомендаций.
     *
     * @param user_id         идентификатор пользователя
     * @param recommendations список {@link RecommendationDto} рекомендованных продуктов
     */
    public record RecommendationResponse(String user_id, List<RecommendationDto> recommendations) {
    }

}
