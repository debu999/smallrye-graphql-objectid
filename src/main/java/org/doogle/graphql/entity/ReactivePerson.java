package org.doogle.graphql.entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.smallrye.graphql.api.AdaptWith;
import io.smallrye.mutiny.Uni;
import lombok.*;
import org.bson.types.ObjectId;
import org.doogle.graphql.enums.Status;

import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactivePerson extends ReactivePanacheMongoEntityBase {

    @AdaptWith(ObjectIdAdapter.class)
    public ObjectId id;
    public String name;
    public ZonedDateTime birth;
    public Status status;

    public static Uni<ReactivePerson> deleteByIdentifier(ReactivePerson person) {
        Uni<ReactivePerson> personUni = findById(person.id);
        return personUni.log("person").call(p -> deleteById(p.id)).log("deleted");
    }
}