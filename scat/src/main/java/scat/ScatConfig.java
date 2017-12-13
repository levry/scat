package scat;

import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import scat.web.support.json.EntityJsonDeserializer;
import scat.web.support.json.EntityJsonModule;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.Set;

/**
 * @author levry
 */
@Configuration
//@ImportAutoConfiguration(SpringDataWebAutoConfiguration.class)
//@EnableJpaRepositories("scat.repo")
public class ScatConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder(EntityManager entityManager) {
        return new Jackson2ObjectMapperBuilder()
                .modules(new EntityJsonModule(entityManager));
    }

    /*private JsonDeserializer<?>[] collectDeserializersForEntities(EntityManager entityManager) {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        return entities.stream()
                .map(entityType ->
                        new EntityJsonDeserializer(entityType, entityManager)
                )
                .toArray(JsonDeserializer[]::new);
    }*/

}
