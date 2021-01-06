package scat.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scat.adapter.persistence.search.CountrySearch;
import scat.domain.model.Country;
import scat.domain.repo.CountryRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author levry
 */
public interface CountryJpaRepository extends JpaRepository<Country, Integer>, CountryRepository {

    @Override
    @Query("SELECT c FROM Country c WHERE LOWER(c.name) in (?1)")
    List<Country> findAllByNames(Collection<String> names);

    @Override
    default List<Country> findBy(Country params) {
        return new CountrySearch(this).findBy(params);
    }
}
