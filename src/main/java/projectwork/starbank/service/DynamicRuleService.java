package projectwork.starbank.service;

import projectwork.starbank.dto.DynamicRuleDto;

import java.util.List;

/**
 * Интерфейс сервиса для управления динамическими правилами.
 */
public interface DynamicRuleService {

    /**
     * Сохраняет (создает или обновляет) динамическое правило.
     *
     * @param dto DTO с данными правила.
     * @return Сохраненное правило в виде DTO.
     */
    DynamicRuleDto save(DynamicRuleDto dto);

    /**
     * Возвращает список всех динамических правил.
     *
     * @return Список DTO динамических правил.
     */
    List<DynamicRuleDto> getAll();

    /**
     * Удаляет динамическое правило по идентификатору связанного продукта.
     *
     * @param productId Идентификатор продукта.
     */
    void deleteByProductId(String productId);
}
