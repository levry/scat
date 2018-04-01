package scat.web.search;

import org.springframework.util.StringUtils;
import scat.data.City;
import scat.repo.CityRepository;

import java.util.List;

/**
 * @author levry
 */
public class CitySearch {

    private final CityRepository repository;

    public CitySearch(CityRepository repository) {
        this.repository = repository;
    }

    public List<City> findBy(CityCriteria criteria) {

        SearchFilters<City> filters = new SearchFilters<>();
        filters.byIdAndName(criteria.id, criteria.name);
        if (criteria.hasCountryBy()) {
            filters.joinByIdAndName("country", criteria.country, criteria.country_name);
        }
        if (criteria.hasRegionBy()) {
            filters.joinByIdAndName("region", criteria.region, criteria.region_name);
        }

        return repository.findAll(filters.specification());
    }

    public static class CityCriteria {

        private Long id;
        private String name;
        private Integer country;
        private String country_name;
        private Integer region;
        private String region_name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
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

        public Integer getRegion() {
            return region;
        }

        public void setRegion(Integer region) {
            this.region = region;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        private boolean hasCountryBy() {
            return null != country || StringUtils.hasText(country_name);
        }

        private boolean hasRegionBy() {
            return null != region || StringUtils.hasText(region_name);
        }
    }
}
