package com.bitfarmsoftware.starwars;

import android.app.Application;

import com.bitfarmsoftware.starwars.di.AndroidModule;
import com.bitfarmsoftware.starwars.di.DaggerMainComponent;
import com.bitfarmsoftware.starwars.di.StarWarsComponent;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class StarWarsApplication extends Application {

    private StarWarsComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }

        if(appComponent == null){
            // build dagger component
            appComponent = DaggerMainComponent.builder()
                    .androidModule(new AndroidModule(this))
                    .build();
        }
    }

    public StarWarsComponent getApplicationComponent(){
        return this.appComponent;
    }

    public void setApplicationComponent(StarWarsComponent component){
        this.appComponent = component;
    }

}
