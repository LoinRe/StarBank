package projectwork.starbank.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import projectwork.starbank.dto.DynamicRuleDto;
import projectwork.starbank.model.DynamicRule;

public class DynamicRuleMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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