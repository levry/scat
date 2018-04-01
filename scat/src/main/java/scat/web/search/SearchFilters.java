package scat.web.search;

import scat.repo.support.Criteria;
import scat.repo.support.SpecificationBuilder;

import java.util.function.Consumer;

/**
 * @author levry
 */
public class SearchFilters<T> {

    private final SpecificationBuilder<T> builder;

    SearchFilters() {
        this(new SpecificationBuilder<>());
    }

    private SearchFilters(SpecificationBuilder<T> builder) {
        this.builder = builder;
    }

    public void notNulls(Consumer<Criteria> criteriaConsumer) {
        criteriaConsumer.accept(builder.notNulls());
    }

    public void joinBy(String path, Consumer<SearchFilters<?>> filtersConsumer) {
        builder.join(path, joinBuilder -> filtersConsumer.accept(new SearchFilters<>(joinBuilder)));
    }

    public void byIdAndName(Number id, String name) {
        notNulls(criteria -> {
            criteria.eq("id", id);
            criteria.ilike("name", name);
        });
    }

    public void joinByIdAndName(String path, Number id, String name) {
        joinBy(path, filters -> filters.byIdAndName(id, name));
    }

    public SpecificationBuilder<T> specification() {
        return builder;
    }
}
