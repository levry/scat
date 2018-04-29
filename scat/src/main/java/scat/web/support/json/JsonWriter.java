package scat.web.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import scat.data.City;

import java.io.IOException;

/**
 * @author levry
 */
public class JsonWriter {

    private final JsonGenerator jgen;

    JsonWriter(JsonGenerator jgen) {
        this.jgen = jgen;
    }

    public void field(String name, Long value) throws IOException {
        if (null != value) {
            jgen.writeNumberField(name, value);
        }
    }

    public void field(String name, Integer value) throws IOException {
        if (null != value) {
            jgen.writeNumberField(name, value);
        }
    }

    public void field(String name, String value) throws IOException {
        if (null != value) {
            jgen.writeStringField(name, value);
        }
    }

    public <T> void fieldObjectIf(String name, T value, CheckedConsumer<T> consumer) throws IOException {
        if (null != value) {
            jgen.writeFieldName(name);
            jgen.writeStartObject();
            consumer.accept(value);
            jgen.writeEndObject();
        }
    }

    public void fieldNull(String name, Integer value) throws IOException {
        jgen.writeFieldName(name);
        if (null != value) {
            jgen.writeNumber(value);
        } else {
            jgen.writeNull();
        }

    }

    public void fieldObject(String name, Object obj) throws IOException {
        jgen.writeObjectField(name, obj);
    }

    @FunctionalInterface
    interface CheckedConsumer<T> {
        void accept(T t) throws IOException;
    }
}
