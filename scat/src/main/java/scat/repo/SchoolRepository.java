package scat.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.data.School;

/**
 * @author levry
 */
public interface SchoolRepository extends DataRepository<School, Long>, JpaSpecificationExecutor<School> {
}
