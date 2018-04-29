package scat.web.search;

import scat.data.Region;
import scat.repo.RegionRepository;
import scat.repo.support.SpecificationBuilder;

import java.util.List;

/**
 * @author levry
 */
public class RegionSearch {

    private final RegionRepository repository;

    public RegionSearch(RegionRepository repository) {
        this.repository = repository;
    }

    public List<Region> findBy(RegionCriteria criteria) {

        SpecificationBuilder<Region> builder = new SpecificationBuilder<>();
        builder.notNulls(region -> {
            region.eq("id", criteria.id);
            region.ilike("name", criteria.name);
        });
        builder.fetch("country", countrySpec ->
            countrySpec.notNulls(country -> {
                country.eq("id", criteria.country);
                country.ilike("name", criteria.country_name);
        }));

        return repository.findAll(builder);
    }

    public static class RegionCriteria {

        private Integer id;
        private String name;
        private Integer country;
        private String country_name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCountry() {
            return country;
        }

        public void setCountry(Integer country) {
            this.country = country;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }
    }
}
