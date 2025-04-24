package projectwork.starbank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RuleStats {

    @Id
    private String ruleId;
    private long count;

    public RuleStats() {}

    public RuleStats(String ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}
