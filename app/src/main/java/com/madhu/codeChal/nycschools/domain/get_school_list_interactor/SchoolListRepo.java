package com.madhu.codeChal.nycschools.domain.get_school_list_interactor;

import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.data.SchoolListResponse;
import com.madhu.codeChal.nycschools.entities.Borough;

import io.reactivex.Single;


public interface SchoolListRepo {

    Single<SchoolListResponse> getSchoolsByBorough(Borough borough);

}
