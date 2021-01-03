package scat.domain.repo;

import scat.domain.model.SchoolType;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public interface SchoolTypeRepository {
    List<SchoolType> findBy(SchoolType params);

    Optional<SchoolType> findById(Integer id);

    SchoolType save(SchoolType type);

    void deleteById(Integer id);

    List<SchoolType> findAll();

    default SchoolType findOne(Integer id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
