package projectwork.starbank.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Конфигурирует кэширование приложения на основе Caffeine.
 * Создаёт {@link CaffeineCacheManager} с предопределёнными именами кэшей
 * и общими настройками времени жизни и размера.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Создаёт и настраивает {@link CaffeineCacheManager} для трёх кэшей:
     * <ul>
     *   <li><code>userOf</code></li>
     *   <li><code>sumByType</code></li>
     *   <li><code>userTransactionCount</code></li>
     * </ul>
     * Все записи в кэше живут 10 минут после записи, максимальный размер — 1000 элементов.
     *
     * @return настроенный {@link CaffeineCacheManager}
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // Зарегистрирует кэши по названиям, которые будут использоваться в @Cacheable
        cacheManager.setCacheNames(List.of("userOf", "sumByType", "userTransactionCount"));

        // Настройки всех кэшей
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000));
        return cacheManager;
    }
}
