package scat.web.support.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.JsonExpectationsHelper;
import scat.data.*;

import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

/**
 * @author levry
 */
class SchoolJsonSerializerTest {

    private final ObjectMapper objectMapper = json()
            .serializers(new CityJsonSerializer(), new SchoolJsonSerializer())
            .build();

    private final JsonExpectationsHelper jsonHelper = new JsonExpectationsHelper();

    @Test
    void serialize_empty() throws Exception {
        School school = new School();

        String json = objectMapper.writeValueAsString(school);

        jsonHelper.assertJsonEqual("{}", json);
    }

    @Test
    void serialize_school() throws Exception {
        School school = new School();
        school.setId(1L);
        school.setName("URFU");
        school.setNumber(23);
        school.setType(type(3, "University"));
        City city = city(8L, "Ekaterinburg", country(2, "Russia"), region(66, "Ural"));
        school.setCity(city);

        String json = objectMapper.writeValueAsString(school);

        jsonHelper.assertJsonEqual("{\"id\":1,\"name\":\"URFU\",\"number\":23," +
                "\"type\":{\"id\":3,\"name\":\"University\"}," +
                "\"city\":{\"id\":8,\"name\":\"Ekaterinburg\"}," +
                "\"country\":{\"id\":2,\"name\":\"Russia\"}," +
                "\"region\":{\"id\":66,\"name\":\"Ural\"}}", json, true);
    }

    @Test
    void serialize_with_null_type() throws Exception {
        School school = new School();
        school.setId(1L);
        school.setName("URFU");
        school.setNumber(23);
        school.setType(null);
        City city = city(8L, "Ekaterinburg", country(2, "Russia"), region(66, "Ural"));
        school.setCity(city);

        String json = objectMapper.writeValueAsString(school);

        jsonHelper.assertJsonEqual("{\"id\":1,\"name\":\"URFU\",\"number\":23," +
                "\"city\":{\"id\":8,\"name\":\"Ekaterinburg\"}," +
                "\"country\":{\"id\":2,\"name\":\"Russia\"}," +
                "\"region\":{\"id\":66,\"name\":\"Ural\"}}", json, true);
    }

    @Test
    void serialize_with_null_city() throws Exception {
        School school = new School();
        school.setId(1L);
        school.setName("URFU");
        school.setNumber(23);
        school.setType(type(3, "University"));
        school.setCity(null);

        String json = objectMapper.writeValueAsString(school);

        jsonHelper.assertJsonEqual("{\"id\":1,\"name\":\"URFU\",\"number\":23," +
                "\"type\":{\"id\":3,\"name\":\"University\"}}", json, true);
    }

    @Test
    void number_should_by_null_if_they_is_null() throws Exception {
        School school = new School();
        school.setId(1L);
        school.setName("URFU");
        school.setNumber(null);
        school.setType(type(3, "University"));
        City city = city(8L, "Ekaterinburg", country(2, "Russia"), region(66, "Ural"));
        school.setCity(city);

        String json = objectMapper.writeValueAsString(school);

        jsonHelper.assertJsonEqual("{\"id\":1,\"name\":\"URFU\",\"number\":null," +
                "\"type\":{\"id\":3,\"name\":\"University\"}," +
                "\"city\":{\"id\":8,\"name\":\"Ekaterinburg\"}," +
                "\"country\":{\"id\":2,\"name\":\"Russia\"}," +
                "\"region\":{\"id\":66,\"name\":\"Ural\"}}", json, true);
    }

    private City city(long id, String name, Country country, Region region) {
        City city = new City();
        city.setId(id);
        city.setName(name);
        city.setCountry(country);
        city.setRegion(region);
        return city;
    }

    private SchoolType type(int id, String name) {
        SchoolType type = new SchoolType();
        type.setId(id);
        type.setName(name);
        return type;
    }

    private Country country(int id, String name) {
        Country country = new Country();
        country.setId(id);
        country.setName(name);
        return country;
    }

    private Region region(int id, String name) {
        Region region = new Region();
        region.setId(id);
        region.setName(name);
        return region;
    }

}