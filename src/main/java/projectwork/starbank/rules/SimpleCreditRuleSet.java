package projectwork.starbank.rules;

import org.springframework.stereotype.Component;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.Optional;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {
    private final RecommendationRepository repository;

    public SimpleCreditRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> elevator(String userId) {

        boolean hasNotCredit = !repository.userHasProductType(userId, "CREDIT");
        double debitDeposits = repository.getSumDepositsByProductType(userId, "DEBIT");
        double debitExpenses = repository.getSumWithdrawalsByProductType(userId, "DEBIT");
        if (hasNotCredit && debitDeposits > debitExpenses && debitDeposits > 100_000) {
            return Optional.of(new RecommendationDto(
                    "147f6a0f-3b91-413b-ab99-87f081d60d5a",
                    "Simple Credit ",
                    "Откройте мир выгодных кредитов с нами!\n" +
                            "\n" +
                            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n" +
                            "\n" +
                            "Почему выбирают нас:\n" +
                            "\n" +
                            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n" +
                            "\n" +
                            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n" +
                            "\n" +
                            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n" +
                            "\n" +
                            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!"
            ));
        }

        return Optional.empty();
    }
}
