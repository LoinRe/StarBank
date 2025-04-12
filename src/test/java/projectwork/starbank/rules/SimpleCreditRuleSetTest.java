package projectwork.starbank.rules;


import org.junit.jupiter.api.Test;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class SimpleCreditRuleSetTest {


    private final RecommendationRepository repo = mock(RecommendationRepository.class);
    private final SimpleCreditRuleSet ruleSet = new SimpleCreditRuleSet(repo);


    @Test
    void shouldReturnSimpleCreditRecommendation() {
        String userId = "user-eligible";

        when(repo.userHasProductType(userId, "CREDIT")).thenReturn(false);
        when(repo.getSumDepositsByProductType(userId, "DEBIT")).thenReturn(200000.0);
        when(repo.getSumWithdrawalsByProductType(userId, "DEBIT")).thenReturn(150000.0);

        Optional<RecommendationDto> result = ruleSet.elevator(userId);

        assertAll(
                () -> assertTrue(result.isPresent(), "Recommendation should be present"),
                () -> assertEquals("Простой кредит", result.get().getName())
        );
    }


    @Test
    void shouldNotReturnSimpleCreditIfConditionsFail() {
        String userId = "user-ineligible";


        when(repo.userHasProductType(userId, "CREDIT")).thenReturn(true); // У него уже есть кредит


        Optional<RecommendationDto> result = ruleSet.elevator(userId);


        assertFalse(result.isPresent());
    }
}

