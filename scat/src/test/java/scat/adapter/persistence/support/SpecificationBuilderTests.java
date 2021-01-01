package scat.adapter.persistence.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.criteria.*;

import static javax.persistence.criteria.JoinType.INNER;
import static javax.persistence.criteria.JoinType.LEFT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author levry
 */
class SpecificationBuilderTests {

    @Mock
    private Root<Object> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void toPredicate_should_return_null_if_empty_specs() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        Predicate predicate = spec.toPredicate(root, query, builder);

        assertNull(predicate);
    }

    @Test
    @DisplayName("eq(string): should build predicate 'lower(property) = value'")
    void eq_should_build_lower_property_equal_lower_string() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.eq("name", "Petro");
        spec.toPredicate(root, query, builder);

        assertAll(
            () -> verify(root).get(eq("name")),
            () -> verify(builder).lower(any()),
            () -> verify(builder).equal(any(), eq("petro"))
        );
    }

    @Test
    @DisplayName("eq: should build predicate 'property = value'")
    void eq_should_build_property_equal_value() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.eq("number", 3);
        spec.toPredicate(root, query, builder);

        assertAll(
            () -> verify(root).get(eq("number")),
            () -> verify(builder).equal(any(), eq(3))
        );
    }

    @Test
    @DisplayName("isNull: should build predicate 'property is null'")
    void isNull_should_build_property_is_null() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.isNull("region");
        spec.toPredicate(root, query, builder);

        assertAll(
            () -> verify(root).get(eq("region")),
            () -> verify(builder).isNull(any())
        );
    }

    @Test
    @DisplayName("ilike: should build predicate 'lower(property) like value%'")
    void ilike_should_build_property_like_lower_value() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.ilike("title", "Hero");
        spec.toPredicate(root, query, builder);

        assertAll(
            () -> verify(root).get(eq("title")),
            () -> verify(builder).lower(any()),
            () -> verify(builder).like(any(), eq("hero%"))
        );
    }

    @Test
    @DisplayName("join: should join root with path")
    void join_should_join_with_path() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.join("region", regionSpec -> {});
        spec.toPredicate(root, query, builder);

        verify(root).join(eq("region"));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("join: should accept nested spec for joined path")
    void join_should_accept_nested_spec() {
        Join<Object, Object> joinRegion = mock(Join.class);
        when(root.join(eq("region"))).thenReturn(joinRegion);

        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.join("region", regionSpec -> regionSpec.eq("name", "Ural"));
        spec.toPredicate(root, query, builder);

        assertAll(
            () -> verify(root, never()).get(eq("name")),
            () -> verify(joinRegion).get(eq("name")),
            () -> verify(builder).lower(any()),
            () -> verify(builder).equal(any(), eq("ural"))
        );
    }

    @Test
    void fetch_should_inner_fetch_with_path() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.fetch("country", countrySpec -> {});
        spec.toPredicate(root, query, builder);

        verify(root).fetch(eq("country"), eq(INNER));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("fetch: should accept nested spec for fetched path")
    void fetch_should_accept_nested_spec() {
        From<Object, Object> fetchCountry = mock(From.class, withSettings().extraInterfaces(Fetch.class));
        when(root.fetch(eq("country"), any())).thenReturn((Fetch<Object, Object>) fetchCountry);


        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.fetch("country", countrySpec -> countrySpec.eq("name", "Russia"));
        spec.toPredicate(root, query, builder);

        assertAll(
                () -> verify(root, never()).get(eq("name")),
                () -> verify(fetchCountry).get(eq("name")),
                () -> verify(builder).lower(any()),
                () -> verify(builder).equal(any(), eq("russia"))
        );
    }

    @Test
    void leftFetch_should_fetch_with_left_path() {
        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.leftFetch("country", regionSpec -> {});
        spec.toPredicate(root, query, builder);

        verify(root).fetch(eq("country"), eq(LEFT));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("leftFetch: should accept nested spec for fetched path")
    void leftFetch_should_accept_nested_spec() {
        From<Object, Object> fetchCountry = mock(From.class, withSettings().extraInterfaces(Fetch.class));
        when(root.fetch(eq("country"), any())).thenReturn((Fetch<Object, Object>) fetchCountry);


        SpecificationBuilder<Object> spec = new SpecificationBuilder<>();

        spec.leftFetch("country", countrySpec -> countrySpec.eq("name", "Germany"));
        spec.toPredicate(root, query, builder);

        assertAll(
                () -> verify(root, never()).get(eq("name")),
                () -> verify(fetchCountry).get(eq("name")),
                () -> verify(builder).lower(any()),
                () -> verify(builder).equal(any(), eq("germany"))
        );
    }

}