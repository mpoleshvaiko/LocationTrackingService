package com.example.locationtrackingservice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    suspend fun getAll(): List<LocationEntity>

    @Insert
    suspend fun insertAll(vararg locationEntities: LocationEntity)

    @Query("DELETE FROM location")
    suspend fun removeAllLocations()
}