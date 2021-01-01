package scat.adapter.web.support.json;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

/**
 * @author levry
 */
class JsonWriter {

    private final JsonGenerator jgen;

    JsonWriter(JsonGenerator jgen) {
        this.jgen = jgen;
    }

    void field(String name, Long value) throws IOException {
        if (null != value) {
            jgen.writeNumberField(name, value);
        }
    }

    void field(String name, Integer value) throws IOException {
        if (null != value) {
            jgen.writeNumberField(name, value);
        }
    }

    void field(String name, String value) throws IOException {
        if (null != value) {
            jgen.writeStringField(name, value);
        }
    }

    <T> void fieldObjectIf(String name, T value, CheckedConsumer<T> consumer) throws IOException {
        if (null != value) {
            fieldObject(name, value, consumer);
        }
    }

    <T> void fieldObject(String name, T value, CheckedConsumer<T> consumer) throws IOException {
        jgen.writeFieldName(name);
        jgen.writeStartObject();
        consumer.accept(value);
        jgen.writeEndObject();
    }

    void fieldNull(String name, Integer value) throws IOException {
        jgen.writeFieldName(name);
        if (null != value) {
            jgen.writeNumber(value);
        } else {
            jgen.writeNull();
        }
    }

    void fieldObject(String name, Object obj) throws IOException {
        jgen.writeObjectField(name, obj);
    }

    @FunctionalInterface
    interface CheckedConsumer<T> {
        void accept(T t) throws IOException;
    }
}
