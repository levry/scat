package scat.web.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.data.Country;
import scat.repo.CountryRepository;

import javax.persistence.EntityManager;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CountrySearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    private Entities entities;

    private CountrySearch search;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
        search = new CountrySearch(countryRepository);
    }

    @Transactional
    @Test
    public void find_by_name_starts_with() {

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
    public void find_by_id() {
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