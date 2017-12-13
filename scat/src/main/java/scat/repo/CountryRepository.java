package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import scat.data.Country;

/**
 * @author levry
 */
public interface CountryRepository extends JpaRepository<Country, Integer> {
}
