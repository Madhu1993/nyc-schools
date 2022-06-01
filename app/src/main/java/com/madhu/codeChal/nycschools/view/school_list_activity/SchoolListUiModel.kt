package com.madhu.codeChal.nycschools.view.school_list_activity

import com.madhu.codeChal.nycschools.adapters.school_list.SchoolListItemUiModel

data class SchoolListUiModel(val schoolListItemUiModels: List<SchoolListItemUiModel>, val errorMessage: String? = null)