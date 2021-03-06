package scat.adapter.web.support.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.JsonExpectationsHelper;
import scat.domain.model.City;
import scat.domain.model.Country;
import scat.domain.model.Region;

import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

/**
 * @author levry
 */
class CityJsonSerializerTests {

    private final ObjectMapper objectMapper = json()
            .serializers(new CityJsonSerializer())
            .build();

    private final JsonExpectationsHelper jsonHelper = new JsonExpectationsHelper();

    @Test
    void serialize_empty() throws Exception {
        City city = new City();

        String json = objectMapper.writeValueAsString(city);

        jsonHelper.assertJsonEqual("{}", json);
    }

    @Test
    void serialize_city() throws Exception {
        City city = new City();
        city.setId(2L);
        city.setName("Ekaterinburg");
        city.setCountry(country(7, "Russia"));
        city.setRegion(region(66, "Ural"));

        String json = objectMapper.writeValueAsString(city);

        jsonHelper.assertJsonEqual("{\"id\":2," +
                "\"name\":\"Ekaterinburg\"," +
                "\"country\":{\"id\":7,\"name\":\"Russia\"}," +
                "\"region\":{\"id\":66,\"name\":\"Ural\"}}", json);
    }

    @Test
    void serialize_with_null_region() throws Exception {
        City city = new City();
        city.setId(2L);
        city.setName("Moscow");
        city.setCountry(country(7, "Russia"));
        city.setRegion(null);

        String json = objectMapper.writeValueAsString(city);

        jsonHelper.assertJsonEqual("{\"id\":2," +
                "\"name\":\"Moscow\"," +
                "\"country\":{\"id\":7,\"name\":\"Russia\"}}", json);
    }

    @Test
    void serialize_with_null_country() throws Exception {
        City city = new City();
        city.setId(2L);
        city.setName("Ekaterinburg");
        city.setCountry(null);
        city.setRegion(region(66, "Ural"));

        String json = objectMapper.writeValueAsString(city);

        jsonHelper.assertJsonEqual("{\"id\":2," +
                "\"name\":\"Ekaterinburg\"," +
                "\"region\":{\"id\":66,\"name\":\"Ural\"}}", json);
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