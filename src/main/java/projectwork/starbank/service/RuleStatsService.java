package projectwork.starbank.service;

import projectwork.starbank.model.RuleStats;

import java.util.List;

/**
 * Интерфейс сервиса для работы со статистикой использования правил.
 */
public interface RuleStatsService {

    /**
     * Увеличивает счетчик использования для указанного правила.
     *
     * @param ruleId Идентификатор правила.
     */
    void increment(String ruleId);

    /**
     * Удаляет статистику для указанного правила.
     *
     * @param ruleId Идентификатор правила.
     */
    void delete(String ruleId);

    /**
     * Возвращает всю собранную статистику по правилам.
     *
     * @return Список объектов {@link RuleStats}.
     */
    List<RuleStats> getAllStats();
}
