package projectwork.starbank.service;

import projectwork.starbank.dto.RecommendationDto;

import java.util.List;

/**
 * Интерфейс сервиса для генерации персональных рекомендаций продуктов.
 */
public interface RecommendationService {

    /**
     * Возвращает список рекомендованных продуктов для пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Список DTO рекомендованных продуктов.
     */
    List<RecommendationDto> getRecommendations(String id);
}
