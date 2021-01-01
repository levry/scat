package scat.adapter.persistence.search;

import scat.domain.model.City;
import scat.adapter.persistence.CityJpaRepository;
import scat.domain.repo.CityRepository;
import scat.adapter.persistence.support.SpecificationBuilder;

import java.util.List;

import static javax.persistence.criteria.JoinType.LEFT;

/**
 * @author levry
 */
public class CitySearch {

    private final CityJpaRepository repository;

    public CitySearch(CityJpaRepository repository) {
        this.repository = repository;
    }

    public List<City> findBy(CityRepository.CityCriteria criteria) {

        SpecificationBuilder<City> spec = new SpecificationBuilder<>();
        spec.notNulls(city -> {
            city.eq("id", criteria.getId());
            city.ilike("name", criteria.getName());
        });
        spec.fetch("country", countrySpec ->
            countrySpec.notNulls(country -> {
                country.eq("id", criteria.getCountry());
                country.ilike("name", criteria.getCountry_name());
            })
        );
        spec.fetch("region", LEFT, regionSpec -> regionSpec
            .notNulls(region -> {
                region.eq("id", criteria.getRegion());
                region.ilike("name", criteria.getRegion_name());
        }));

        return repository.findAll(spec);
    }

}
