package projectwork.starbank.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.service.RecommendationService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; ////пришлось у чаьта спрашивать


import java.util.List;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private RecommendationService recommendationService;


    @Test
    void shouldReturnRecommendationResponse() throws Exception {
        String userId = "test-user";
        List<RecommendationDto> recommendations = List.of(
                new RecommendationDto("1", "Invest 500", "Описание")
        );


        when(recommendationService.getRecommendations(userId)).thenReturn(recommendations);


        mockMvc.perform(get("/recommendation/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userId))
                .andExpect(jsonPath("$.recommendations[0].name").value("Invest 500"));
    }


    @Test
    void shouldReturnEmptyListIfNoRecommendations() throws Exception {
        String userId = "no-recs";


        when(recommendationService.getRecommendations(userId)).thenReturn(List.of());


        mockMvc.perform(get("/recommendation/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userId))
                .andExpect(jsonPath("$.recommendations").isEmpty());
    }
}

