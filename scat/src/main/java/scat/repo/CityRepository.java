package scat.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.data.City;

/**
 * @author levry
 */
public interface CityRepository extends DataRepository<City, Long>, JpaSpecificationExecutor<City> {

}
