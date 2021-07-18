package com.bd.bizhub;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Migration);
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
       // RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {

        //    schema.create("Headings")
        //            .addField("make", String.class)
        //            .addField("model", String.class)
        //            .addField("primaryKEY", String.class, FieldAttribute.PRIMARY_KEY);
            oldVersion++;
        }else{
            oldVersion++;
        }
    }
}
