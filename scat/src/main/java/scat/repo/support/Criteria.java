package scat.repo.support;

import java.util.function.Consumer;

/**
 * @author levry
 */
public interface Criteria {

    void eq(String property, Object value);

    void ilike(String property, String value);

    default Criteria notNulls() {
        return new NotNullCriteria(this);
    }

    default void notNulls(Consumer<Criteria> criteriaConsumer) {
        criteriaConsumer.accept(notNulls());
    }
}
