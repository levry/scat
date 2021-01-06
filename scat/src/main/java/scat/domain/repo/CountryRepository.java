package scat.domain.repo;

import scat.domain.model.Country;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public interface CountryRepository {

    List<Country> findAllByNames(Collection<String> names);

    List<Country> findBy(Country params);

    Optional<Country> findById(Integer id);

    Country save(Country country);

    void deleteById(Integer id);

    default Country findOne(Integer id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
