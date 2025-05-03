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

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::handleUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleUpdate(Update update) {
        logger.info("Processing update: {}", update);
        Message message = update.message();

        if (message == null || message.text() == null) return;

        String chatId = message.chat().id().toString();
        String text = message.text();

        if ("/start".equals(text)) {
            handleStartCommand(chatId);
        } else if (text.startsWith("/recommend ")) {
            handleRecommendCommand(chatId, text.replace("/recommend ", "").trim());
        }
    }

    private void handleStartCommand(String chatId) {
        String helpMessage = "Добро пожаловать в StarBank Bot! \nКоманда: /recommend username";
        telegramBot.execute(new SendMessage(chatId, helpMessage));
    }

    private void handleRecommendCommand(String chatId, String username) {
        List<User> users = userRepository.findByUsername(username);
        if (users.size() != 1) {
            telegramBot.execute(new SendMessage(chatId, "Пользователь не найден."));
            return;
        }

        User user = users.get(0);
        List<RecommendationDto> recs = recommendationService.getRecommendations(user.getId());

        if (recs.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId,
                    "Здравствуйте, " + user.getFirstName() + " " + user.getLastName() + "\nНовых продуктов для вас нет."));
        } else {
            String message = buildRecommendationMessage(user, recs);
            telegramBot.execute(new SendMessage(chatId, message));
        }
    }

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
