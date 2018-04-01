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
import scat.data.Country;
import scat.data.Region;
import scat.repo.RegionRepository;

import javax.persistence.EntityManager;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static scat.web.search.RegionSearch.*;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegionSearchTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RegionRepository repository;

    private Entities entities;

    private RegionSearch search;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
        search = new RegionSearch(repository);
    }

    @Transactional
    @Test
    public void find_by_name_starts_with() {

        Country russia = entities.country("Russia");
        Region ural = entities.region("Ural", russia);
        entities.region("Syberia", russia);
        entities.region("Crimea", russia);


        RegionCriteria criteria = new RegionCriteria();
        criteria.setName("u");
        List<Region> regions = search.findBy(criteria);

        assertThat(regions.size(), is(1));
        assertThat(regions, hasItems(ural));
    }

    @Transactional
    @Test
    public void find_by_country_name_starts_with() {

        Country russia = entities.country("Russia");
        Country germany = entities.country("Germany");

        entities.region("Ural", russia);
        Region bayern = entities.region("Bayern", germany);


        RegionCriteria params = new RegionCriteria();
        params.setCountry_name("g");

        List<Region> regions = search.findBy(params);


        assertThat(regions.size(), is(1));
        assertThat(regions, hasItems(bayern));
    }

    @Transactional
    @Test
    public void find_by_id() {
        Country russia = entities.country("Russia");
        Region ural = entities.region("Ural", russia);
        entities.region("Syberia", russia);
        entities.region("Crimea", russia);

        RegionCriteria params = new RegionCriteria();
        params.setId(ural.getId());
        List<Region> regions = search.findBy(params);

        assertThat(regions.size(), is(1));
        assertThat(regions, hasItems(ural));
    }

    @Transactional
    @Test
    public void find_by_country_id() {
        Country russia = entities.country("Russia");
        Country germany = entities.country("Germany");

        entities.region("Ural", russia);
        Region bayern = entities.region("Bayern", germany);

        RegionCriteria params = new RegionCriteria();
        params.setCountry(germany.getId());
        List<Region> regions = search.findBy(params);

        assertThat(regions.size(), is(1));
        assertThat(regions, hasItems(bayern));
    }
}