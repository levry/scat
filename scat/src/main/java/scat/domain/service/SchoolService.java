package scat.domain.service;

import scat.domain.model.School;
import scat.adapter.persistence.SchoolJpaRepository;
import scat.domain.service.dto.SchoolInput;

/**
 * @author levry
 */
public interface SchoolService {

    Iterable<School> findBy(SchoolJpaRepository.SchoolCriteria criteria);

    School findOne(Long id);

    School save(SchoolInput input);

    School update(Long id, SchoolInput data);

    void delete(Long id);
}
