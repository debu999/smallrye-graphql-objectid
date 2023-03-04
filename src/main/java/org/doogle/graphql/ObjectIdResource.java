package org.doogle.graphql;

import io.smallrye.mutiny.Uni;
import org.doogle.graphql.entity.ReactivePerson;
import org.doogle.graphql.enums.Action;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class ObjectIdResource {

    @Query("Person")
    public Uni<List<ReactivePerson>> fetchAllPersons()
    {
        return ReactivePerson.listAll();
    }

    @Mutation("Person")
    public Uni<ReactivePerson> mutatePerson(@Name("person") @Description("CREATE/UPDATE/DELETE person")
                                            ReactivePerson person,
                                            @Name("action") @Description("action for mutation")
                                            @NonNull Action action)
    {
        if (action==Action.DELETE)
        {
            return ReactivePerson.deleteByIdentifier(person);
        }
        return ReactivePerson.persistOrUpdate(person).map(ignored -> person);
    }
}
