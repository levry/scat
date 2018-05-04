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
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * @author levry
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SchoolTypeSearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SchoolTypeRepository repository;

    private Entities entities;

    private SchoolTypeSearch search;

    @BeforeEach
    void setUp() {
        entities = new Entities(entityManager);
        search = new SchoolTypeSearch(repository);
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        SchoolType academy = entities.schoolType("Academy");
        entities.schoolType("Colledge");
        entities.schoolType("University");

        SchoolType type = new SchoolType();
        type.setName("a");
        List<SchoolType> types = search.findBy(type);

        assertThat(types.size(), is(1));
        assertThat(types, hasItems(academy));
    }

    @Transactional
    @Test
    void find_by_id() {

        SchoolType academy = entities.schoolType("Academy");
        entities.schoolType("Colledge");

        SchoolType type = new SchoolType();
        type.setId(academy.getId());
        List<SchoolType> types = search.findBy(type);

        assertThat(types.size(), is(1));
        assertThat(types, hasItems(academy));
    }
}