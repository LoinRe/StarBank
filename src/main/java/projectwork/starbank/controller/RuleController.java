package projectwork.starbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectwork.starbank.dto.DynamicRuleDto;
import projectwork.starbank.service.DynamicRuleService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRuleService service;

    public RuleController(DynamicRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DynamicRuleDto> create(@RequestBody DynamicRuleDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", service.getAll());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable String productId) {
        service.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}

