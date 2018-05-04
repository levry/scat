package scat.repo.support;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * @author levry
 */
class NotNullCriteriaTest {

    @Test
    void eq_should_filter_if_value_is_not_null() {
        Criteria criteria = mock(Criteria.class);
        Criteria notNulls = new NotNullCriteria(criteria);

        notNulls.eq("prop", "value");

        verify(criteria).eq("prop", "value");
    }

    @Test
    void eq_should_no_filter_if_value_is_null() {
        Criteria criteria = mock(Criteria.class);
        Criteria notNulls = new NotNullCriteria(criteria);

        notNulls.eq("prop", null);

        verify(criteria, never()).eq("prop", null);
    }

    @Test
    void ilike_should_filter_if_value_has_text() {
        Criteria criteria = mock(Criteria.class);
        Criteria notNulls = new NotNullCriteria(criteria);

        notNulls.ilike("prop", "text");

        verify(criteria).ilike("prop", "text");
    }

    @Test
    void ilike_should_no_filter_if_value_no_text() {
        Criteria criteria = mock(Criteria.class);
        Criteria notNulls = new NotNullCriteria(criteria);

        notNulls.ilike("prop", "");

        verify(criteria, never()).ilike("prop", "");
    }
}