package scat;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

/**
 * @author levry
 */
@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {
    @Bean
    public Entities entities(EntityManager entityManager) {
        return new Entities(entityManager);
    }
}
