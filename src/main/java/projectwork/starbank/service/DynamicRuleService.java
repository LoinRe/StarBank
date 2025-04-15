package projectwork.starbank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectwork.starbank.Mapper.DynamicRuleMapper;
import projectwork.starbank.dto.DynamicRuleDto;
import projectwork.starbank.model.DynamicRule;
import projectwork.starbank.repository.jpa.DynamicRuleRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DynamicRuleService {

    private final DynamicRuleRepository repository;
    public DynamicRuleService(DynamicRuleRepository repository) {
        this.repository = repository;
    }


    @Transactional // readOnly = false по умолчанию для @Transactional
    public DynamicRuleDto save(DynamicRuleDto dto) {
        DynamicRule ruleEntity = DynamicRuleMapper.toEntity(dto);
        DynamicRule saved = repository.save(ruleEntity);
        return DynamicRuleMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DynamicRuleDto> getAll() {
        List<DynamicRule> allRules = repository.findAll();
        return allRules.stream() // Создаем поток из списка сущностей
                .map(DynamicRuleMapper::toDto) // Применяем метод toDto к каждому элементу потока
                .collect(Collectors.toList()); // Собираем результат обратно в список
    }

    @Transactional
    public void deleteByProductId(String productId) {
        repository.deleteByProductId(productId);
    }
}
