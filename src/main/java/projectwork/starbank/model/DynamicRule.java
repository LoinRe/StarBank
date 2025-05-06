package projectwork.starbank.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Сущность JPA, представляющая динамическое правило рекомендации.
 * Динамические правила хранятся в базе данных и могут быть добавлены/изменены во время работы приложения.
 */
@Entity
@Table(name = "dynamic_rule")
public class DynamicRule {

    /**
     * Уникальный идентификатор правила (генерируется базой данных).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор продукта, к которому относится правило (например, "CREDIT_CARD_STANDARD").
     * Используется для связи с продуктом и для статистики {@link RuleStats}.
     */
    private String productId;

    /**
     * Название рекомендуемого продукта.
     */
    private String productName;

    /**
     * Текстовое описание рекомендуемого продукта (для отображения пользователю).
     */
    private String productText;

    /**
     * Само правило в виде строки MVEL (или другого языка выражений),
     * которое будет выполняться для определения, подходит ли рекомендация пользователю.
     * Хранится в базе данных в формате JSON.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    private String rule;


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

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
