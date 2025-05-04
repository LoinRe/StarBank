package projectwork.starbank.rules;

import org.springframework.stereotype.Component;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.Optional;

/**
 * Набор правил для рекомендации продукта "Top Saving" ("Копилка").
 * Рекомендует пользователям, у которых есть дебетовая карта,
 * сумма пополнений по дебетовым или сберегательным счетам ≥50000,
 * и сумма пополнений по дебетовым картам больше расходов по ним.
 */
@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    /**
     * Конструирует набор правил с указанным репозиторием.
     *
     * @param repository репозиторий для доступа к транзакционным данным пользователя
     */
    public TopSavingRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    /**
     * Проверяет условия для выдачи рекомендации "Top Saving".
     * <ul>
     *   <li>У пользователя есть дебетовая карта;</li>
     *   <li>Сумма пополнений по "DEBIT" или "SAVING" ≥50000;</li>
     *   <li>Сумма пополнений по "DEBIT" > суммы расходов по "DEBIT".</li>
     * </ul>
     *
     * @param userId идентификатор пользователя
     * @return {@code Optional<RecommendationDto>} с DTO рекомендации, если все условия выполнены;
     * иначе пустой {@code Optional}
     */
    @Override
    public Optional<RecommendationDto> elevator(String userId) {

        // Проверка условий:
        // 1. У пользователя есть продукт типа "DEBIT".
        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");
        // 2. Сумма пополнений по "DEBIT" или "SAVING" >= 50,000.
        double debitDeposits = repository.getSumDepositsByProductType(userId, "DEBIT");
        double savingDeposits = repository.getSumDepositsByProductType(userId, "SAVING");
        double investExpenses = repository.getSumWithdrawalsByProductType(userId, "DEBIT");

        if (hasDebit && (debitDeposits >= 50_000 || savingDeposits >= 50_000) && debitDeposits > investExpenses) {
            return Optional.of(new RecommendationDto(
                    "59efc529-2fff-41af-baff-90ccd7402925", // ID продукта "Top Saving"
                    "Top Saving",
                    "   Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. " +
                            "Больше никаких забытых чеков и потерянных квитанций — всё под контролем! " +
                            "Преимущества «Копилки»: " +
                            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет. " +
                            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости. " +
                            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг. " +
                            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"
            ));
        }

        return Optional.empty();
    }
}
