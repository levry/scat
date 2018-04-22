package scat.web.search;

import org.springframework.stereotype.Component;
import scat.data.School;
import scat.repo.SchoolRepository;
import scat.repo.support.SpecificationBuilder;

import java.util.List;

/**
 * @author levry
 */
@Component
public class SchoolSearch {

    private final SchoolRepository repository;

    public SchoolSearch(SchoolRepository repository) {
        this.repository = repository;
    }

    public List<School> findBy(SchoolCriteria criteria) {

        SpecificationBuilder<School> builder = new SpecificationBuilder<>();

        builder.notNulls(school -> {
            school.eq("id", criteria.id);
            school.ilike("name", criteria.name);
            school.eq("number", criteria.number);
        });
        builder.fetch("type", typeSpec ->
            typeSpec.notNulls(type -> {
                type.eq("id", criteria.type);
                type.ilike("name", criteria.type_name);
        }));

        builder.fetch("city", citySpec -> {
            citySpec.notNulls(city -> {
                city.eq("id", criteria.city);
                city.ilike("name", criteria.city_name);
            });

            citySpec.fetch("country", countrySpec ->
                countrySpec.notNulls(country -> {
                    country.eq("id", criteria.country);
                    country.ilike("name", criteria.country_name);
                })
            );

            citySpec.leftFetch("region", regionSpec ->
                regionSpec.notNulls(region -> {
                    region.eq("id", criteria.region);
                    region.ilike("name", criteria.region_name);
                })
            );

        });

        return repository.findAll(builder);
    }

    public static class SchoolCriteria {

        private Long id;
        private String name;
        private Integer number;
        private Integer type;
        private String type_name;
        private Long city;
        private String city_name;
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

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public Long getCity() {
            return city;
        }

        public void setCity(Long city) {
            this.city = city;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
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

    }
}
