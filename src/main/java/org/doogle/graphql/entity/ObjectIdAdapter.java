package org.doogle.graphql.entity;

import io.smallrye.graphql.api.Adapter;
import org.bson.types.ObjectId;

public class ObjectIdAdapter implements Adapter<ObjectId, String> {
    @Override
    public ObjectId from(String o) throws Exception{
        return new ObjectId(o);
    }
    @Override
    public String to(ObjectId a) throws Exception{
        return a.toHexString();
    }
}
