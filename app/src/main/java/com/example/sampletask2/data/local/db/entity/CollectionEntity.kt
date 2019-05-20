package com.example.sampletask2.data.local.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
class CollectionEntity(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long,

    @ColumnInfo(name = "collection_id")
    @NonNull
    var collectionId: Long,

    @ColumnInfo(name = "name")
    @NonNull
    val name: String,

    @ColumnInfo(name = "url")
    @NonNull
    val url: String,

    @ColumnInfo(name = "slug")
    @NonNull
    val slug: String
)