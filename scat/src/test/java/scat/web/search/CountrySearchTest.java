package scat.web.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.data.Country;
import scat.repo.CountryRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * @author levry
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CountrySearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    private Entities entities;

    private CountrySearch search;

    @BeforeEach
    void setUp() {
        entities = new Entities(entityManager);
        search = new CountrySearch(countryRepository);
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        Country russia = entities.country("Russia");
        Country rumania = entities.country("Rumania");
        entities.country("Germany");

        Country country = new Country();
        country.setName("Ru");

        List<Country> countries = search.findBy(country);

        assertThat(countries.size(), is(2));
        assertThat(countries, hasItems(russia, rumania));
    }

    @Transactional
    @Test
    void find_by_id() {
        Country russia = entities.country("Russia");
        entities.country("Rumania");
        entities.country("Germany");

        Country country = new Country();
        country.setId(russia.getId());

        List<Country> countries = search.findBy(country);

        assertThat(countries.size(), is(1));
        assertThat(countries, hasItems(russia));
    }
}