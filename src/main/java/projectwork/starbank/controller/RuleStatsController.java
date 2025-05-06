package projectwork.starbank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectwork.starbank.model.RuleStats;
import projectwork.starbank.service.RuleStatsService;

import java.util.List;
import java.util.Map;

/**
 * REST‑контроллер для получения статистики использования динамических правил.
 * Предоставляет эндпоинт для выборки количества срабатываний каждого правила.
 */
@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final RuleStatsService statsService;

    /**
     * Конструирует контроллер со службой статистики правил.
     *
     * @param statsService сервис для получения данных по срабатываниям правил
     */
    public RuleStatsController(RuleStatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Возвращает статистику использования правил.
     * Формирует список, где каждый элемент содержит:
     * <ul>
     *   <li><b>rule_id</b> — идентификатор правила;</li>
     *   <li><b>count</b> — количество срабатываний.</li>
     * </ul>
     *
     * @return карта с ключом "stats", значением которого является список статистики правил
     */
    @GetMapping("/stats")
    public Map<String, List<Map<String, Object>>> getStats() {
        List<RuleStats> stats = statsService.getAllStats();
        List<Map<String, Object>> statsList = stats.stream()
                .map(s -> {
                    Map<String, Object> map = Map.of(
                            "rule_id", s.getRuleId(),
                            "count", s.getCount()
                    );
                    return map;
                })
                .toList();
        return Map.of("stats", statsList);
    }
}