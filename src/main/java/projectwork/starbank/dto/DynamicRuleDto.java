package projectwork.starbank.dto;

import java.util.List;
import java.util.Map;

public class DynamicRuleDto {
    private Long id;
    private String productId;
    private String productName;
    private String productText;
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

