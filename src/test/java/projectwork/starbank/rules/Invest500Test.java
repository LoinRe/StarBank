package projectwork.starbank.rules;


import org.junit.jupiter.api.Test;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class Invest500Test {


    private final RecommendationRepository repo = mock(RecommendationRepository.class);
    private final invest500 ruleSet = new invest500(repo);


    @Test
    void shouldReturnInvest500Recommendation() {
        String userId = "user-test";


        when(repo.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repo.userHasProductType(userId, "INVEST")).thenReturn(false);
        when(repo.getSumDepositsByProductType(userId, "SAVING")).thenReturn(1500.0);


        Optional<RecommendationDto> result = ruleSet.elevator(userId);


        assertTrue(result.isPresent());
        assertEquals("Invest 500", result.get().getName());
    }


    @Test
    void shouldNotReturnInvest500IfConditionsFail() {
        String userId = "user-fail";


        when(repo.userHasProductType(userId, "DEBIT")).thenReturn(false); // одна из проверок не проходит


        Optional<RecommendationDto> result = ruleSet.elevator(userId);


        assertFalse(result.isPresent());
    }
}

