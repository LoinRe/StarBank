package projectwork.starbank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectwork.starbank.repository.RecommendationRepository;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    private final RecommendationRepository repository;

    public TestController(RecommendationRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{userId}")
    public int testQuery(@PathVariable String userId) {
        return repository.getRandomTransactionAmount(UUID.fromString(userId));
    }
}
