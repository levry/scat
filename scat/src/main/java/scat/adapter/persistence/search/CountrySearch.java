package scat.adapter.persistence.search;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import scat.domain.model.Country;
import scat.adapter.persistence.CountryJpaRepository;

import java.util.List;

/**
 * @author levry
 */
public class CountrySearch {

    private final CountryJpaRepository repository;

    public CountrySearch(CountryJpaRepository repository) {
        this.repository = repository;
    }

    public List<Country> findBy(Country params) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", m -> m.ignoreCase().startsWith());
        Example<Country> example = Example.of(params, matcher);
        return repository.findAll(example);
    }

}
