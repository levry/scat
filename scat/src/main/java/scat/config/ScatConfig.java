package scat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scat.support.json.EntityJsonModule;

import javax.persistence.EntityManager;

/**
 * @author levry
 */
@Configuration
public class ScatConfig {

    @Bean
    public EntityJsonModule entityJsonModule(EntityManager entityManager) {
        return new EntityJsonModule(entityManager);
    }

}
