package projectwork.starbank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectwork.starbank.model.RuleStats;
import projectwork.starbank.repository.RuleStatsRepository;

import java.util.List;

@Service
public class RuleStatsServiceImpl implements RuleStatsService {
    private final RuleStatsRepository repository;

    public RuleStatsServiceImpl(RuleStatsRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public void increment(String ruleId) {
        RuleStats stats = repository.findById(ruleId).orElse(new RuleStats(ruleId, 0));
        stats.setCount(stats.getCount() + 1);
        repository.save(stats);
    }

    @Transactional
    @Override
    public void delete(String ruleId) {
        repository.deleteById(ruleId);
    }

    @Override
    public List<RuleStats> getAllStats() {
        return repository.findAll();
    }
}
