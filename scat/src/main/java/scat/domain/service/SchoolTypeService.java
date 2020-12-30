package scat.domain.service;

import scat.data.SchoolType;
import scat.domain.service.dto.SchoolTypeInput;

/**
 * @author levry
 */
public interface SchoolTypeService {
    Iterable<SchoolType> findBy(SchoolTypeInput criteria);

    SchoolType findOne(Integer id);

    SchoolType save(SchoolTypeInput data);

    SchoolType update(Integer id, SchoolTypeInput data);

    void remove(Integer id);
}
