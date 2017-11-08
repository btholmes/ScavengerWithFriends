package com.example.btholmes.scavenger11.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by wdian on 2/6/15.
 */
public class ScavengerApplication extends Application {

    @Override
    public void onCreate() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        super.onCreate();
    }

}
