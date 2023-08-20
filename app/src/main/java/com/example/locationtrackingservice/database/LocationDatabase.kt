package com.example.locationtrackingservice.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationEntity::class], version = 3)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}