package scat.adapter.persistence.search;

import lombok.RequiredArgsConstructor;
import scat.domain.model.School;
import scat.adapter.persistence.SchoolJpaRepository;
import scat.adapter.persistence.support.SpecificationBuilder;

import java.util.List;

/**
 * @author levry
 */
@RequiredArgsConstructor
public class SchoolSearch {

    private final SchoolJpaRepository repository;

    public List<School> findBy(SchoolJpaRepository.SchoolCriteria criteria) {

        SpecificationBuilder<School> builder = new SpecificationBuilder<>();

        builder.notNulls(school -> {
            school.eq("id", criteria.getId());
            school.ilike("name", criteria.getName());
            school.eq("number", criteria.getNumber());
        });
        builder.fetch("type", typeSpec ->
            typeSpec.notNulls(type -> {
                type.eq("id", criteria.getType());
                type.ilike("name", criteria.getType_name());
        }));

        builder.fetch("city", citySpec -> {
            citySpec.notNulls(city -> {
                city.eq("id", criteria.getCity());
                city.ilike("name", criteria.getCity_name());
            });

            citySpec.fetch("country", countrySpec ->
                countrySpec.notNulls(country -> {
                    country.eq("id", criteria.getCountry());
                    country.ilike("name", criteria.getCountry_name());
                })
            );

            citySpec.leftFetch("region", regionSpec ->
                regionSpec.notNulls(region -> {
                    region.eq("id", criteria.getRegion());
                    region.ilike("name", criteria.getRegion_name());
                })
            );

        });

        return repository.findAll(builder);
    }

}
