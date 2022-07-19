package com.locus.locusassignment.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.locus.locusassignment.model.DataModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataRepository @Inject constructor(@ApplicationContext val context: Context) {

    fun getAssestData():ArrayList<DataModel>{
        var jsonString:String = context.assets.open("data.json").bufferedReader().use { it.readText() }

        val gson = Gson()
        val listPersonType = object : TypeToken<ArrayList<DataModel>>() {}.type
        var dataList: ArrayList<DataModel> = gson.fromJson(jsonString, listPersonType)
        return dataList
    }

}