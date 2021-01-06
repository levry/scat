package scat.domain.service;

import scat.domain.model.City;
import scat.domain.repo.CityRepository;
import scat.domain.service.dto.CityInput;

/**
 * @author levry
 */
public interface CityService {
    Iterable<City> findBy(CityRepository.CityCriteria criteria);

    City findOne(Long id);

    City save(CityInput data);

    City update(Long id, CityInput data);

    void remove(Long id);
}
