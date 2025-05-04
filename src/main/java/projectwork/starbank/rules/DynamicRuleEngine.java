package projectwork.starbank.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.List;
import java.util.Map;

/**
 * Движок динамических правил рекомендаций.
 * <p>Для заданного пользователя последовательно проверяет набор условий (правило) в виде списка Map,
 * используя данные из {@link RecommendationRepository}. Если любое условие не выполняется,
 * возвращает {@code false}, иначе — {@code true}.</p>
 */
@Component
public class DynamicRuleEngine {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRuleEngine.class);

    private final RecommendationRepository repository;

    /**
     * Конструирует движок с указанным репозиторием.
     *
     * @param repository репозиторий для доступа к данным пользователя
     */
    public DynamicRuleEngine(RecommendationRepository repository) {
        this.repository = repository;
    }

    /**
     * Оценивает динамическое правило для пользователя.
     * <p>Каждое условие в {@code ruleList} должно вернуть {@code true}. При первом {@code false}
     * метод возвращает {@code false}.</p>
     * <p>
     * Структура одного условия:
     * <ul>
     *   <li><b>query</b> (String) — тип проверки;</li>
     *   <li><b>arguments</b> (List&lt;String&gt;) — параметры проверки;</li>
     *   <li><b>negate</b> (Boolean, optional) — флаг инверсии результата.</li>
     * </ul>
     *
     * @param userId   идентификатор пользователя
     * @param ruleList список условий для проверки
     * @return {@code true}, если все условия выполнены; {@code false} иначе
     */
    public boolean evaluate(String userId, List<Map<String, Object>> ruleList) {
        logger.debug("Evaluating dynamic rules for user: {}", userId);
        // Последовательно проверяем каждое условие в списке
        for (Map<String, Object> rule : ruleList) {
            String query = (String) rule.get("query"); // Тип запроса
            List<String> arguments = (List<String>) rule.get("arguments");
            boolean negate = Boolean.TRUE.equals(rule.get("negate")); // Безопасная проверка на true
            logger.trace("Evaluating rule: query={}, arguments={}, negate={}", query, arguments, negate);

            // Выполняем проверку в зависимости от типа запроса
            boolean result = switch (query) {
                case "USER_OF" -> { // Проверяет, есть ли у пользователя продукт данного типа
                    String productType = arguments.get(0);
                    logger.trace("Calling repository.userHasProductType({}, {})", userId, productType);
                    yield repository.userHasProductType(userId, productType);
                }
                case "ACTIVE_USER_OF" -> { // Проверяет, есть ли у пользователя >= 5 транзакций по продукту данного типа
                    String productType = arguments.get(0);
                    logger.trace("Calling repository.userTransactionCount({}, {})", userId, productType);
                    int count = repository.userTransactionCount(userId, productType);
                    logger.trace("Transaction count: {}", count);
                    yield count >= 5;
                }
                case "TRANSACTION_SUM_COMPARE" -> { // Сравнивает сумму транзакций с целевым значением
                    String productType = arguments.get(0);
                    String transactionType = arguments.get(1);
                    String op = arguments.get(2); // Оператор сравнения (">", "<", "=", и т.д.)
                    double target = Double.parseDouble(arguments.get(3));
                    logger.trace("Calling repository.getSumByTransactionType({}, {}, {})", userId, productType, transactionType);
                    double sum = repository.getSumByTransactionType(userId, productType, transactionType);
                    logger.trace("Transaction sum: {}, comparing with target: {} using operator: {}", sum, target, op);
                    yield compare(sum, op, target);
                }
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> { // Сравнивает сумму пополнений с суммой списаний
                    String productType = arguments.get(0);
                    String op = arguments.get(1); // Оператор сравнения
                    logger.trace("Calling repository.getSumDepositsByProductType/getSumWithdrawalsByProductType for product type: {}", productType);
                    double deposits = repository.getSumDepositsByProductType(userId, productType);
                    double withdrawals = repository.getSumWithdrawalsByProductType(userId, productType);
                    logger.trace("Deposits sum: {}, Withdrawals sum: {}. Comparing using operator: {}", deposits, withdrawals, op);
                    yield compare(deposits, op, withdrawals);
                }
                default -> {
                    logger.warn("Unknown query type in dynamic rule: {}", query);
                    yield false; // Неизвестный тип запроса - считаем условие невыполненным
                }
            };

            // Применяем отрицание, если флаг negate установлен
            if (negate) {
                logger.trace("Negating result: {} -> {}", result, !result);
                result = !result;
            }

            logger.trace("Rule evaluation result: {}", result);

            // Если текущее условие не выполнено, то все правило считается невыполненным
            if (!result) {
                logger.debug("Rule set evaluation failed at rule: {}. Returning false.", rule);
                return false;
            }
        }

        // Если все условия успешно пройдены
        logger.debug("All rules evaluated successfully for user {}. Returning true.", userId);
        return true;
    }

    private boolean compare(double left, String op, double right) {
        return switch (op) {
            case ">" -> left > right;
            case "<" -> left < right;
            case ">=" -> left >= right;
            case "<=" -> left <= right;
            case "=" -> left == right;
            default -> {
                logger.warn("Unsupported comparison operator: {}", op);
                yield false; // Неподдерживаемый оператор - считаем сравнение ложным
            }
        };
    }
}