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
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author levry
 */
@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class SchoolTypeSearchTests {

    @Autowired
    private SchoolTypeRepository repository;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        SchoolType academy = entities.schoolType("Academy");
        entities.schoolType("Colledge");
        entities.schoolType("University");

        SchoolType type = new SchoolType();
        type.setName("a");
        List<SchoolType> types = repository.findBy(type);


        assertThat(types).containsExactlyInAnyOrder(academy);
    }

    @Transactional
    @Test
    void find_by_id() {

        SchoolType academy = entities.schoolType("Academy");
        entities.schoolType("Colledge");

        SchoolType type = new SchoolType();
        type.setId(academy.getId());
        List<SchoolType> types = repository.findBy(type);

        assertThat(types).containsExactlyInAnyOrder(academy);
    }
}