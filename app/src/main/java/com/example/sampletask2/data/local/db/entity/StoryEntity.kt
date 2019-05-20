package com.example.sampletask2.data.local.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
class StoryEntity(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long,

    @ColumnInfo(name = "story_id")
    @NonNull
    var storyId: String,

    @ColumnInfo(name = "author_name")
    @NonNull
    var authorName: String,

    @ColumnInfo(name = "headline")
    @NonNull
    var headline: String,

    @ColumnInfo(name = "summary")
    @NonNull
    var summary: String,

    @ColumnInfo(name = "hero_url")
    @NonNull
    var heroUrl: String,

    @ColumnInfo(name = "is_premium")
    @NonNull
    var isPremium: Boolean,

    @ColumnInfo(name = "collection_id")
    @NonNull
    var collectionId: Long
)