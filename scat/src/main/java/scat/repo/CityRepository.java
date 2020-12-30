package scat.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.domain.model.City;
import scat.web.search.CitySearch;

import java.util.List;

/**
 * @author levry
 */
public interface CityRepository extends DataRepository<City, Long>, JpaSpecificationExecutor<City> {

    default List<City> findBy(CitySearch.CityCriteria criteria) {
        return new CitySearch(this).findBy(criteria);
    }

}
