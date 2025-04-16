package projectwork.starbank.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class RecommendationRepositoryTest {


    @Autowired
    private RecommendationRepository repository;


    UUID userInvest = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d"); // Invest500
    UUID userSaving = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b"); // TopSaving
    UUID userCredit = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a"); // SimpleCredit


    // --- Invest500 ---
    @Test
    void shouldMatchInvest500Conditions() {
        assertTrue(repository.userHasProductType(userInvest.toString(), "DEBIT"));
        assertFalse(repository.userHasProductType(userInvest.toString(), "INVEST"));
        assertTrue(repository.getSumDepositsByProductType(userInvest.toString(), "SAVING") > 1000);
    }


    // --- TopSaving ---
    @Test
    void shouldMatchTopSavingConditions() {
        double debitDeposits = repository.getSumDepositsByProductType(userSaving.toString(), "DEBIT");
        double savingDeposits = repository.getSumDepositsByProductType(userSaving.toString(), "SAVING");
        double debitWithdraw = repository.getSumWithdrawalsByProductType(userSaving.toString(), "DEBIT");


        assertTrue(repository.userHasProductType(userSaving.toString(), "DEBIT"));
        assertTrue(debitDeposits >= 50000 || savingDeposits >= 50000);
        assertTrue(debitDeposits > debitWithdraw);
    }


    // --- SimpleCredit ---
    @Test
    void shouldMatchSimpleCreditConditions() {
        assertFalse(repository.userHasProductType(userCredit.toString(), "CREDIT"));


        double debitDeposits = repository.getSumDepositsByProductType(userCredit.toString(), "DEBIT");
        double debitWithdraw = repository.getSumWithdrawalsByProductType(userCredit.toString(), "DEBIT");


        assertTrue(debitDeposits > debitWithdraw);
        assertTrue(debitWithdraw > 100_000);
    }
}

