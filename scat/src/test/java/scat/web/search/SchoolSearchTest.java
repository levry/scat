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
import scat.data.School;
import scat.repo.SchoolRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static scat.web.search.SchoolSearch.SchoolCriteria;

/**
 * @author levry
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SchoolSearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SchoolRepository repository;

    private Entities entities;

    private SchoolSearch search;

    @BeforeEach
    void setUp() {
        entities = new Entities(entityManager);
        search = new SchoolSearch(repository);
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setName("U");
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
    }

    @Transactional
    @Test
    void find_by_id() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setId(urfu.getId());
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
    }

    @Transactional
    @Test
    void find_by_type_name() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setType_name("Uni");
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
    }

    @Transactional
    @Test
    void find_by_type_id() {
        City city = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", "School", city);
        School urfu = entities.school("URFU", "University", city);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setType(urfu.getType().getId());
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
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
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
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
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
    }

    @Transactional
    @Test
    void find_by_number() {
        City ekat = entities.city("Ekaterinburg", "Russia");
        entities.school("School 4", 4, "School", ekat);
        School urfu = entities.school("URFU", 432, "University", ekat);


        SchoolCriteria criteria = new SchoolCriteria();
        criteria.setNumber(432);
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
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
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(school));
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
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(urfu));
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
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(school));
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
        List<School> schools = search.findBy(criteria);


        assertThat(schools.size(), is(1));
        assertThat(schools, hasItems(school));
    }
}