package scat.domain.repo;

import lombok.Data;
import scat.adapter.persistence.RegionJpaRepository;
import scat.domain.model.Region;

import java.util.Collection;
import java.util.List;

/**
 * @author levry
 */
public interface RegionRepository {

    List<Region> findAllByNames(Collection<String> names);

    List<Region> findBy(RegionJpaRepository.RegionCriteria criteria);

    Region findOne(Integer id);

    Region save(Region region);

    Region getOne(Integer id);

    void deleteById(Integer id);

    @Data
    class RegionCriteria {

        private Integer id;
        private String name;
        private Integer country;
        private String country_name; // NOSONAR

    }
}
