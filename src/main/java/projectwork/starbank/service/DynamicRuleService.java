package projectwork.starbank.service;

import projectwork.starbank.dto.DynamicRuleDto;

import java.util.List;

public interface DynamicRuleService {
    DynamicRuleDto save(DynamicRuleDto dto);

    List<DynamicRuleDto> getAll();

    void deleteByProductId(String productId);
}
