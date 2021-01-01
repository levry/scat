package scat.domain.repo;

import lombok.Data;
import scat.domain.model.City;
import scat.domain.model.School;

import java.util.List;

/**
 * @author levry
 */
public interface SchoolRepository {

    List<School> findBy(SchoolCriteria criteria);

    School findOne(Long id);

    School save(School school);

    School getOne(Long id);

    void deleteById(Long id);

    boolean hasSchool(String name, City city);

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
