package projectwork.starbank.dto;


/**
 * DTO для передачи информации о рекомендованном банковском продукте.
 * Содержит идентификатор, название и описание продукта.
 */
public class RecommendationDto {

    /**
     * Уникальный идентификатор рекомендованного продукта.
     * Например: {@code "CREDIT_CARD_PREMIUM"}.
     */
    private String id;
    /**
     * Название рекомендованного продукта.
     * Например: {@code "Премиальная кредитная карта"}.
     */
    private String name;
    /**
     * Текстовое описание или промо-текст для продукта.
     */
    private String text;

    /**
     * Конструирует DTO рекомендованного продукта.
     *
     * @param id   уникальный идентификатор продукта
     * @param name название продукта
     * @param text описание или промо‑текст продукта
     */
    public RecommendationDto(String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

}
