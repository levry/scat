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
import scat.data.City;
import scat.repo.CityRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static scat.web.search.CitySearch.CityCriteria;

/**
 * @author levry
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CitySearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CityRepository repository;

    private Entities entities;

    private CitySearch search;

    @BeforeEach
    void setUp() {
        entities = new Entities(entityManager);
        search = new CitySearch(repository);
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        City city = entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setName("E");

        List<City> cities = search.findBy(criteria);

        assertThat(cities).containsExactly(city);
    }

    @Transactional
    @Test
    void find_by_id() {

        City city = entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setId(city.getId());

        List<City> cities = search.findBy(criteria);

        assertThat(cities).containsExactly(city);
    }

    @Transactional
    @Test
    void find_by_country_name() {

        City ekat = entities.city("Ekaterinburg", "Russia");
        City moscow = entities.city("Moscow", "Russia");
        entities.city("London", "United Kindom");

        CityCriteria criteria = new CityCriteria();
        criteria.setCountry_name("Ru");

        List<City> cities = search.findBy(criteria);

        assertThat(cities).containsExactlyInAnyOrder(ekat, moscow);
    }

    @Transactional
    @Test
    void find_by_country_id() {
        entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");
        City berlin = entities.city("Berlin", "Germany");

        CityCriteria criteria = new CityCriteria();
        criteria.setCountry(berlin.getCountry().getId());

        List<City> cities = search.findBy(criteria);

        assertThat(cities).containsExactly(berlin);
    }

    @Transactional
    @Test
    void find_by_region_name() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setRegion_name("Ur");

        List<City> cities = search.findBy(criteria);

        assertThat(cities).containsExactly(ekat);
    }

    @Transactional
    @Test
    void find_by_region_id() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setRegion(ekat.getRegion().getId());

        List<City> cities = search.findBy(criteria);

        assertThat(cities).containsExactly(ekat);
    }

}