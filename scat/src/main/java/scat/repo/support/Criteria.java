package scat.repo.support;

/**
 * @author levry
 */
public interface Criteria {

    void eq(String property, Object value);

    void ilike(String property, String value);

    default Criteria notNulls() {
        return new NotNullCriteria(this);
    }
}
