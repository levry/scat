package scat.domain.service;

import scat.domain.model.Country;
import scat.domain.service.dto.CountryInput;

/**
 * @author levry
 */
public interface CountryService {
    Iterable<Country> findBy(CountryInput criteria);

    Country findOne(Integer id);

    Country save(CountryInput data);

    Country update(Integer id, CountryInput data);

    void remove(Integer id);
}
