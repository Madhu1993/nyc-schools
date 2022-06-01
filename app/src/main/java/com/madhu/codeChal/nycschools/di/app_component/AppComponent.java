package com.madhu.codeChal.nycschools.di.app_component;

import com.madhu.codeChal.nycschools.view.school_list_activity.SchoolListViewModelImpl;

import dagger.Component;

@Component(modules = {NetworkModule.class,
        SchoolListModule.class,
        SatScoreModule.class,
        RxModule.class,
        DbModule.class})
@AppScope
public interface AppComponent {

    void inject(SchoolListViewModelImpl schoolListPresenter);

}
