package scat.repo;

import org.springframework.data.jpa.repository.Query;
import scat.data.Country;

import java.util.Collection;
import java.util.List;

/**
 * @author levry
 */
public interface CountryRepository extends DataRepository<Country, Integer> {

    @Query("SELECT c FROM Country c WHERE LOWER(c.name) in (?1)")
    List<Country> findAllByNames(Collection<String> names);

}
