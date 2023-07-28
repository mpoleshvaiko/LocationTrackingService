package com.example.locationtrackingservice.stateMachine

import android.location.Location
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface LocationTrackingStateMachine {
    val currentState: LiveData<States>
    fun transitionTo(newState: States)
    suspend fun saveLocationToDataBase(location: Location)

    fun getLocationUpdates(): Flow<Location?>
    fun removeLocationUpdates()
}