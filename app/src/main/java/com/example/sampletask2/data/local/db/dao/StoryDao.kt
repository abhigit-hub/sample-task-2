package com.example.sampletask2.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sampletask2.data.local.db.entity.StoryEntity

@Dao
interface StoryDao {

    @Insert
    fun insertStory(storyEntity: StoryEntity): Long

    @Query("SELECT * FROM story where collection_id = :collectionId ORDER BY is_premium DESC, id ASC")
    fun getStoriesByCollectionId(collectionId: Long): List<StoryEntity>

    @Query("DELETE FROM story")
    fun nukeStoryTable(): Int
}