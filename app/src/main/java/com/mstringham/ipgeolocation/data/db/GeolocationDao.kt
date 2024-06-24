package com.mstringham.ipgeolocation.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GeolocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Geolocation): Long

    @Update
    suspend fun update(item: Geolocation)

    @Delete
    suspend fun delete(item: Geolocation)

    @Query("DELETE FROM geolocation")
    suspend fun deleteAllItems()

    @Query("SELECT * from geolocation WHERE `query` = :query LIMIT 1")
    suspend fun getItemByQuery(query: String): Geolocation?

    @Query("SELECT * from geolocation ORDER BY id DESC")
    suspend fun getAllItems(): List<Geolocation>
}