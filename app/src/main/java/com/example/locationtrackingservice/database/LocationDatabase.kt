package com.example.locationtrackingservice.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationEntity::class], version = 2)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}