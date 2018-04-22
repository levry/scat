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
    protected void serializeObject(School school, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumberField("id", school.getId());
        jgen.writeStringField("name", school.getName());
        jgen.writeFieldName("number");
        if(null != school.getNumber()) {
            jgen.writeNumber(school.getNumber());
        } else {
            jgen.writeNull();
        }
        jgen.writeFieldName("type");
        jgen.writeStartObject();
        jgen.writeNumberField("id", school.getType().getId());
        jgen.writeStringField("name", school.getType().getName());
        jgen.writeEndObject();
        jgen.writeFieldName("city");
        jgen.writeObject(school.getCity());
    }
}
