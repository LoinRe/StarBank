package projectwork.starbank.rules;

import org.springframework.stereotype.Component;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.Optional;

/**
 * Набор правил для рекомендации «Простой кредит».
 * <p>Выполняет проверку следующих условий для пользователя:</p>
 * <ul>
 *   <li>Нет открытого кредитного продукта (CREDIT).</li>
 *   <li>Сумма пополнений по дебетовым картам (DEBIT) больше суммы списаний.</li>
 *   <li>Сумма списаний по дебетовым картам превышает 100000.</li>
 * </ul>
 * <p>Если все условия выполняются — возвращает рекомендацию, иначе — пустой результат.</p>
 */
@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {
    private final RecommendationRepository repository;

    /**
     * Конструирует набор правил с указанным репозиторием.
     *
     * @param repository репозиторий для доступа к транзакционным данным пользователя
     */
    public SimpleCreditRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    /**
     * Проверяет условия для выдачи рекомендации «Простой кредит».
     *
     * @param userId идентификатор пользователя
     * @return {@code Optional} с {@link RecommendationDto}, если все условия выполнены;
     * иначе пустой {@code Optional}
     */
    @Override
    public Optional<RecommendationDto> elevator(String userId) {
        // Проверка условий:
        // 1. У пользователя нет продуктов типа "CREDIT".
        boolean hasNotCredit = !repository.userHasProductType(userId, "CREDIT");
        // 2. Сумма пополнений по продуктам типа "DEBIT" больше суммы списаний.
        double debitDeposits = repository.getSumDepositsByProductType(userId, "DEBIT");
        double debitExpenses = repository.getSumWithdrawalsByProductType(userId, "DEBIT");

        if (hasNotCredit && debitDeposits > debitExpenses && debitExpenses > 100_000) {
            return Optional.of(new RecommendationDto(
                    "ab138afb-f3ba-4a93-b74f-0fcee86d447f", // ID продукта "Простой кредит"
                    "Простой кредит", // ← Название из ТЗ
                    "Откройте мир выгодных кредитов с нами! " +
                            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! " +
                            "Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту. " +
                            "Почему выбирают нас: " +
                            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов. " +
                            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении. " +
                            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое. " +
                            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!"
            ));
        }

        return Optional.empty();
    }
}
