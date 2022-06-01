package com.madhu.codeChal.nycschools.view.school_list_activity

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.madhu.codeChal.nycschools.adapters.school_list.OnSchoolListItemSelectedListener
import com.madhu.codeChal.nycschools.adapters.school_list.SchoolListAdapter
import com.madhu.codeChal.nycschools.adapters.school_list.SchoolListItemUiModel
import com.madhu.codechal.nycschools.R

class SchoolListActivity : AppCompatActivity(), OnSchoolListItemSelectedListener {

    private var recyclerView: RecyclerView? = null

    private lateinit var schoolListViewModel: SchoolListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.school_list_activity)


        /*We have created the model, ViewModel, and RecyclerView.
        These components need to be instantiated in the MainActivity class for the application to work.
        Start by declaring the RecyclerView and instantiating it.
        Set the layout manager for the RecyclerView to LinearLayoutManager.

        The objective is to observe for changes in the viewmodel and forwards them to the RecyclerAdapter.*/

        recyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager

        val schoolListAdapter = SchoolListAdapter(this, this, linearLayoutManager)
        recyclerView!!.adapter = schoolListAdapter

        schoolListViewModel = ViewModelProviders.of(this).get(SchoolListViewModelImpl::class.java)
        schoolListViewModel.getSchoolList().observe(this, Observer { schoolListViewModel ->
            schoolListViewModel?.let {
                schoolListAdapter.updateList(schoolListViewModel.schoolListItemUiModels)
                schoolListViewModel.errorMessage?.let {
                    toast(it)
                }
            }
        })
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSchoolListItemSelected(schoolListItemUiModel: SchoolListItemUiModel) {
        schoolListViewModel.onSchoolListItemSelected(schoolListItemUiModel)
    }
}
