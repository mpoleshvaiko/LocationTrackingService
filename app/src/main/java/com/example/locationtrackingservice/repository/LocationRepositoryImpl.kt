package com.example.locationtrackingservice.repository

import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.database.LocationDao

class LocationRepositoryImpl(private val locationDao: LocationDao) : LocationRepository {
    override fun getLocations() = locationDao.getAll()
    override suspend fun insertLocation(locationEntity: LocationEntity) = locationDao.insertAll(locationEntity)
}