package projectwork.starbank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectwork.starbank.model.RuleStats;
import projectwork.starbank.service.RuleStatsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final RuleStatsService statsService;

    public RuleStatsController(RuleStatsService statsService) {
        this.statsService = statsService;
    }

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
