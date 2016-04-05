package com.coltan.tapnote;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Robert Mathew on 5/4/16.
 */
public class MyDebugApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
