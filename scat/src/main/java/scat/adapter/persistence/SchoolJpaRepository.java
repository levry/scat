package scat.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.adapter.persistence.search.SchoolSearch;
import scat.domain.model.City;
import scat.domain.model.School;
import scat.domain.repo.SchoolRepository;
import scat.adapter.persistence.support.SpecificationBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author levry
 */
public interface SchoolJpaRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School>, SchoolRepository {

    @Override
    default School findOne(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    default boolean hasSchool(String name, City city) {
        SpecificationBuilder<School> spec = new SpecificationBuilder<>();
        spec.eq("name", name);
        spec.eq("city", city);
        return 0 != count(spec);
    }

    @Override
    default List<School> findBy(SchoolCriteria criteria) {
        return new SchoolSearch(this).findBy(criteria);
    }

}
