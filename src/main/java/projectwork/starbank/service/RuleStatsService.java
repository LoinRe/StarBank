package projectwork.starbank.service;

import projectwork.starbank.model.RuleStats;

import java.util.List;

public interface RuleStatsService {
    void increment(String ruleId);

    void delete(String ruleId);

    List<RuleStats> getAllStats();
}
