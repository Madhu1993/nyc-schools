package com.madhu.codeChal.nycschools.view.school_list_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.madhu.codeChal.nycschools.adapters.school_list.SchoolListItemUiModel
import com.madhu.codeChal.nycschools.adapters.school_list.SchoolListItemType
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.GetSatScoreDataInteractor
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.data.SatDataResponse
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.GetSchoolListInteractor
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.data.SchoolListResponse
import com.madhu.codeChal.nycschools.entities.Borough
import com.madhu.codeChal.nycschools.entities.SatScoreData
import com.madhu.codeChal.nycschools.entities.School
import com.madhu.codeChal.nycschools.rx_util.SchedulerProvider
import com.madhu.codeChal.nycschools.view.base.BaseViewModel

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject

import io.reactivex.disposables.Disposable

class SchoolListViewModelImpl : BaseViewModel(), SchoolListViewModel {

    @Inject
    lateinit var getSchoolListInteractor: GetSchoolListInteractor

    @Inject
    lateinit var getSatScoreDataInteractor: GetSatScoreDataInteractor

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val schoolListItemUiModels: MutableList<SchoolListItemUiModel> = ArrayList()

    private val schoolListUiModelLiveData: MutableLiveData<SchoolListUiModel> = MutableLiveData()

    private val pendingDownloads = HashMap<String, Disposable>()

    init {
        componentProvider.appComponent
                .inject(this)
    }

    private fun postUpdatedList() {
        schoolListUiModelLiveData.postValue(SchoolListUiModel(schoolListItemUiModels))
    }

    private fun postWithError(error: String) {
        schoolListUiModelLiveData.postValue(SchoolListUiModel(schoolListItemUiModels, error))
    }

    override fun getSchoolList(): LiveData<SchoolListUiModel> {
        if (schoolListItemUiModels.isEmpty()) {
            for (borough in Borough.values()) {
                val boroughItem = SchoolListItemUiModel.createBoroughItem(borough)
                schoolListItemUiModels.add(boroughItem)
            }
        }
        postUpdatedList()

        return schoolListUiModelLiveData
    }

    override fun onSchoolListItemSelected(schoolListItemUiModel: SchoolListItemUiModel) {
        if (schoolListItemUiModel.type == SchoolListItemType.BOROUGH_TITLE) {
            onBoroughSelected(schoolListItemUiModel)
        } else if (schoolListItemUiModel.type == SchoolListItemType.SCHOOL_ITEM) {
            onSchoolSelected(schoolListItemUiModel)
        }
    }

    private fun onBoroughSelected(schoolListItemUiModel: SchoolListItemUiModel) {
        val borough = schoolListItemUiModel.borough
        //cancel download of json and remove school cells from list
        if (schoolListItemUiModel.isSelected) {
            for (i in schoolListItemUiModels.size - 1 downTo 0) {
                if (schoolListItemUiModels[i].borough == borough) {
                    if (schoolListItemUiModels[i].type == SchoolListItemType.BOROUGH_TITLE) {
                        schoolListItemUiModels[i].isLoading = false
                    } else {
                        schoolListItemUiModels.removeAt(i)
                    }
                }
            }
            postUpdatedList()
            val disposable = pendingDownloads[borough.code]
            disposable?.dispose()
            return
        }

        schoolListItemUiModels.forEach {
            if (it.type == SchoolListItemType.BOROUGH_TITLE && it.borough == borough) {
                it.isLoading = true
            }
        }

        postUpdatedList()

        val disposable = getSchoolListInteractor.getSchoolsByBorough(borough)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe({ this.processGetSchoolListResponse(it, borough) },
                        { failedToLoadList(borough) })

        pendingDownloads[borough.code] = disposable

        onDestroyDisposable.add(disposable)
    }

    private fun processGetSchoolListResponse(schoolListResponse: SchoolListResponse, borough: Borough) {
        if (!schoolListResponse.isSuccessful) {
            failedToLoadList(borough)
            return
        }

        val schools = schoolListResponse.schools

        var targetIdx = 0
        schoolListItemUiModels.forEachIndexed { index, schoolListItemUiModel ->
            if (schoolListItemUiModel.borough == schoolListResponse.borough) {
                schoolListItemUiModel.isLoading = false
                targetIdx = index
            }
        }

        val newSchoolListItemUiModels = schoolsToListItems(schools)
        schoolListItemUiModels.addAll(targetIdx + 1, newSchoolListItemUiModels)
        postUpdatedList()
    }

    private fun failedToLoadList(borough: Borough) {
        schoolListItemUiModels.forEach {
            if (it.type == SchoolListItemType.BOROUGH_TITLE && it.borough == borough) {
                it.isLoading = false
            }
        }
        postWithError("Failed to load schools")
    }

    private fun schoolsToListItems(schools: List<School>): List<SchoolListItemUiModel> {
        val schoolListItemUiModels = ArrayList<SchoolListItemUiModel>()
        for (school in schools) {
            val schoolItem = SchoolListItemUiModel.createSchoolItem(school, school.borough)
            schoolListItemUiModels.add(schoolItem)
        }
        return schoolListItemUiModels
    }

    private fun onSchoolSelected(schoolListItemUiModel: SchoolListItemUiModel) {
        val school = schoolListItemUiModel.school
        if (schoolListItemUiModel.isSelected) {
            val indexOfScore = schoolListItemUiModels.indexOf(schoolListItemUiModel) + 1
            if (indexOfScore <= schoolListItemUiModels.lastIndex) {
                schoolListItemUiModels.removeAt(indexOfScore)
            }
            postUpdatedList()
            return
        }

        val disposable = getSatScoreDataInteractor.getSatScoreDataByDbn(school.dbn)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe({ satDataResponse -> processSatScoreResponse(satDataResponse, school) },
                        { throwable -> failedToGetSatData() })

        pendingDownloads[school.dbn] = disposable

        onDestroyDisposable.add(disposable)
    }

    private fun processSatScoreResponse(satDataResponse: SatDataResponse, school: School) {
        val satScoreData = satDataResponse.satScoreData
        if (!satDataResponse.isSuccessful || satScoreData == null) {
            failedToGetSatData()
            return
        }

        val scoreListItem = satDataToSchoolListItem(satScoreData, school)

        schoolListItemUiModels.forEachIndexed { index, schoolListItemUiModel ->
            if (satDataResponse.satScoreData.dbn == schoolListItemUiModel.school?.dbn) {
                schoolListItemUiModels.add(index + 1, scoreListItem)
                postUpdatedList()
                return
            }
        }
    }

    private fun failedToGetSatData() {
        postWithError("Failed to load SAT scores")
    }

    private fun satDataToSchoolListItem(satScoreData: SatScoreData, school: School): SchoolListItemUiModel {
        return SchoolListItemUiModel.newBuilder()
                .borough(school.borough)
                .type(SchoolListItemType.SAT_SCORE_ITEM)
                .school(school)
                .satScoreData(satScoreData)
                .build()

    }

}
