package scat.domain.repo;

import lombok.Data;
import scat.domain.model.City;
import scat.domain.model.School;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public interface SchoolRepository {

    List<School> findBy(SchoolCriteria criteria);

    Optional<School> findById(Long id);

    School save(School school);

    void deleteById(Long id);

    boolean hasSchool(String name, City city);

    default School findOne(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Data
    class SchoolCriteria {

        private Long id;
        private String name;
        private Integer number;
        private Integer type;
        private String type_name; // NOSONAR
        private Long city;
        private String city_name; // NOSONAR
        private Integer country;
        private String country_name; // NOSONAR
        private Integer region;
        private String region_name; // NOSONAR

    }
}
