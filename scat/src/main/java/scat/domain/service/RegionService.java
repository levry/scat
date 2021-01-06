package scat.domain.service;

import scat.domain.model.Region;
import scat.adapter.persistence.RegionJpaRepository;
import scat.domain.service.dto.RegionInput;

/**
 * @author levry
 */
public interface RegionService {
    Iterable<Region> findBy(RegionJpaRepository.RegionCriteria criteria);

    Region findOne(Integer id);

    Region save(RegionInput data);

    Region update(Integer id, RegionInput data);

    void remove(Integer id);
}
