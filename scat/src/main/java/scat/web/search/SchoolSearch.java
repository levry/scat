package scat.web.search;

import lombok.Setter;
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

    @Setter
    public static class SchoolCriteria {

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
