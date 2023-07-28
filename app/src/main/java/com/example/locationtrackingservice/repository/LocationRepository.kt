package com.example.locationtrackingservice.repository

import com.example.locationtrackingservice.database.LocationEntity

interface LocationRepository {
    fun getLocations(): List<LocationEntity>
    suspend fun insertLocation(locationEntity: LocationEntity)
}