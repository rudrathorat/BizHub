package com.bd.bizhub.model;
import org.jetbrains.annotations.NotNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;


public class User extends RealmObject {
    @PrimaryKey
    @Required
    @RealmField("_id")
    private String id;
    @Required
    private String _partition;
    private RealmList<Project> memberOf;
    @Required
    private String name;


   // private String Full_name;
    public User() { }



    // Standard getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String get_partition() { return _partition; }
    public void set_partition(String _partition) { this._partition = _partition; }
    public RealmList getMemberOf() { return memberOf; }
    public void setMemberOf(RealmList memberOf) { this.memberOf = memberOf; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}