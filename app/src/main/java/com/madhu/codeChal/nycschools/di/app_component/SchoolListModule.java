package com.madhu.codeChal.nycschools.di.app_component;

import com.madhu.codeChal.nycschools.data.SchoolListDbRepoImpl;
import com.madhu.codeChal.nycschools.data.SchoolListWebRepoImpl;
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.GetSchoolListInteractor;
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.SchoolListDbRepo;
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.SchoolListRepo;
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.impl.GetSchoolListInteractorImpl;

import dagger.Module;
import dagger.Provides;

@Module
class SchoolListModule {

    @AppScope
    @Provides
    static SchoolListRepo getSchoolListRepo(SchoolListWebRepoImpl schoolListWebRepo){
        return schoolListWebRepo;
    }

    @AppScope
    @Provides
    static SchoolListDbRepo getSchoolListDbRepo(SchoolListDbRepoImpl schoolListDbRepo){
        return schoolListDbRepo;
    }

    @AppScope
    @Provides
    static GetSchoolListInteractor getSchoolListInteractor(GetSchoolListInteractorImpl getSchoolListInteractor){
        return getSchoolListInteractor;
    }

}
