package scat.domain.repo;

import lombok.Data;
import scat.domain.batch.school.SchoolData;
import scat.domain.model.City;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public interface CityRepository {

    List<City> findBy(CityCriteria criteria);

    Optional<City> findById(Long id);

    City save(City city);

    void deleteById(Long id);

    boolean hasCity(City city);

    Optional<City> findBy(SchoolData data);

    default City findOne(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Data
    class CityCriteria {

        private Long id;
        private String name;
        private Integer country;
        private String country_name; // NOSONAR
        private Integer region;
        private String region_name; // NOSONAR

    }
}
