package projectwork.starbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectwork.starbank.dto.DynamicRuleDto;
import projectwork.starbank.service.DynamicRuleService;

import java.util.HashMap;
import java.util.Map;

/**
 * REST‑контроллер для управления динамическими правилами.
 * Предоставляет эндпоинты для создания, получения и удаления правил.
 */
@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRuleService service;

    /**
     * Конструктор контроллера.
     *
     * @param service Сервис для работы с динамическими правилами.
     */
    public RuleController(DynamicRuleService service) {
        this.service = service;
    }

    /**
     * Создаёт новое динамическое правило.
     *
     * @param dto {@link DynamicRuleDto} с данными правила для сохранения
     * @return {@link ResponseEntity} с телом созданного {@link DynamicRuleDto} и статусом 200 OK
     */
    @PostMapping
    public ResponseEntity<DynamicRuleDto> create(@RequestBody DynamicRuleDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    /**
     * Возвращает все существующие динамические правила.
     *
     * @return {@link ResponseEntity} со статусом 200 OK и телом {@code Map<String, Object>},
     * где по ключу "data" содержится список {@link DynamicRuleDto}
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", service.getAll());
        return ResponseEntity.ok(result);
    }

    /**
     * Удаляет динамическое правило, связанное с указанным идентификатором продукта.
     *
     * @param productId идентификатор продукта, для которого удаляется правило
     * @return {@link ResponseEntity} со статусом 204 No Content
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable String productId) {
        service.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
