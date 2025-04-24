package projectwork.starbank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.info.BuildProperties;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagementController.class)
class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private BuildProperties buildProperties;

    @Test
    //Проверить, что вызов /management/clear-caches проходит успешно.
    void shouldClearCaches() throws Exception {
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());
    }

    @Test
    // Проверить, что /management/info возвращает корректные данные.
    void shouldReturnServiceInfo() throws Exception {
        when(buildProperties.getName()).thenReturn("StarBank");
        when(buildProperties.getVersion()).thenReturn("1.0.0");

        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("StarBank"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }
}
