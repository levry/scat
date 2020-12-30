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
import scat.data.Country;
import scat.data.Region;
import scat.repo.RegionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static scat.web.search.RegionSearch.RegionCriteria;

/**
 * @author levry
 */
@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class RegionSearchTests {

    @Autowired
    private RegionRepository repository;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Transactional
    @Test
    void find_by_name_starts_with() {

        Country russia = entities.country("Russia");
        Region ural = entities.region("Ural", russia);
        entities.region("Syberia", russia);
        entities.region("Crimea", russia);


        RegionCriteria criteria = new RegionCriteria();
        criteria.setName("u");
        List<Region> regions = repository.findBy(criteria);

        assertThat(regions).containsExactlyInAnyOrder(ural);
    }

    @Transactional
    @Test
    void find_by_country_name_starts_with() {

        Country russia = entities.country("Russia");
        Country germany = entities.country("Germany");

        entities.region("Ural", russia);
        Region bayern = entities.region("Bayern", germany);


        RegionCriteria params = new RegionCriteria();
        params.setCountry_name("g");

        List<Region> regions = repository.findBy(params);


        assertThat(regions).containsExactlyInAnyOrder(bayern);
    }

    @Transactional
    @Test
    void find_by_id() {
        Country russia = entities.country("Russia");
        Region ural = entities.region("Ural", russia);
        entities.region("Syberia", russia);
        entities.region("Crimea", russia);


        RegionCriteria params = new RegionCriteria();
        params.setId(ural.getId());
        List<Region> regions = repository.findBy(params);


        assertThat(regions).containsExactlyInAnyOrder(ural);
    }

    @Transactional
    @Test
    void find_by_country_id() {
        Country russia = entities.country("Russia");
        Country germany = entities.country("Germany");

        entities.region("Ural", russia);
        Region bayern = entities.region("Bayern", germany);

        RegionCriteria params = new RegionCriteria();
        params.setCountry(germany.getId());
        List<Region> regions = repository.findBy(params);


        assertThat(regions).containsExactlyInAnyOrder(bayern);
    }
}