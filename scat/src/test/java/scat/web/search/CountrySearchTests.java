package scat.web.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.TestConfig;
import scat.data.Country;
import scat.repo.CountryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author levry
 */
@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class CountrySearchTests {

    @Autowired
    private CountryRepository repository;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        Country russia = entities.country("Russia");
        Country rumania = entities.country("Rumania");
        entities.country("Germany");

        Country country = new Country();
        country.setName("Ru");

        List<Country> countries = repository.findBy(country);

        assertThat(countries).containsExactlyInAnyOrder(russia, rumania);
    }

    @Transactional
    @Test
    void find_by_id() {
        Country russia = entities.country("Russia");
        entities.country("Rumania");
        entities.country("Germany");

        Country country = new Country();
        country.setId(russia.getId());

        List<Country> countries = repository.findBy(country);

        assertThat(countries).containsExactlyInAnyOrder(russia);
    }
}