package scat.domain.batch.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.TestConfig;
import scat.domain.model.City;
import scat.domain.model.School;
import scat.domain.model.SchoolType;
import scat.adapter.persistence.CityJpaRepository;
import scat.adapter.persistence.SchoolJpaRepository;
import scat.adapter.persistence.SchoolTypeJpaRepository;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author levry
 */
@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class SchoolWriterTests {

    @Autowired
    private SchoolWriter schoolWriter;

    @Autowired
    private SchoolJpaRepository schoolRepository;

    @Autowired
    private CityJpaRepository cityRepository;

    @Autowired
    private SchoolTypeJpaRepository schoolTypeRepository;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Transactional
    @Test
    void put_school() {

        City city = entities.city("Tomsk", "Siberia", "Russia");

        Iterable<SchoolData> input = singletonList(SchoolData.builder()
                .country("Russia")
                .region("Siberia")
                .city("Tomsk")
                .type("University")
                .name("Tomsk University")
                .number(21)
                .build()
        );


        SchoolWriteResult result = schoolWriter.put(input);


        School school = schoolRepository.findAll().iterator().next();

        assertThat(school.getName()).isEqualTo("Tomsk University");
        assertThat(school.getNumber()).isEqualTo(21);
        assertThat(school.getType().getName()).isEqualTo("University");
        assertThat(school.getCity()).isEqualTo(city);
        assertThat(result.getSchoolsAdded()).isEqualTo(1L);
        assertThat(result.getTypesAdded()).isEqualTo(1L);
    }

    @Transactional
    @Test
    void should_be_ignore_case_names_in_city() {
        City city = entities.city("Tomsk", "Siberia", "Russia");

        Iterable<SchoolData> input = singletonList(SchoolData.builder()
                .country("RUSSIA")
                .region("SIBERIA")
                .city("TOMSK")
                .type("University")
                .name("Tomsk University")
                .number(21)
                .build()
        );


        SchoolWriteResult result = schoolWriter.put(input);


        School school = schoolRepository.findAll().iterator().next();
        assertThat(school.getCity()).describedAs("Schould be find exists city").isEqualTo(city);
        assertThat(cityRepository.count()).describedAs("City not created if exists similar").isEqualTo(1L);
        assertThat(result.getSchoolsAdded()).describedAs("Result should be shools-added").isEqualTo(1L);
    }

    @Transactional
    @Test
    void should_be_ignore_case_name_in_type() {

        entities.city("Tomsk", "Siberia", "Russia");
        SchoolType schoolType = entities.schoolType("University");

        Iterable<SchoolData> input = singletonList(SchoolData.builder()
                .country("Russia")
                .region("Siberia")
                .city("Tomsk")
                .type("UNIVERSITY")
                .name("Tomsk University")
                .number(21)
                .build()
        );


        SchoolWriteResult result = schoolWriter.put(input);


        School school = schoolRepository.findAll().iterator().next();
        assertThat(school.getType()).isEqualTo(schoolType);
        assertThat(schoolTypeRepository.count()).describedAs("Type not created if exists similar").isEqualTo(1L);
        assertThat(result.getTypesAdded()).describedAs("Result should be shools-added").isEqualTo(0L);
    }

    @Transactional
    @Test
    void should_not_create_school_if_city_not_found() {

        Iterable<SchoolData> input = singletonList(SchoolData.builder()
                .country("Russia")
                .region("Siberia")
                .city("Tomsk")
                .type("University")
                .name("Tomsk University")
                .number(21)
                .build()
        );


        SchoolWriteResult result = schoolWriter.put(input);

        assertThat(schoolRepository.count()).isEqualTo(0L);
        assertThat(result.getCitiesMissed()).describedAs("School should not created for missed cities").isEqualTo(1L);
    }

}