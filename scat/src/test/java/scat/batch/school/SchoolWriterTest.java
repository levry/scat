package scat.batch.school;

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
import scat.data.School;
import scat.data.SchoolType;
import scat.repo.CityRepository;
import scat.repo.SchoolRepository;
import scat.repo.SchoolTypeRepository;

import javax.persistence.EntityManager;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SchoolWriterTest {

    @Autowired
    private SchoolWriter schoolWriter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SchoolTypeRepository schoolTypeRepository;

    private Entities entities;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
    }

    @Transactional
    @Test
    public void put_school() {

        City city = entities.city("Tomsk", "Siberia", "Russia");

        Iterable<SchoolData> input = singletonList(data()
                .country("Russia")
                .region("Siberia")
                .city("Tomsk")
                .type("University")
                .name("Tomsk University")
                .number(21)
                .data
        );


        SchoolWriteResult result = schoolWriter.put(input);


        School school = schoolRepository.findAll().iterator().next();
        assertThat(school.getName(), is("Tomsk University"));
        assertThat(school.getNumber(), is(21));
        assertThat(school.getType().getName(), is("University"));
        assertThat(school.getCity(), is(city));
        assertThat(result.getSchoolsAdded(), is(1L));
        assertThat(result.getTypesAdded(), is(1L));
    }

    @Transactional
    @Test
    public void should_be_ignore_case_names_in_city() {
        City city = entities.city("Tomsk", "Siberia", "Russia");

        Iterable<SchoolData> input = singletonList(data()
                .country("RUSSIA")
                .region("SIBERIA")
                .city("TOMSK")
                .type("University")
                .name("Tomsk University")
                .number(21)
                .data
        );


        SchoolWriteResult result = schoolWriter.put(input);


        School school = schoolRepository.findAll().iterator().next();
        assertThat("Schould be find exists city", school.getCity(), is(city));
        assertThat("City not created if exists similar", cityRepository.count(), is(1L));
        assertThat("Result should be shools-added", result.getSchoolsAdded(), is(1L));
    }

    @Transactional
    @Test
    public void should_be_ignore_case_name_in_type() {

        entities.city("Tomsk", "Siberia", "Russia");
        SchoolType schoolType = entities.schoolType("University");

        Iterable<SchoolData> input = singletonList(data()
                .country("Russia")
                .region("Siberia")
                .city("Tomsk")
                .type("UNIVERSITY")
                .name("Tomsk University")
                .number(21)
                .data
        );


        SchoolWriteResult result = schoolWriter.put(input);


        School school = schoolRepository.findAll().iterator().next();
        assertThat(school.getType(), is(schoolType));
        assertThat("Type not created if exists similar", schoolTypeRepository.count(), is(1L));
        assertThat("Result should be shools-added", result.getTypesAdded(), is(0L));
    }

    @Transactional
    @Test
    public void should_not_create_school_if_city_not_found() {

        Iterable<SchoolData> input = singletonList(data()
                .country("Russia")
                .region("Siberia")
                .city("Tomsk")
                .type("University")
                .name("Tomsk University")
                .number(21)
                .data
        );


        SchoolWriteResult result = schoolWriter.put(input);

        assertThat(schoolRepository.count(), is(0L));
        assertThat("School should not created for missed cities", result.getCitiesMissed(), is(1L));
    }

    private static SchoolDataBuilder data() {
        return new SchoolDataBuilder();
    }

    private static class SchoolDataBuilder {
        private final SchoolData data;

        private SchoolDataBuilder() {
            this.data = new SchoolData();
        }

        SchoolDataBuilder country(String name) {
            data.setCountry(name);
            return this;
        }

        SchoolDataBuilder region(String name) {
            data.setRegion(name);
            return this;
        }

        SchoolDataBuilder city(String name) {
            data.setCity(name);
            return this;
        }

        SchoolDataBuilder type(String name) {
            data.setType(name);
            return this;
        }

        SchoolDataBuilder name(String name) {
            data.setName(name);
            return this;
        }

        SchoolDataBuilder number(Integer number) {
            data.setNumber(number);
            return this;
        }
    }
}