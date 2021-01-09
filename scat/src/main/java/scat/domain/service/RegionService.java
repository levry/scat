package scat.domain.service;

import scat.domain.model.Region;
import scat.domain.service.dto.RegionInput;

import static scat.domain.repo.RegionRepository.*;

/**
 * @author levry
 */
public interface RegionService {
    Iterable<Region> findBy(RegionCriteria criteria);

    Region findOne(Integer id);

    Region save(RegionInput data);

    Region update(Integer id, RegionInput data);

    void remove(Integer id);
}
