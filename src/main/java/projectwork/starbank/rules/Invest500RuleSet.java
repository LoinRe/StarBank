package projectwork.starbank.rules;

import org.springframework.stereotype.Component;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.Optional;

/**
 * Набор правил для рекомендации продукта "Invest 500" (ИИС).
 * Рекомендует ИИС пользователям, у которых есть дебетовая карта, но нет инвестиционных продуктов,
 * и при этом сумма пополнений по сберегательным счетам превышает 1000.
 */
@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    /**
     * Конструктор набора правил.
     *
     * @param repository Репозиторий для доступа к данным транзакций пользователя.
     */
    public Invest500RuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> elevator(String userId) {

        // Проверка условий:
        // 1. У пользователя есть продукт типа "DEBIT".
        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");
        // 2. У пользователя нет продуктов типа "INVEST".
        boolean noInvest = !repository.userHasProductType(userId, "INVEST");
        // 3. Сумма пополнений по продуктам типа "SAVING" больше 1000.
        double savingDeposits = repository.getSumDepositsByProductType(userId, "SAVING");

        if (hasDebit && noInvest && savingDeposits > 1000) {
            return Optional.of(new RecommendationDto(
                    "147f6a0f-3b91-413b-ab99-87f081d60d5a", // ID продукта "Invest 500"
                    "Invest 500",
                    "Откройте свой путь к успеху с индивидуальным инвестиционным " +
                            "счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами" +
                            " и начните инвестировать с умом. Пополните счет до конца года " +
                            "и получите выгоду в виде вычета на взнос в следующем налоговом" +
                            " периоде. Не упустите возможность разнообразить свой портфель," +
                            " снизить риски и следить за актуальными рыночными тенденциями." +
                            " Откройте ИИС сегодня и станьте ближе к финансовой независимости!"
            ));
        }

        return Optional.empty();

    }
}
