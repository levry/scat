package scat.batch.address;

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
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author levry
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AddressWriterTest {

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

    @BeforeEach
    void setUp() {
        entities = new Entities(entityManager);
    }

    @Transactional
    @Test
    void put_address() {
        List<Address> input = singletonList(address("Russia", "Central", "Moscow"));

        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();
        assertThat(city).isNotNull();
        assertThat(city.getName()).isEqualTo("Moscow");
        assertThat(city.getCountry().getName()).isEqualTo("Russia");
        assertThat(city.getRegion().getName()).isEqualTo("Central");
    }

    @Transactional
    @Test
    void put_address_without_region() {

        List<Address> input = singletonList(address("Russia", null, "Moscow"));

        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();
        assertThat(city).isNotNull();
        assertThat(city.getName()).isEqualTo("Moscow");
        assertThat(city.getCountry().getName()).isEqualTo("Russia");
        assertThat(city.getRegion()).isNull();
    }

    @Transactional
    @Test
    void put_address_with_exists_country() {
        Country country = entities.country("Germany");

        Collection<Address> input = singletonList(address("Germany", null, "Berlin"));
        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();

        assertThat(city).isNotNull();
        assertThat(city.getCountry()).isEqualTo(country);
        assertThat(countryRepository.count()).isEqualTo(1L);
    }

    @Transactional
    @Test
    void put_address_with_exists_region() {
        Country country = entities.country("Canada");
        Region region = entities.region("Ontario", country);

        Collection<Address> input = singletonList(address("Canada", "Ontario", "Montreal"));
        addressWriter.put(input);

        City city = cityRepository.findAll().iterator().next();

        assertThat(city).isNotNull();
        assertThat(city.getName()).isEqualTo("Montreal");
        assertThat(city.getRegion()).isEqualTo(region);
        assertThat(regionRepository.count()).isEqualTo(1L);
    }

    @Transactional
    @Test
    void put_address_with_exists_city() {
        entities.city("Paris", "France");

        Collection<Address> input = singletonList(address("France", null, "Paris"));
        addressWriter.put(input);

        assertThat(countryRepository.count()).describedAs("Country not created if exists").isEqualTo(1L);
        assertThat(cityRepository.count()).describedAs("City not created if exists").isEqualTo(1L);
    }

    @Transactional
    @Test
    void should_not_create_city_if_exists_similar() {
        entities.city("Beijing", "China");

        Collection<Address> input = singletonList(address("China", null, "BEIJING"));
        addressWriter.put(input);

        assertThat(cityRepository.count()).describedAs("City not created if exists similar name").isEqualTo(1L);
    }

    @Transactional
    @Test
    void should_not_create_country_if_exists_similar() {
        entities.country("China");

        Collection<Address> input = singletonList(address("CHINA", null, "BEIJING"));
        addressWriter.put(input);

        assertThat(countryRepository.count()).describedAs("Country not created if exists similar name").isEqualTo(1L);
    }

    @Transactional
    @Test
    void put_multiple_addresses() {
        Collection<Address> input = Arrays.asList(
                address("Russia", "Ural", "Omsk"),
                address("Germany", "Bavaria", "Bremen"),
                address("Germany", "Bavaria", "Gamburg")
        );

        addressWriter.put(input);

        assertThat(countryRepository.count()).isEqualTo(2L);
        assertThat(regionRepository.count()).isEqualTo(2L);
        assertThat(cityRepository.count()).isEqualTo(3L);
    }

    private Address address(String country, String region, String city) {
        return new Address(country, region, city);
    }

}