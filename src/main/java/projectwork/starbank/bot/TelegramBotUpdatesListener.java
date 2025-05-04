package projectwork.starbank.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.UpdatesListener;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.dto.User;
import projectwork.starbank.repository.UserRepository;
import projectwork.starbank.service.RecommendationService;

import java.util.List;

/**
 * Обработчик входящих обновлений Telegram‑бота.
 * Реализует интерфейс UpdatesListener для приёма команд /start и /recommend,
 * взаимодействует с RecommendationService и UserRepository
 * и отправляет пользователю персональные рекомендации.
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Регистрирует этот бин как обработчик обновлений у TelegramBot.
     * Этот метод автоматически вызывается после создания бина.
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Обрабатывает пакет обновлений от Telegram API.
     * Делегирует обработку каждого обновления методу {@link #handleUpdate(Update)}.
     *
     * @param updates Список обновлений.
     * @return Всегда {@link UpdatesListener#CONFIRMED_UPDATES_ALL}, подтверждая обработку всех обновлений.
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::handleUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Обрабатывает одно обновление (обычно входящее сообщение).
     * Извлекает ID чата и текст сообщения.
     * Вызывает соответствующий обработчик команды (/start или /recommend).
     *
     * @param update Объект обновления.
     */
    private void handleUpdate(Update update) {
        logger.info("Processing update: {}", update);
        Message message = update.message();

        if (message == null || message.text() == null) {
            logger.trace("Skipping update with no message or text: {}", update.updateId());
            return; // Игнорируем обновления без текстовых сообщений
        }

        String chatId = message.chat().id().toString();
        String text = message.text();

        // Маршрутизация команд
        if ("/start".equals(text)) {
            handleStartCommand(chatId);
        } else if (text.startsWith("/recommend ")) {
            handleRecommendCommand(chatId, text.replace("/recommend ", "").trim());
        }
    }

    /**
     * Обрабатывает команду /start.
     * Отправляет приветственное сообщение с инструкцией по использованию.
     *
     * @param chatId ID чата.
     */
    private void handleStartCommand(String chatId) {
        String helpMessage = "Добро пожаловать в StarBank Bot! \nКоманда: /recommend username";
        logger.debug("Sending start message to chat {}", chatId);
        telegramBot.execute(new SendMessage(chatId, helpMessage));
    }

    /**
     * Обрабатывает команду /recommend username.
     * Ищет пользователя по username, запрашивает рекомендации и отправляет их в чат.
     * Если пользователь не найден или нет рекомендаций, отправляет соответствующее сообщение.
     *
     * @param chatId   ID чата.
     * @param username Имя пользователя, для которого нужны рекомендации.
     */
    private void handleRecommendCommand(String chatId, String username) {
        logger.debug("Handling /recommend command for username '{}' in chat {}", username, chatId);
        List<User> users = userRepository.findByUsername(username);
        if (users.size() != 1) {
            logger.warn("User not found or multiple users found for username '{}'", username);
            telegramBot.execute(new SendMessage(chatId, "Пользователь не найден."));
            return;
        }

        User user = users.get(0);
        logger.debug("Found user: {} ({})", user.getUsername(), user.getId());
        List<RecommendationDto> recs = recommendationService.getRecommendations(user.getId());

        if (recs.isEmpty()) {
            logger.info("No recommendations found for user {} ({})", user.getUsername(), user.getId());
            telegramBot.execute(new SendMessage(chatId,
                    "Здравствуйте, " + user.getFirstName() + " " + user.getLastName() + "\nНовых продуктов для вас нет."));
        } else {
            logger.info("Found {} recommendations for user {} ({})", recs.size(), user.getUsername(), user.getId());
            String message = buildRecommendationMessage(user, recs);
            telegramBot.execute(new SendMessage(chatId, message));
        }
    }

    /**
     * Формирует текстовое сообщение с рекомендациями для пользователя.
     *
     * @param user Пользователь (DTO).
     * @param recs Список рекомендаций (DTO).
     * @return Отформатированная строка сообщения.
     */
    private String buildRecommendationMessage(User user, List<RecommendationDto> recs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Здравствуйте, ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n");
        sb.append("Новые продукты для вас:\n\n");

        recs.forEach(r -> {
            sb.append("🎁 ").append(r.getName()).append("\n");
            sb.append("✨ ").append(r.getText()).append("\n");
            sb.append("\n---\n\n");
        });

        return sb.toString();
    }
}
