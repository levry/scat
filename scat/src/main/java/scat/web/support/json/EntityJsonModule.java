package scat.web.support.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.CollectionFactory;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.Iterator;

/**
 * @author levry
 */
public class EntityJsonModule extends SimpleModule {

    public EntityJsonModule(EntityManager em) {
        setDeserializerModifier(new EntityDeserializerModifier(em));
    }

    private static class EntityDeserializerModifier extends BeanDeserializerModifier {

        private final EntityManager em;

        private EntityDeserializerModifier(EntityManager em) {
            this.em = em;
        }

        @Override
        public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc, BeanDeserializerBuilder builder) {

            Iterator<SettableBeanProperty> properties = builder.getProperties();
            while (properties.hasNext()) {
                SettableBeanProperty property = properties.next();

                JavaType propType = property.getType();
                if (isCollection(propType)) {
                    propType = propType.getContentType();
                }

                Class<?> typeClass = propType.getRawClass();
                if (typeClass.isAnnotationPresent(Entity.class)) {

                    EntityJsonDeserializer deser = entityDeserializer(typeClass);
                    JsonDeserializer<?> deserializer = wrapIfCollection(property, deser, config);

                    builder.addOrReplaceProperty(property.withValueDeserializer(deserializer), false);
                }
            }

            return super.updateBuilder(config, beanDesc, builder);
        }

        private EntityJsonDeserializer entityDeserializer(Class<?> propType) {
            EntityType<?> entity = em.getMetamodel().entity(propType);
            return new EntityJsonDeserializer(entity, em);
        }

        private JsonDeserializer<?> wrapIfCollection(SettableBeanProperty property, JsonDeserializer<Object> deser, DeserializationConfig config) {
            JavaType propType = property.getType();

            if(!isCollection(propType)) {
                return deser;
            }

            TypeFactory typeFactory = config.getTypeFactory();

            if(propType.isArrayType()) {
                ArrayType arrayType = typeFactory.constructArrayType(propType.getContentType());
                return new ObjectArrayDeserializer(arrayType, deser, null);
            }

            CollectionLikeType collectionType = typeFactory
                    .constructCollectionLikeType(propType.getRawClass(), propType.getContentType());

            ValueInstantiator instantiator = new CollectionValueInstantiator(property);
            return new CollectionDeserializer(collectionType, deser, null, instantiator);
        }

        private class CollectionValueInstantiator extends ValueInstantiator {
            private final SettableBeanProperty property;

            CollectionValueInstantiator(SettableBeanProperty property) {
                this.property = property;
            }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) {
                JavaType type = property.getType();
                Class<?> collectionClass = type.getRawClass();

                if(type.isMapLikeType()) {
                    return CollectionFactory.createMap(collectionClass, 0);
                }

                return CollectionFactory.createCollection(collectionClass, 0);
            }
        }

        private static boolean isCollection(JavaType propType) {
            return propType.isCollectionLikeType() || propType.isArrayType();
        }
    }
}
