package com.madhu.codeChal.nycschools.di.app_component;

import com.madhu.codeChal.nycschools.data.SatScoreDataDbRepoImpl;
import com.madhu.codeChal.nycschools.data.SatScoreDataRepoImpl;
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.GetSatScoreDataInteractor;
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.SatScoreDataDbRepo;
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.SatScoreDataRepo;
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.impl.GetSatScoreDataInteractorImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class SatScoreModule {

    @AppScope
    @Provides
    static GetSatScoreDataInteractor getSatScoreDataInteractor(GetSatScoreDataInteractorImpl getSatScoreDataInteractor){
        return getSatScoreDataInteractor;
    }

    @AppScope
    @Provides
    static SatScoreDataRepo satScoredDataRepo(SatScoreDataRepoImpl satScoredDataRepo){
        return satScoredDataRepo;
    }

    @AppScope
    @Provides
    static SatScoreDataDbRepo satScoredDataDbRepo(SatScoreDataDbRepoImpl satScoredDataRepo){
        return satScoredDataRepo;
    }

}
