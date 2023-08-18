package com.example.locationtrackingservice.repository

import com.example.locationtrackingservice.database.LocationEntity

interface LocationRepository {
    suspend fun getLocations(): List<LocationEntity>
    suspend fun insertLocation(locationEntity: LocationEntity)
    suspend fun clearLocations()
}