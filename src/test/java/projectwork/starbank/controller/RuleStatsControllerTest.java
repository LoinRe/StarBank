package projectwork.starbank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import projectwork.starbank.model.RuleStats;
import projectwork.starbank.service.RuleStatsService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RuleStatsController.class)
class RuleStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleStatsService statsService;

    @Test
    // Проверить, что API /rule/stats корректно возвращает статистику.
    void shouldReturnRuleStats() throws Exception {
        when(statsService.getAllStats()).thenReturn(List.of(new RuleStats("rule-1", 5)));

        mockMvc.perform(get("/rule/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats[0].rule_id").value("rule-1"))
                .andExpect(jsonPath("$.stats[0].count").value(5));
    }
}
