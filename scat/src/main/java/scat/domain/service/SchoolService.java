package scat.domain.service;

import scat.domain.model.School;
import scat.domain.service.dto.SchoolInput;
import scat.web.search.SchoolSearch;

/**
 * @author levry
 */
public interface SchoolService {

    Iterable<School> findBy(SchoolSearch.SchoolCriteria criteria);

    School findOne(Long id);

    School save(SchoolInput input);

    School update(Long id, SchoolInput data);

    void delete(Long id);
}
