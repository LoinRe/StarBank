package projectwork.starbank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Основной класс приложения StarBank.
 * Инициализирует Spring Boot приложение, включает кэширование и планирование задач.
 */
@SpringBootApplication
@OpenAPIDefinition()
@EnableCaching
@EnableScheduling
public class StarBankApplication {

    /**
     * Точка входа в приложение.
     * Запускает Spring Boot приложение.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(StarBankApplication.class, args);
    }

}
