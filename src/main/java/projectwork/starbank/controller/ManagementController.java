package projectwork.starbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST‑контроллер для административных операций:
 * очистки кэша и получения информации о сборке приложения.
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private BuildProperties buildProperties;

    /**
     * Очищает все зарегистрированные кэши приложения.
     *
     * @return всегда возвращает HTTP 200 OK.
     */
    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает метаданные текущей сборки приложения.
     *
     * @return Map с ключами:
     * <ul>
     *   <li><b>name</b> — имя приложения;</li>
     *   <li><b>version</b> — версия сборки.</li>
     * </ul>
     */
    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        );
    }
}
