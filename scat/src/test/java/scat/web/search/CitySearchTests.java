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
import scat.domain.model.City;
import scat.repo.CityRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static scat.web.search.CitySearch.CityCriteria;

/**
 * @author levry
 */
@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class CitySearchTests {

    @Autowired
    private CityRepository repository;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        City city = entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setName("E");

        List<City> cities = repository.findBy(criteria);

        assertThat(cities).containsExactly(city);
    }

    @Transactional
    @Test
    void find_by_id() {

        City city = entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setId(city.getId());

        List<City> cities = repository.findBy(criteria);

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

        List<City> cities = repository.findBy(criteria);

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

        List<City> cities = repository.findBy(criteria);

        assertThat(cities).containsExactly(berlin);
    }

    @Transactional
    @Test
    void find_by_region_name() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setRegion_name("Ur");

        List<City> cities = repository.findBy(criteria);

        assertThat(cities).containsExactly(ekat);
    }

    @Transactional
    @Test
    void find_by_region_id() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria criteria = new CityCriteria();
        criteria.setRegion(ekat.getRegion().getId());

        List<City> cities = repository.findBy(criteria);

        assertThat(cities).containsExactly(ekat);
    }

}