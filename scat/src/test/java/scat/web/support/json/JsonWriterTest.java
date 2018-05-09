package scat.web.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.springframework.test.util.JsonExpectationsHelper;
import scat.data.City;
import scat.data.Region;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

/**
 * @author levry
 */
class JsonWriterTest {

    private final JsonExpectationsHelper jsonHelper = new JsonExpectationsHelper();

    @Test
    @DisplayName("field(long): should write long")
    void fieldLong_should_write_long() throws Throwable {
        String json = withWriter(writer -> writer.field("id", 2L));

        jsonHelper.assertJsonEqual("{\"id\": 2}", json);
    }

    @Test
    @DisplayName("field(long): not should write if number is null")
    void fieldLong_not_should_write_if_number_is_null() throws Throwable {
        String json = withWriter(writer -> writer.field("id", (Long) null));

        jsonHelper.assertJsonEqual("{}", json);
    }

    @Test
    @DisplayName("field(int): should write integer")
    void fieldInteger_should_write_integer() throws Throwable {
        String json = withWriter(writer -> writer.field("number", 101));

        jsonHelper.assertJsonEqual("{\"number\": 101}", json);
    }

    @Test
    @DisplayName("field(int): not should write if number is null")
    void fieldInteger_not_should_write_if_number_is_null() throws Throwable {
        String json = withWriter(writer -> writer.field("number", (Integer) null));

        jsonHelper.assertJsonEqual("{}", json);
    }

    @Test
    @DisplayName("field(string): should write string")
    void fieldString_should_write_string() throws Throwable {
        String json = withWriter(writer -> writer.field("name", "String"));

        jsonHelper.assertJsonEqual("{\"name\": \"String\"}", json);
    }

    @Test
    @DisplayName("field(string): not should write if string is null")
    void fieldString_not_should_write_if_value_is_null() throws Throwable {
        String json = withWriter(writer -> writer.field("name", (String) null));

        jsonHelper.assertJsonEqual("{}", json);
    }

    @Test
    @DisplayName("fieldNull: should write number")
    void fieldNull_should_write_number() throws Throwable {
        String json = withWriter(writer -> writer.fieldNull("number", 23));

        jsonHelper.assertJsonEqual("{\"number\": 23}", json);
    }

    @Test
    @DisplayName("fieldNull: should write 'null' if number is null")
    void fieldNull_should_write_null_if_value_is_null() throws Throwable {
        String json = withWriter(writer -> writer.fieldNull("number", null));

        jsonHelper.assertJsonEqual("{\"number\": null}", json);
    }

    @Test
    @DisplayName("fieldObject: should write object")
    void fieldObject() throws Throwable {
        String json = withWriter(writer -> {
            HashMap<Object, Object> obj = new HashMap<>();
            obj.put("message", "Hello");
            writer.fieldObject("obj", obj);
        });

        jsonHelper.assertJsonEqual("{\"obj\": {\"message\": \"Hello\"}}", json);
    }

    @Test
    @DisplayName("fieldObjectIf: should write value")
    void fieldObjectIf_should_write_if_value_is_not_null() throws Throwable {
        City city = new City();
        city.setRegion(new Region());

        String json = withWriter(writer -> writer
            .fieldObjectIf("region", city.getRegion(), region -> {
                writer.field("name", "Ural");
            })
        );

        jsonHelper.assertJsonEqual("{\"region\": {\"name\": \"Ural\"}}", json);
    }

    @Test
    @DisplayName("fieldObjectIf: not should write if value is null")
    void fieldObjectIf_not_should_write_if_value_is_null() throws Throwable {
        City city = new City();
        city.setRegion(null);

        String json = withWriter(writer -> writer
                .fieldObjectIf("region", city.getRegion(), region -> {
                    writer.field("name", "Ural");
                })
        );

        jsonHelper.assertJsonEqual("{ }", json);
    }

    private String withWriter(ThrowingConsumer<JsonWriter> writerConsumer) throws Throwable {
        Writer w = new StringWriter();

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator(w);

        JsonWriter writer = new JsonWriter(jgen);
        jgen.writeStartObject();
        writerConsumer.accept(writer);
        jgen.writeEndObject();
        jgen.flush();

        return w.toString();
    }
}