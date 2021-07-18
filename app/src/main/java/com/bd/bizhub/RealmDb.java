package com.bd.bizhub;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.sync.ClientResetRequiredError;
import io.realm.mongodb.sync.SyncSession;

public class RealmDb extends Application {
    App app;
    @Override
    public void onCreate() {
        super.onCreate();



        // on below line we are
        // initializing our realm database.
        Realm.init(this);

        // on below line we are setting realm configuration
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .schemaVersion(1)
                        // below line is to allow write
                        // data to database on ui thread.
                        .allowWritesOnUiThread(true)

                        // below line is to delete realm
                        // if migration is needed.

                        .allowQueriesOnUiThread(true)
                        .migration(new Migration())
                        .deleteRealmIfMigrationNeeded()

                        // at last we are calling a method to build.
                        .build();
        // on below line we are setting
        // configuration to our realm database.
        Realm.setDefaultConfiguration(config);

        SyncSession.ClientResetHandler handler = (session, error) -> Log.e("EXAMPLE", "Client Reset required for: " +
                session.getConfiguration().getServerUrl() + " for error: " +
                error.toString());

        app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultClientResetHandler(handler)
                .build());

    }

    public App getApp() {
        return app;
    }

    public void setApp(App someApp) {
        this.app = someApp;
    }


}