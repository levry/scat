package scat.repo;

import scat.data.SchoolType;
import scat.web.search.SchoolTypeSearch;

import java.util.List;

/**
 * @author levry
 */
public interface SchoolTypeRepository extends DataRepository<SchoolType, Integer> {

    default List<SchoolType> findBy(SchoolType params) {
        return new SchoolTypeSearch(this).findBy(params);
    }
}
