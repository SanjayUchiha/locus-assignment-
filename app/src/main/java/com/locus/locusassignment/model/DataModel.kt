package com.locus.locusassignment.model
import com.google.gson.annotations.SerializedName

data class DataModel(
    @SerializedName("dataMap")
    val dataMap: DataMap,
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String
)

data class DataMap(
    @SerializedName("options")
    val options: List<String>
)

