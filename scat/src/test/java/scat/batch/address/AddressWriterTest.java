package scat.batch.address;

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
import scat.data.Country;
import scat.data.Region;
import scat.repo.CityRepository;
import scat.repo.CountryRepository;
import scat.repo.RegionRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AddressWriterTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AddressWriter addressWriter;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CityRepository cityRepository;

    private Entities entities;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
    }

    @Transactional
    @Test
    public void put_address() {
        List<Address> input = singletonList(address("Russia", "Central", "Moscow"));

        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();
        assertThat(city, notNullValue());
        assertThat(city.getName(), equalTo("Moscow"));
        assertThat(city.getCountry().getName(), equalTo("Russia"));
        assertThat(city.getRegion().getName(), equalTo("Central"));
    }

    @Transactional
    @Test
    public void put_address_without_region() {

        List<Address> input = singletonList(address("Russia", null, "Moscow"));

        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();
        assertThat(city, notNullValue());
        assertThat(city.getName(), equalTo("Moscow"));
        assertThat(city.getCountry().getName(), equalTo("Russia"));
        assertThat(city.getRegion(), nullValue());
    }

    @Transactional
    @Test
    public void put_address_with_exists_country() {
        Country country = entities.country("Germany");

        Collection<Address> input = singletonList(address("Germany", null, "Berlin"));
        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();

        assertThat(city, notNullValue());
        assertThat(city.getCountry(), is(country));
        assertThat(countryRepository.count(), is(1L));
    }

    @Transactional
    @Test
    public void put_address_with_exists_region() {
        Country country = entities.country("Canada");
        Region region = entities.region("Ontario", country);

        Collection<Address> input = singletonList(address("Canada", "Ontario", "Montreal"));
        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();

        assertThat(city, notNullValue());
        assertThat(city.getName(), is("Montreal"));
        assertThat(city.getRegion(), is(region));
        assertThat(regionRepository.count(), is(1L));
    }

    @Transactional
    @Test
    public void put_address_with_exists_city() {
        entities.city("Paris", "France");

        Collection<Address> input = singletonList(address("France", null, "Paris"));
        addressWriter.put(input);

        assertThat("Country not created if exists", countryRepository.count(), is(1L));
        assertThat("City not created if exists", cityRepository.count(), is(1L));
    }

    @Transactional
    @Test
    public void should_not_create_city_if_exists_similar() {
        entities.city("Beijing", "China");

        Collection<Address> input = singletonList(address("China", null, "BEIJING"));
        addressWriter.put(input);

        assertThat("City not created if exists similar name", cityRepository.count(), is(1L));
    }

    @Transactional
    @Test
    public void should_not_create_country_if_exists_similar() {
        entities.country("China");

        Collection<Address> input = singletonList(address("CHINA", null, "BEIJING"));
        addressWriter.put(input);

        assertThat("Country not created if exists similar name", countryRepository.count(), is(1L));
    }

    @Transactional
    @Test
    public void put_multiple_addresses() {
        Collection<Address> input = Arrays.asList(
                address("Russia", "Ural", "Omsk"),
                address("Germany", "Bavaria", "Bremen"),
                address("Germany", "Bavaria", "Gamburg")
        );

        addressWriter.put(input);

        assertThat(countryRepository.count(), is(2L));
        assertThat(regionRepository.count(), is(2L));
        assertThat(cityRepository.count(), is(3L));
    }

    private Address address(String country, String region, String city) {
        Address address = new Address();
        address.setCountry(country);
        address.setRegion(region);
        address.setCity(city);
        return address;
    }

}