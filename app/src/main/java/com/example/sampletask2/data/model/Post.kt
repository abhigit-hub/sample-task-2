package com.example.sampletask2.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(

    @Expose
    @SerializedName("id")
    val id: Long,

    @Expose
    @SerializedName("type")
    val type: String,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("url")
    val url: String,

    @Expose
    @SerializedName("slug")
    val slug: String
)