package com.example.locationtrackingservice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getAll(): List<LocationEntity>

    @Insert
    suspend fun insertAll(vararg locationEntities: LocationEntity)
}