package com.example.sampletask2.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sampletask2.data.local.db.entity.CollectionEntity

@Dao
interface CollectionDao {

    @Insert
    fun insertCollection(collectionEntity: CollectionEntity): Long

    @Query("SELECT * FROM collection")
    fun getAllCollections(): List<CollectionEntity>

    @Query("SELECT * FROM collection where id = :id")
    fun getCollectionById(id: Long): CollectionEntity

    @Query("DELETE FROM collection")
    fun nukeCollectionTable(): Int
}