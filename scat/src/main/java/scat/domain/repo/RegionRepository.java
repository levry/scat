package scat.domain.repo;

import lombok.Data;
import scat.adapter.persistence.RegionJpaRepository;
import scat.domain.model.Region;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public interface RegionRepository {

    List<Region> findAllByNames(Collection<String> names);

    List<Region> findBy(RegionJpaRepository.RegionCriteria criteria);

    Optional<Region> findById(Integer id);

    Region save(Region region);

    void deleteById(Integer id);

    default Region findOne(Integer id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Data
    class RegionCriteria {

        private Integer id;
        private String name;
        private Integer country;
        private String country_name; // NOSONAR

    }
}
