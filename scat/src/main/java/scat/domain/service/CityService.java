package scat.domain.service;

import scat.domain.model.City;
import scat.domain.service.dto.CityInput;
import scat.web.search.CitySearch;

/**
 * @author levry
 */
public interface CityService {
    Iterable<City> findBy(CitySearch.CityCriteria criteria);

    City findOne(Long id);

    City save(CityInput data);

    City update(Long id, CityInput data);

    void remove(Long id);
}
