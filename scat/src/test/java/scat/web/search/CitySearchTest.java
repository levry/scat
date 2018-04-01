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
import scat.data.City;
import scat.repo.CityRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static scat.web.search.CitySearch.CityCriteria;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CitySearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CityRepository repository;

    private Entities entities;

    private CitySearch search;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
        search = new CitySearch(repository);
    }

    @Transactional
    @Test
    public void find_by_name_starts_with() {

        City city = entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria params = new CityCriteria();
        params.setName("E");

        List<City> cities = search.findBy(params);

        assertThat(cities.size(), is(1));
        assertThat(cities, hasItems(city));
    }

    @Transactional
    @Test
    public void find_by_id() {

        City city = entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria params = new CityCriteria();
        params.setId(city.getId());

        List<City> cities = search.findBy(params);

        assertThat(cities.size(), is(1));
        assertThat(cities, hasItems(city));
    }

    @Transactional
    @Test
    public void find_by_country_name() {

        City ekat = entities.city("Ekaterinburg", "Russia");
        City moscow = entities.city("Moscow", "Russia");
        entities.city("London", "United Kindom");

        CityCriteria params = new CityCriteria();
        params.setCountry_name("Ru");

        List<City> cities = search.findBy(params);

        assertThat(cities.size(), is(2));
        assertThat(cities, hasItems(ekat, moscow));
    }

    @Transactional
    @Test
    public void find_by_country_id() {
        entities.city("Ekaterinburg", "Russia");
        entities.city("Moscow", "Russia");
        City berlin = entities.city("Berlin", "Germany");

        CityCriteria params = new CityCriteria();
        params.setCountry(berlin.getCountry().getId());

        List<City> cities = search.findBy(params);

        assertThat(cities.size(), is(1));
        assertThat(cities, hasItems(berlin));
    }

    @Transactional
    @Test
    public void find_by_region_name() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria params = new CityCriteria();
        params.setRegion_name("Ur");

        List<City> cities = search.findBy(params);

        assertThat(cities.size(), is(1));
        assertThat(cities, hasItems(ekat));
    }

    @Transactional
    @Test
    public void find_by_region_id() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        entities.city("Moscow", "Russia");

        CityCriteria params = new CityCriteria();
        params.setRegion(ekat.getRegion().getId());

        List<City> cities = search.findBy(params);

        assertThat(cities.size(), is(1));
        assertThat(cities, hasItems(ekat));
    }

}