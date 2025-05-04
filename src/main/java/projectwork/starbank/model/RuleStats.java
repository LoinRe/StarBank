package projectwork.starbank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Сущность JPA для хранения статистики использования правил.
 */
@Entity
public class RuleStats {

    /**
     * Идентификатор правила (совпадает с productId в {@link DynamicRule}).
     */
    @Id
    private String ruleId;

    /**
     * Количество срабатываний правила.
     */
    private long count;

    public RuleStats() {
    }

    public RuleStats(String ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
