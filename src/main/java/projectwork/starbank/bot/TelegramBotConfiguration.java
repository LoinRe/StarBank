package projectwork.starbank.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для создания бина Telegram-бота.
 */
@Configuration
public class TelegramBotConfiguration {
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Создает и настраивает бин {@link TelegramBot}.
     * Использует токен, указанный в свойстве 'telegram.bot.token'.
     * При инициализации удаляет все ранее установленные команды бота.
     *
     * @return Экземпляр {@link TelegramBot}.
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}