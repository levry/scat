package scat.web.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import scat.data.Country;
import scat.web.model.RegionModel;
import scat.web.support.json.EntityJsonModule;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author levry
 */
public class EntityJsonDeserializerTest {

    @Test
    public void deserialize_domain_in_field() throws Exception {

        Country country = country(23, "Russia");

        EntityManager entityManager = mockEntityManager();
        when(entityManager.find(Country.class, 23)).thenReturn(country);
        ObjectMapper mapper = objectMapper(entityManager);

        String json = "{\"name\": \"Ural\", \"country\": 23}";
        RegionModel model = mapper.readValue(json, RegionModel.class);

        Assert.assertThat(model.getName(), is("Ural"));
        Assert.assertThat(model.getCountry(), is(country));
    }

    @Test
    public void deserialize_domain() throws Exception {
        EntityManager entityManager = mockEntityManager();
        ObjectMapper mapper = objectMapper(entityManager);

        String json = "{ \"id\": 2, \"name\": \"Russia\" }";
        Country country = mapper.readValue(json, Country.class);

        Assert.assertThat(country.getId(), is(2));
        Assert.assertThat(country.getName(), is("Russia"));
    }

    @Test
    public void deserialize_collection_domains() throws Exception {
        Country russia = country(23, "Russia");
        Country germany = country(3, "Germany");

        EntityManager entityManager = mockEntityManager();
        when(entityManager.find(Country.class, 23)).thenReturn(russia);
        when(entityManager.find(Country.class, 3)).thenReturn(germany);
        ObjectMapper mapper = objectMapper(entityManager);

        String json = "{\"countries\": [23, 3]}";
        CountryList model = mapper.readValue(json, CountryList.class);

        Assert.assertEquals(model.getCountries(), Arrays.asList(russia, germany));
    }

    @Test
    public void deserialize_array_domains() throws Exception {
        Country russia = country(23, "Russia");
        Country germany = country(3, "Germany");

        EntityManager entityManager = mockEntityManager();
        when(entityManager.find(Country.class, 23)).thenReturn(russia);
        when(entityManager.find(Country.class, 3)).thenReturn(germany);
        ObjectMapper mapper = objectMapper(entityManager);

        String json = "{\"countries\": [23, 3]}";
        CountryArray model = mapper.readValue(json, CountryArray.class);

        Assert.assertArrayEquals(new Country[] {russia, germany}, model.getCountries());
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
