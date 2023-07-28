package com.example.locationtrackingservice.managers.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationManager {
    fun getCurrentLocation(): Flow<Location?>
    fun requestLocationUpdate(): Flow<Location?>
    fun removeLocationUpdate()
}