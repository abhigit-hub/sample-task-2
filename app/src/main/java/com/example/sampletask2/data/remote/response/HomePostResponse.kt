package com.example.sampletask2.data.remote.response

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomePostResponse(

    @Expose
    @SerializedName("slug")
    val slug: String,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("summary")
    val summary: String,

    @Expose
    @SerializedName("items")
    val responseList: List<JsonObject>
)