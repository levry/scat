package scat.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import scat.domain.model.Region;
import scat.adapter.persistence.search.RegionSearch;
import scat.domain.repo.RegionRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author levry
 */
public interface RegionJpaRepository extends JpaRepository<Region, Integer>, JpaSpecificationExecutor<Region>, RegionRepository {

    @Override
    @Query("SELECT r FROM Region r JOIN r.country c WHERE LOWER(CONCAT(c.name, ' ', r.name)) IN (?1)")
    List<Region> findAllByNames(Collection<String> names);

    @Override
    default List<Region> findBy(RegionCriteria criteria) {
        return new RegionSearch(this).findBy(criteria);
    }

}
