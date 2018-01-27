package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.data.City;

/**
 * @author levry
 */
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {

}
