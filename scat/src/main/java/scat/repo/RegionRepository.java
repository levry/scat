package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import scat.data.Region;

import java.util.Optional;

/**
 * @author levry
 */
public interface RegionRepository extends JpaRepository<Region, Integer> {
//public interface RegionRepository extends CrudRepository<Region, Integer> {
}
