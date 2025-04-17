package projectwork.starbank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectwork.starbank.mapper.DynamicRuleMapper;
import projectwork.starbank.dto.DynamicRuleDto;
import projectwork.starbank.model.DynamicRule;
import projectwork.starbank.repository.DynamicRuleRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DynamicRuleServiceImpl implements DynamicRuleService {

    private final DynamicRuleRepository repository;

    public DynamicRuleServiceImpl(DynamicRuleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DynamicRuleDto save(DynamicRuleDto dto) {
        DynamicRule ruleEntity = DynamicRuleMapper.toEntity(dto);
        DynamicRule saved = repository.save(ruleEntity);
        return DynamicRuleMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DynamicRuleDto> getAll() {
        List<DynamicRule> allRules = repository.findAll();
        return allRules.stream()
                .map(DynamicRuleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByProductId(String productId) {
        repository.deleteByProductId(productId);
    }
}
