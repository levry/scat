package scat.domain.repo;

import scat.domain.model.SchoolType;

import java.util.Arrays;
import java.util.List;

/**
 * @author levry
 */
public interface SchoolTypeRepository {
    List<SchoolType> findBy(SchoolType params);

    SchoolType findOne(Integer id);

    SchoolType save(SchoolType type);

    SchoolType getOne(Integer id);

    void deleteById(Integer id);

    List<SchoolType> findAll();
}
