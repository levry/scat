package scat.web.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import scat.data.Country;
import scat.web.model.RegionModel;
import scat.web.support.json.EntityJsonModule;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author levry
 */
class EntityJsonDeserializerTest {

    @Test
    void deserialize_domain_in_field() throws Exception {

        Country country = country(23, "Russia");

        EntityManager entityManager = mockEntityManager();
        when(entityManager.find(Country.class, 23)).thenReturn(country);
        ObjectMapper mapper = objectMapper(entityManager);


        String json = "{\"name\": \"Ural\", \"country\": 23}";
        RegionModel model = mapper.readValue(json, RegionModel.class);


        assertThat(model.getName()).isEqualTo("Ural");
        assertThat(model.getCountry()).isEqualTo(country);
    }

    @Test
    void deserialize_domain() throws Exception {
        EntityManager entityManager = mockEntityManager();
        ObjectMapper mapper = objectMapper(entityManager);


        String json = "{ \"id\": 2, \"name\": \"Russia\" }";
        Country country = mapper.readValue(json, Country.class);


        assertThat(country.getId()).isEqualTo(2);
        assertThat(country.getName()).isEqualTo("Russia");
    }

    @Test
    void deserialize_collection_domains() throws Exception {
        Country russia = country(23, "Russia");
        Country germany = country(3, "Germany");

        EntityManager entityManager = mockEntityManager();
        when(entityManager.find(Country.class, 23)).thenReturn(russia);
        when(entityManager.find(Country.class, 3)).thenReturn(germany);
        ObjectMapper mapper = objectMapper(entityManager);


        String json = "{\"countries\": [23, 3]}";
        CountryList model = mapper.readValue(json, CountryList.class);


        assertThat(model.getCountries()).containsExactlyInAnyOrder(russia, germany);
    }

    @Test
    void deserialize_array_domains() throws Exception {
        Country russia = country(23, "Russia");
        Country germany = country(3, "Germany");

        EntityManager entityManager = mockEntityManager();
        when(entityManager.find(Country.class, 23)).thenReturn(russia);
        when(entityManager.find(Country.class, 3)).thenReturn(germany);
        ObjectMapper mapper = objectMapper(entityManager);


        String json = "{\"countries\": [23, 3]}";
        CountryArray model = mapper.readValue(json, CountryArray.class);


        assertThat(model.getCountries()).containsExactlyInAnyOrder(russia, germany);
    }

    private EntityManager mockEntityManager() {
        Type type = mock(Type.class);
        when(type.getJavaType()).thenReturn(Integer.class);

        EntityType<Country> entityType = mock(EntityType.class);
        when(entityType.getIdType()).thenReturn(type);
        when(entityType.getJavaType()).thenReturn(Country.class);

        Metamodel metamodel = mock(Metamodel.class);
        when(metamodel.entity(Country.class)).thenReturn(entityType);

        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getMetamodel()).thenReturn(metamodel);
        return entityManager;
    }

    private static ObjectMapper objectMapper(EntityManager entityManager) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modules(new EntityJsonModule(entityManager));
        return builder.build();
    }

    private static class CountryList {

        private List<Country> countries;

        public List<Country> getCountries() {
            return countries;
        }

        public void setCountries(List<Country> countries) {
            this.countries = countries;
        }
    }

    private static class CountryArray {

        private Country[] countries;

        public Country[] getCountries() {
            return countries;
        }

        public void setCountries(Country[] countries) {
            this.countries = countries;
        }
    }

    private static Country country(Integer id, String name) {
        Country country = new Country();
        country.setId(id);
        country.setName(name);
        return country;
    }

}
