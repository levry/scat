package scat.adapter.persistence.search;

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
import scat.domain.model.School;
import scat.adapter.persistence.SchoolJpaRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static scat.adapter.persistence.SchoolJpaRepository.SchoolCriteria;

/**
 * @author levry
 */
@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class SchoolSearchTests {

    @Autowired
    private SchoolJpaRepository repository;

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
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setName("U");
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_id() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setId(urfu.getId());
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_type_name() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setType_name("Uni");
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_type_id() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setType(urfu.getType().getId());
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_city_name() {
        City ekat = entities.city("Ekaterinburg", "Russia");
        City moscow = entities.city("Moscow", "Russia");
        entities.school("School 4", "School", moscow);
        School urfu = entities.school("URFU", "University", ekat);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setCity_name("Eka");
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_city_id() {

        City ekat = entities.city("Ekaterinburg", "Russia");
        City moscow = entities.city("Moscow", "Russia");
        entities.school("School 4", "School", moscow);
        School urfu = entities.school("URFU", "University", ekat);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setCity(ekat.getId());
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_number() {
        City ekat = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", 4, "School", ekat);
        School urfu = entities.school("URFU", 432, "University", ekat);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setNumber(432);
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_region_id() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        City moscow = entities.city("Moscow", "Central", "Russia");
        School school = entities.school("School 4", "School", moscow);
        entities.school("URFU", "University", ekat);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setRegion(moscow.getRegion().getId());
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(school);
    }

    @Transactional
    @Test
    void find_by_region_name() {
        City ekat = entities.city("Ekaterinburg", "Ural", "Russia");
        City moscow = entities.city("Moscow", "Central", "Russia");
        entities.school("School 4", "School", moscow); // not found
        School urfu = entities.school("URFU", "University", ekat);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setRegion_name("ural");
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(urfu);
    }

    @Transactional
    @Test
    void find_by_country_id() {
        City ekat = entities.city("Ekaterinburg", "Russia");
        City berlin = entities.city("Berlin", "Germany");
        School school = entities.school("School 4", "School", berlin);
        entities.school("URFU", "University", ekat); // not found


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setCountry(berlin.getCountry().getId());
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(school);
    }

    @Transactional
    @Test
    void find_by_country_name() {
        City ekat = entities.city("Ekaterinburg", "Russia");
        City berlin = entities.city("Berlin", "Germany");
        School school = entities.school("School 4", "School", berlin);
        entities.school("URFU", "University", ekat); // not found


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setCountry_name("germany");
        List<School> schools = repository.findBy(criteria);


        assertThat(schools).containsExactlyInAnyOrder(school);
    }
}