package projectwork.starbank.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO для представления динамического правила.
 * Используется для передачи данных между контроллером, сервисом и маппером.
 */
public class DynamicRuleDto {
    /**
     * Уникальный идентификатор правила (может быть null при создании нового правила).
     */
    private Long id;
    /**
     * Идентификатор продукта, к которому относится правило.
     */
    private String productId;
    /**
     * Название рекомендуемого продукта.
     */
    private String productName;
    /**
     * Текстовое описание рекомендуемого продукта.
     */
    private String productText;
    /**
     * Структурированное представление логики правила.
     * Список объектов Map, который будет сериализован в JSON
     * и сохранён в соответствующей сущности {@link projectwork.starbank.model.DynamicRule}.
     */
    private List<Map<String, Object>> rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<Map<String, Object>> getRule() {
        return rule;
    }

    public void setRule(List<Map<String, Object>> rule) {
        this.rule = rule;
    }
}
