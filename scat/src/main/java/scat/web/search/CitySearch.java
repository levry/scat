package scat.web.search;

import lombok.Setter;
import scat.data.City;
import scat.repo.CityRepository;
import scat.repo.support.SpecificationBuilder;

import java.util.List;

import static javax.persistence.criteria.JoinType.LEFT;

/**
 * @author levry
 */
public class CitySearch {

    private final CityRepository repository;

    public CitySearch(CityRepository repository) {
        this.repository = repository;
    }

    public List<City> findBy(CityCriteria criteria) {

        SpecificationBuilder<City> spec = new SpecificationBuilder<>();
        spec.notNulls(city -> {
            city.eq("id", criteria.id);
            city.ilike("name", criteria.name);
        });
        spec.fetch("country", countrySpec ->
            countrySpec.notNulls(country -> {
                country.eq("id", criteria.country);
                country.ilike("name", criteria.country_name);
            })
        );
        spec.fetch("region", LEFT, regionSpec -> regionSpec
            .notNulls(region -> {
                region.eq("id", criteria.region);
                region.ilike("name", criteria.region_name);
        }));

        return repository.findAll(spec);
    }

    @Setter
    public static class CityCriteria {

        private Long id;
        private String name;
        private Integer country;
        private String country_name;
        private Integer region;
        private String region_name;

    }
}
