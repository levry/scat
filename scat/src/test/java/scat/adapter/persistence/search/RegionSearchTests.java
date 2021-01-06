package scat.adapter.persistence.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.TestConfig;
import scat.adapter.persistence.RegionJpaRepository;
import scat.domain.model.Country;
import scat.domain.model.Region;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static scat.adapter.persistence.RegionJpaRepository.RegionCriteria;

/**
 * @author levry
 */
@DataJpaTest
@Import(TestConfig.class)
class RegionSearchTests {

    @Autowired
    private RegionJpaRepository repository;

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