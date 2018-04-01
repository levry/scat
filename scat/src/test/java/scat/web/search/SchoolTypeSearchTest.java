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
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SchoolTypeSearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SchoolTypeRepository repository;

    private Entities entities;

    private SchoolTypeSearch search;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
        search = new SchoolTypeSearch(repository);
    }

    @Transactional
    @Test
    public void find_by_name_starts_with() {

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
    public void find_by_id() {

        SchoolType academy = entities.schoolType("Academy");
        entities.schoolType("Colledge");

        SchoolType type = new SchoolType();
        type.setId(academy.getId());
        List<SchoolType> types = search.findBy(type);

        assertThat(types.size(), is(1));
        assertThat(types, hasItems(academy));
    }
}