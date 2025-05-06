package projectwork.starbank.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import projectwork.starbank.dto.DynamicRuleDto;
import projectwork.starbank.model.DynamicRule;

/**
 * Класс-маппер для преобразования между {@link DynamicRule} (сущность) и {@link DynamicRuleDto} (DTO).
 * Отвечает за (де)сериализацию поля `rule` в/из JSON строки с использованием {@link ObjectMapper}.
 */
public class DynamicRuleMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Преобразует DTO в сущность.
     * Сериализует список правил из DTO в JSON-строку.
     *
     * @param dto объект {@link DynamicRuleDto} для преобразования
     * @return новая сущность {@link DynamicRule} с заполненным полем rule в формате JSON
     * @throws RuntimeException при ошибке сериализации JSON
     */
    public static DynamicRule toEntity(DynamicRuleDto dto) {
        DynamicRule entity = new DynamicRule();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setProductText(dto.getProductText());

        try {
            entity.setRule(objectMapper.writeValueAsString(dto.getRule()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации правил", e);
        }

        return entity;
    }

    /**
     * Преобразует сущность в DTO.
     * Десериализует JSON-строку rule в список карт {@code List<Map<String,Object>>}.
     *
     * @param entity объект {@link DynamicRule} для преобразования
     * @return новый {@link DynamicRuleDto} с полем rule в виде структуры данных
     * @throws RuntimeException при ошибке десериализации JSON
     */
    public static DynamicRuleDto toDto(DynamicRule entity) {
        DynamicRuleDto dto = new DynamicRuleDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setProductText(entity.getProductText());

        try {
            dto.setRule(objectMapper.readValue(entity.getRule(), new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации правил", e);
        }

        return dto;
    }
}