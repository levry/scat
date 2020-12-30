package scat.domain.service;

import scat.data.Region;
import scat.domain.service.dto.RegionInput;
import scat.web.search.RegionSearch;

/**
 * @author levry
 */
public interface RegionService {
    Iterable<Region> findBy(RegionSearch.RegionCriteria criteria);

    Region findOne(Integer id);

    Region save(RegionInput data);

    Region update(Integer id, RegionInput data);

    void remove(Integer id);
}
