package com.example.sampletask2.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sampletask2.data.local.db.dao.CollectionDao
import com.example.sampletask2.data.local.db.dao.StoryDao
import com.example.sampletask2.data.local.db.entity.CollectionEntity
import com.example.sampletask2.data.local.db.entity.StoryEntity
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        CollectionEntity::class,
        StoryEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao

    abstract fun storyDao(): StoryDao
}