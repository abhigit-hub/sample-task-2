package com.example.sampletask2.data.remote.response

import com.example.sampletask2.data.model.StoryItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("summary")
    val summary: String,

    @Expose
    @SerializedName("items")
    val items: List<StoryItem>
)