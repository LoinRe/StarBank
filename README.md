# StarBank Recommendation Service

## Описание проекта

Сервис персональных рекомендаций банковских продуктов для клиентов StarBank.

Сервис предоставляет REST API для управления правилами рекомендаций и получения персональных рекомендаций для пользователей.
Также имеется интеграция с Telegram-ботом для получения рекомендаций через чат.

## Основные возможности

*   **Персональные рекомендации:** Генерация списка рекомендованных продуктов на основе статических и динамических правил.
*   **Статические правила:** Предопределенные наборы правил для общих сценариев (например, рекомендации кредитов, сберегательных продуктов, ИИС).
*   **Динамические правила:** Возможность добавления, просмотра и удаления правил через REST API без перезапуска сервиса. Правила хранятся в базе данных и используют MVEL-подобный синтаксис.
*   **Сбор статистики:** Подсчет количества срабатываний динамических правил.
* **REST API:**
    * `GET  /recommendation/{userId}` — получить рекомендации для пользователя
    * `POST /rule` — создать правило
    * `GET  /rule` — список правил
    * `DELETE /rule/{productId}` — удалить правило
    * `GET  /rule/stats` — статистика по срабатываниям
    * `POST /management/clear-caches` — очистка кэша
    * `GET  /management/info` — информация о сборке
* **Telegram‑бот:**
    * `/start` — приветственное сообщение
    * `/recommend <username>` — получить рекомендации
* **Кэширование:** Caffeine Cache для ускорения выборок.

## Технологии

*   Java
*   Spring Boot
*   Spring Data JPA
*   Spring Cache (Caffeine)
*   PostgreSQL (для хранения правил)
*   H2 (для данных транзакций/пользователей - используется `JdbcTemplate`)
*   TelegramBots API
*   Swagger/OpenAPI (для документации REST API - TBD)
*   Maven (система сборки)

## Документация

* **Requirements:** https://github.com/LoinRe/StarBank/wiki/Requirements
* **Traceability:** https://github.com/LoinRe/StarBank/wiki/Traceability
* **Architecture:** https://github.com/LoinRe/StarBank/wiki/Architecture
* **API (OpenAPI/Swagger):** https://github.com/LoinRe/StarBank/wiki/API
* **Deployment:** https://github.com/LoinRe/StarBank/wiki/Deployment

## Сборка и запуск

```bash
git clone https://github.com/LoinRe/StarBank.git
cd StarBank

# Сборка JAR
mvn clean package -DskipTests

# Запуск
java -jar target/starbank-0.1.0.jar
```
