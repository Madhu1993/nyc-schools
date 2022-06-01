package com.madhu.codeChal.nycschools.domain.get_school_list_interactor;

import com.madhu.codeChal.nycschools.entities.School;

import java.util.List;

import io.reactivex.Completable;

public interface SchoolListDbRepo extends SchoolListRepo{

    Completable storeSchools(List<School> schools);

}
