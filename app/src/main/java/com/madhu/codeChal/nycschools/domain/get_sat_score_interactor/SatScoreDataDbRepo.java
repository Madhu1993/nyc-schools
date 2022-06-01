package com.madhu.codeChal.nycschools.domain.get_sat_score_interactor;

import com.madhu.codeChal.nycschools.entities.SatScoreData;

import io.reactivex.Completable;

public interface SatScoreDataDbRepo extends SatScoreDataRepo{

    Completable storeSatData(SatScoreData satScoreData);

}
