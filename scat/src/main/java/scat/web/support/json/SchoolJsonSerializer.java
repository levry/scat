package scat.web.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectSerializer;
import scat.data.School;

import java.io.IOException;

/**
 * @author levry
 */
@JsonComponent
public class SchoolJsonSerializer extends JsonObjectSerializer<School> {

    @Override
    public Class<School> handledType() {
        return School.class;
    }

    @Override
    protected void serializeObject(School school, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        JsonWriter json = new JsonWriter(jgen);
        json.field("id", school.getId());
        json.field("name", school.getName());
        json.fieldNull("number", school.getNumber());
        json.fieldObjectIf("type", school.getType(), type -> {
            json.field("id", type.getId());
            json.field("name", type.getName());
        });
        json.fieldObject("city", school.getCity());
    }
}