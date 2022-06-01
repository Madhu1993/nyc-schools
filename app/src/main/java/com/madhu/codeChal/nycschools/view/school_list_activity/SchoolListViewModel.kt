package com.madhu.codeChal.nycschools.view.school_list_activity

import androidx.lifecycle.LiveData
import com.madhu.codeChal.nycschools.adapters.school_list.SchoolListItemUiModel

interface SchoolListViewModel {
    fun getSchoolList(): LiveData<SchoolListUiModel>
    fun onSchoolListItemSelected(schoolListItemUiModel: SchoolListItemUiModel)
}
