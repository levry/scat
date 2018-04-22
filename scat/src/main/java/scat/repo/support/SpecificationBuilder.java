package scat.repo.support;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static javax.persistence.criteria.JoinType.INNER;
import static javax.persistence.criteria.JoinType.LEFT;

/**
 * @author levry
 */
// TODO test it
public class SpecificationBuilder<T> implements Specification<T>, Criteria {

    private interface Spec {
        Predicate toPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
    }

    private final List<Spec> specs = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return buildPredicate(root, query, builder);
    }

    private Predicate buildPredicate(From<?, ?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (specs.isEmpty()) {
            return null;
        }

        if (specs.size() == 1) {
            return specs.iterator().next().toPredicate(root, query, builder);
        }

        Predicate[] predicates = specs.stream()
                .map(spec -> spec.toPredicate(root, query, builder))
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);
        return builder.and(predicates);
    }

    // criteria methods

    @Override
    public void eq(String property, Object value) {
        specs.add((root, query, builder) -> builder.equal(root.get(property), value));
    }

    @Override
    public void ilike(String property, String value) {
        specs.add((root, query, builder) ->
            builder.like(builder.lower(root.get(property)), value.toLowerCase() + '%')
        );
    }

    // joins

    public <R> void join(String path, Consumer<SpecificationBuilder<R>> builderConsumer) {
        specs.add(joinSpec(builderConsumer, root -> root.join(path)));
    }

    public <R> void fetch(String path, Consumer<SpecificationBuilder<R>> builderConsumer) {
        fetch(path, INNER, builderConsumer);
    }

    public <R> void leftFetch(String path, Consumer<SpecificationBuilder<R>> builderConsumer) {
        fetch(path, LEFT, builderConsumer);
    }

    public <R> void fetch(String path, JoinType joinType, Consumer<SpecificationBuilder<R>> builderConsumer) {
        specs.add(joinSpec(builderConsumer, root -> (From<?, ?>) root.fetch(path, joinType)));
    }

    private <R> Spec joinSpec(Consumer<SpecificationBuilder<R>> builderConsumer,
                              Function<From<?, ?>, From<?, ?>> joiner) {
        return (root, query, criteriaBuilder) -> {
            SpecificationBuilder<R> joinBuilder = new SpecificationBuilder<>();
            builderConsumer.accept(joinBuilder);
            return joinBuilder.buildPredicate(joiner.apply(root), query, criteriaBuilder);
        };
    }

}
