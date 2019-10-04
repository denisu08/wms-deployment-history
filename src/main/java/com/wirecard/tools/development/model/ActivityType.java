package com.wirecard.tools.development.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class ActivityType {
    @Id
    public ObjectId _id;
    public String name;

    public ActivityType() {}

    public ActivityType(ObjectId _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
