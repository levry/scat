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
    protected void serializeObject(City value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("name", value.getName());
        jgen.writeFieldName("country");
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getCountry().getId());
        jgen.writeStringField("name", value.getCountry().getName());
        jgen.writeEndObject();
        if(null != value.getRegion()) {
            jgen.writeFieldName("region");
            jgen.writeStartObject();
            jgen.writeNumberField("id", value.getRegion().getId());
            jgen.writeStringField("name", value.getRegion().getName());
            jgen.writeEndObject();
        }
    }
}
