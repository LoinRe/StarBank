package projectwork.starbank.rules;


import org.junit.jupiter.api.Test;
import projectwork.starbank.dto.RecommendationDto;
import projectwork.starbank.repository.RecommendationRepository;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TopSavingRuleSetTest {
    private final RecommendationRepository repo = mock(RecommendationRepository.class);
    private final TopSavingRuleSet ruleSet = new TopSavingRuleSet(repo);

    @Test
    void shouldReturnTopSavingRecommendation() {
        String userId = "user-ok";

        when(repo.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repo.getSumDepositsByProductType(userId, "DEBIT")).thenReturn(60000.0);
        when(repo.getSumDepositsByProductType(userId, "SAVING")).thenReturn(0.0);
        when(repo.getSumWithdrawalsByProductType(userId, "DEBIT")).thenReturn(30000.0);

        Optional<RecommendationDto> result = ruleSet.elevator(userId);

        assertTrue(result.isPresent());
        assertEquals("Top Saving", result.get().getName());
    }

    @Test
    void shouldNotReturnTopSavingIfConditionsFail() {
        String userId = "user-bad";

        when(repo.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repo.getSumDepositsByProductType(userId, "DEBIT")).thenReturn(10000.0);
        when(repo.getSumDepositsByProductType(userId, "SAVING")).thenReturn(10000.0);
        when(repo.getSumWithdrawalsByProductType(userId, "DEBIT")).thenReturn(20000.0);

        Optional<RecommendationDto> result = ruleSet.elevator(userId);

        assertFalse(result.isPresent());
    }
}
