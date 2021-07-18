package com.bd.bizhub.model;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Member extends RealmObject {
    @NotNull
    @PrimaryKey
    private String _id;
    @NotNull
    private String name;

    // Standard getters & setters
    @NotNull
    public final String getId() {
        return this._id;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }


    public Member(@NotNull Document document) {
        this._id = (String) Objects.requireNonNull(document.get("_id"));
        this.name = (String) Objects.requireNonNull(document.get("name"));

    }
    public Member(){};
}
