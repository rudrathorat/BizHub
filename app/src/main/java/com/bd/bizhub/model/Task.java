package com.bd.bizhub.model;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;
import kotlin.jvm.internal.Intrinsics;


public class Task extends RealmObject {
    @PrimaryKey
    @Required
    @RealmField("_id")
    private ObjectId id;
    @Required
    private String name;
    @Required
    private String description;
    private String owner;
    @Required
    private String status;

    public Task() { }

    public Task(@NotNull String name, String description) {
        super();
        this.id = new ObjectId();
        this.name = name;
        this.description = description;

        this.status = TaskStatus.Open.name();
    }

    @NotNull
    public final TaskStatus getStatusEnum() {
        TaskStatus var1;
        try {
            var1 = TaskStatus.valueOf(this.status);
        } catch (IllegalArgumentException var3) {
            var1 = TaskStatus.Open;
        }

        return var1;
    }

    public final void setStatusEnum(@NotNull TaskStatus value) {
        this.status = value.name();
    }

    // Standard getters & setters
    public ObjectId get_id() { return id; }
    public void set_id(ObjectId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}