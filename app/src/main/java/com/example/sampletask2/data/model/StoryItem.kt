package com.example.sampletask2.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StoryItem(
    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("type")
    val type: String,

    @Expose
    @SerializedName("story")
    val story: Story
) {

    data class Story(

        @Expose
        @SerializedName("author-name")
        val authorName: String,

        @Expose
        @SerializedName("headline")
        val headline: String,

        @Expose
        @SerializedName("summary")
        val summary: String,

        @Expose
        @SerializedName("hero-image")
        val heroImage: String
    )
}