package scat.web.support.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.io.IOException;

/**
 * @author levry
 */
public class EntityJsonDeserializer extends StdScalarDeserializer<Object> {

    private final Class<?> idType;
    private final transient EntityManager em;

    public EntityJsonDeserializer(EntityType entityType, EntityManager em) {
        this(entityType.getJavaType(), entityType.getIdType().getJavaType(), em);
    }

    public EntityJsonDeserializer(Class<?> entityType, Class<?> idType, EntityManager em) {
        super(entityType);
        this.idType = idType;
        this.em = em;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object id = p.readValueAs(idType);
        return em.find(handledType(), id);
    }

}
