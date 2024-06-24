package com.mstringham.ipgeolocation.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Geolocation::class],
    version = 1,
    exportSchema = true
)
abstract class GeolocationDatabase : RoomDatabase() {
    abstract fun geolocationDao(): GeolocationDao

    companion object {
        const val DATABASE_NAME = "ipgeo_db"
    }
}