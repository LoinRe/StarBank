package projectwork.starbank.rules;

import org.springframework.stereotype.Component;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.Optional;

@Component
public class invest500 implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    public invest500(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> elevator(String userId) {

        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");
        boolean noInvest = !repository.userHasProductType(userId, "INVEST");
        double savingDeposits = repository.getSumDepositsByProductType(userId, "SAVING");

        if (hasDebit && noInvest && savingDeposits > 1000) {
            return Optional.of(new RecommendationDto(
                    "147f6a0f-3b91-413b-ab99-87f081d60d5a",
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
