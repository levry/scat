package scat.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import scat.domain.model.SchoolType;
import scat.adapter.persistence.search.SchoolTypeSearch;
import scat.domain.repo.SchoolTypeRepository;

import java.util.List;

/**
 * @author levry
 */
public interface SchoolTypeJpaRepository extends JpaRepository<SchoolType, Integer>, SchoolTypeRepository {

    @Override
    default List<SchoolType> findBy(SchoolType params) {
        return new SchoolTypeSearch(this).findBy(params);
    }
}
