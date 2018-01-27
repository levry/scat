package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import scat.data.Region;

import java.util.Collection;
import java.util.List;

/**
 * @author levry
 */
public interface RegionRepository extends JpaRepository<Region, Integer>, JpaSpecificationExecutor<Region> {

    @Query("SELECT r FROM Region r JOIN r.country c WHERE LOWER(CONCAT(c.name, ' ', r.name)) IN (?1)")
    List<Region> findAllByNames(Collection<String> names);

}
