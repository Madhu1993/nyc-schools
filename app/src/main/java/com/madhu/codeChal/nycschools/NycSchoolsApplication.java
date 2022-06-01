package com.madhu.codeChal.nycschools;

import android.app.Application;

import com.madhu.codeChal.nycschools.di.ComponentProviderImpl;
import com.madhu.codeChal.nycschools.di.app_component.AppComponent;
import com.madhu.codeChal.nycschools.di.app_component.DaggerAppComponent;
import com.madhu.codeChal.nycschools.di.app_component.DbModule;

public class NycSchoolsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent appComponent = DaggerAppComponent.builder()
                .dbModule(new DbModule(getApplicationContext()))
                .build();

        ComponentProviderImpl.initComponentProvider(appComponent);
    }
}
