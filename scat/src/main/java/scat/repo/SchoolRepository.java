package scat.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.data.School;
import scat.web.search.SchoolSearch;

import java.util.List;

/**
 * @author levry
 */
public interface SchoolRepository extends DataRepository<School, Long>, JpaSpecificationExecutor<School> {

    default List<School> findBy(SchoolSearch.SchoolCriteria criteria) {
        return new SchoolSearch(this).findBy(criteria);
    }
}
