package scat.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import scat.adapter.persistence.search.CitySearch;
import scat.domain.batch.school.SchoolData;
import scat.domain.model.City;
import scat.domain.repo.CityRepository;
import scat.adapter.persistence.support.SpecificationBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public interface CityJpaRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City>, CityRepository {

    @Override
    default City findOne(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    default Optional<City> findBy(SchoolData data) {
        SpecificationBuilder<City> spec = new SpecificationBuilder<>();
        spec.eq("name", data.getCity());
        spec.join("country", country -> country.eq("name", data.getCountry()));
        if (null != data.getRegion()) {
            spec.join("region", region -> region.eq("name", data.getRegion()));
        } else {
            spec.isNull("region");
        }
        return findOne(spec);
    }

    @Override
    default List<City> findBy(CityCriteria criteria) {
        return new CitySearch(this).findBy(criteria);
    }

    @Override
    default boolean hasCity(City city) {
        SpecificationBuilder<City> spec = new SpecificationBuilder<>();
        spec.eq("name", city.getName());
        spec.eq("country", city.getCountry());
        if(null != city.getRegion()) {
            spec.eq("region", city.getRegion());
        } else {
            spec.isNull("region");
        }
        return 0 != count(spec);
    }

}
