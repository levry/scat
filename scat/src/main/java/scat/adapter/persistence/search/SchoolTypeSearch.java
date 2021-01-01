package scat.adapter.persistence.search;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import scat.domain.model.SchoolType;
import scat.adapter.persistence.SchoolTypeJpaRepository;

import java.util.List;

/**
 * @author levry
 */
public class SchoolTypeSearch {

    private final SchoolTypeJpaRepository repository;

    public SchoolTypeSearch(SchoolTypeJpaRepository repository) {
        this.repository = repository;
    }

    public List<SchoolType> findBy(SchoolType params) {
        ExampleMatcher matching = ExampleMatcher.matching()
                .withMatcher("name", m -> m.ignoreCase().startsWith());
        Example<SchoolType> example = Example.of(params, matching);
        return repository.findAll(example);
    }
}
