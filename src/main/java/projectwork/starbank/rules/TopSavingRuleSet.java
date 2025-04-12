package projectwork.starbank.rules;

import org.springframework.stereotype.Component;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.Optional;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    public TopSavingRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> elevator(String userId) {

        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");
        double debitDeposits = repository.getSumDepositsByProductType(userId, "DEBIT");
        double savingDeposits = repository.getSumDepositsByProductType(userId, "SAVING");
        double investExpenses = repository.getSumWithdrawalsByProductType(userId, "DEBIT");

        if (hasDebit && (debitDeposits >= 50_000 || savingDeposits >= 50_000) && debitDeposits > investExpenses) {
            return Optional.of(new RecommendationDto(
                    "147f6a0f-3b91-413b-ab99-87f081d60d5a",
                    "Top Saving",
                    "   Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n" +
                            "\n" +
                            "Преимущества «Копилки»:\n" +
                            "\n" +
                            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n" +
                            "\n" +
                            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n" +
                            "\n" +
                            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n" +
                            "\n" +
                            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"
            ));
        }

        return Optional.empty();
    }
}
