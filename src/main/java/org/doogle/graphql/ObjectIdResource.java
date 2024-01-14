package org.doogle.graphql;

import io.smallrye.graphql.json.JsonBCreator;
import io.smallrye.mutiny.Uni;
import jakarta.json.JsonValue.ValueType;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.doogle.graphql.entity.ReactivePerson;
import org.doogle.graphql.enums.Action;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class ObjectIdResource {

  public static JsonbSerializer<ObjectId> OBJECTID_SERIALIZER = new JsonbSerializer<>() {
    @Override
    public void serialize(ObjectId objectId, JsonGenerator jsonGenerator,
        SerializationContext serializationContext) {
      jsonGenerator.write(objectId.toString());
    }
  };

  public static JsonbDeserializer<ObjectId> OBJECTID_DESERIALIZER = new JsonbDeserializer<>() {
    @Override
    public ObjectId deserialize(JsonParser jsonParser,
        DeserializationContext deserializationContext, Type type) {
      try {
        if (jsonParser.getValue().getValueType() == ValueType.NULL) {
          return null;
        } else {
          return new ObjectId(jsonParser.getString());
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  };

  @Query("Person")
  public Uni<List<ReactivePerson>> fetchAllPersons() {

    JsonBCreator.setJsonB(Collections.singletonList(OBJECTID_SERIALIZER),
        Collections.singletonList(OBJECTID_DESERIALIZER));
    return ReactivePerson.listAll();
  }

  @Mutation("Person")
  public Uni<ReactivePerson> mutatePerson(
      @Name("person") @Description("CREATE/UPDATE/DELETE person") ReactivePerson person,
      @Name("action") @Description("action for mutation") @NonNull Action action) {
    if (action == Action.DELETE) {
      return ReactivePerson.deleteByIdentifier(person);
    }
    return ReactivePerson.persistOrUpdate(person).map(ignored -> person);
  }

}
