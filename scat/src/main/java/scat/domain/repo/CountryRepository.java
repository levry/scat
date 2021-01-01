package scat.domain.repo;

import scat.domain.model.Country;

import java.util.Collection;
import java.util.List;

/**
 * @author levry
 */
public interface CountryRepository {

    List<Country> findAllByNames(Collection<String> names);

    List<Country> findBy(Country params);

    Country findOne(Integer id);

    Country save(Country country);

    Country getOne(Integer id);

    void deleteById(Integer id);
}
