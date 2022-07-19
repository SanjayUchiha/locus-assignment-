package com.locus.locusassignment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locus.locusassignment.model.DataModel
import com.locus.locusassignment.repository.DataRepository
import kotlinx.coroutines.launch

class DataViewModel @ViewModelInject constructor(private val repo: DataRepository) : ViewModel() {
    var model = MutableLiveData<ArrayList<DataModel>>()
    var map = HashMap<String, Any>()

    fun getData() {
        viewModelScope.launch {
            var data = repo.getAssestData()
            model.value = data

        }
    }
}