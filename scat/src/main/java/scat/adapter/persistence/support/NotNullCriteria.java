package scat.adapter.persistence.support;

import org.springframework.util.StringUtils;

/**
 * @author levry
 */
public class NotNullCriteria implements Criteria {

    private final Criteria criteria;

    NotNullCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void eq(String property, Object value) {
        if(null != value) {
            criteria.eq(property, value);
        }
    }

    @Override
    public void ilike(String property, String value) {
        if(StringUtils.hasText(value)) {
            criteria.ilike(property, value);
        }
    }
}
