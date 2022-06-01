package com.madhu.codeChal.nycschools.domain.get_sat_score_interactor;

import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.data.SatDataResponse;

import io.reactivex.Single;

public interface GetSatScoreDataInteractor {

    Single<SatDataResponse> getSatScoreDataByDbn(String dbn);

}
