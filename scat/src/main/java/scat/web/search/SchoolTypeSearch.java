package scat.web.search;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;

import java.util.List;

/**
 * @author levry
 */
public class SchoolTypeSearch {

    private final SchoolTypeRepository repository;

    public SchoolTypeSearch(SchoolTypeRepository repository) {
        this.repository = repository;
    }

    public List<SchoolType> findBy(SchoolType params) {
        ExampleMatcher matching = ExampleMatcher.matching()
                .withMatcher("name", m -> m.ignoreCase().startsWith());
        Example<SchoolType> example = Example.of(params, matching);
        return repository.findAll(example);
    }
}
