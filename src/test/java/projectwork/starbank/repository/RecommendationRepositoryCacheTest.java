package projectwork.starbank.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RecommendationRepositoryCacheTest {

    @Autowired
    private RecommendationRepository repository;

    @Test
    void testCachingOfGetSumByTransactionType() {
        String userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"; // ✅ валидный UUID
        String productType = "DEBIT";
        String transactionType = "DEPOSIT";

        long start1 = System.nanoTime();
        double first = repository.getSumByTransactionType(userId, productType, transactionType);
        long duration1 = System.nanoTime() - start1;

        long start2 = System.nanoTime();
        double second = repository.getSumByTransactionType(userId, productType, transactionType);
        long duration2 = System.nanoTime() - start2;

        System.out.println("⏱ Первый вызов: " + duration1 / 1_000_000 + " ms");
        System.out.println("⏱ Второй вызов: " + duration2 / 1_000_000 + " ms");

        assertEquals(first, second, 0.0001);
        assertTrue(duration2 < duration1, "Ожидалось, что второй вызов будет быстрее (из кэша)");
    }
}
