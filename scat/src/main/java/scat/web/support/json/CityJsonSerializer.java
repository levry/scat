package scat.web.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectSerializer;
import scat.data.City;

import java.io.IOException;

/**
 * @author levry
 */
@JsonComponent
public class CityJsonSerializer extends JsonObjectSerializer<City> {

    @Override
    public Class<City> handledType() {
        return City.class;
    }

    @Override
    protected void serializeObject(City value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        JsonWriter json = new JsonWriter(jgen);
        json.field("id", value.getId());
        json.field("name", value.getName());

        json.fieldObjectIf("country", value.getCountry(), country -> {
            json.field("id", country.getId());
            json.field("name", country.getName());
        });

        json.fieldObjectIf("region", value.getRegion(), region -> {
            json.field("id", region.getId());
            json.field("name", region.getName());
        });
    }

}
