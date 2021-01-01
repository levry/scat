package scat.adapter.persistence.search;

import scat.domain.model.Region;
import scat.adapter.persistence.RegionJpaRepository;
import scat.adapter.persistence.support.SpecificationBuilder;

import java.util.List;

/**
 * @author levry
 */
public class RegionSearch {

    private final RegionJpaRepository repository;

    public RegionSearch(RegionJpaRepository repository) {
        this.repository = repository;
    }

    public List<Region> findBy(RegionJpaRepository.RegionCriteria criteria) {

        SpecificationBuilder<Region> builder = new SpecificationBuilder<>();
        builder.notNulls(region -> {
            region.eq("id", criteria.getId());
            region.ilike("name", criteria.getName());
        });
        builder.fetch("country", countrySpec ->
            countrySpec.notNulls(country -> {
                country.eq("id", criteria.getCountry());
                country.ilike("name", criteria.getCountry_name());
        }));

        return repository.findAll(builder);
    }

}
