package com.example.locationtrackingservice.stateMachine

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.locationtrackingservice.LOG_TAG_STATE
import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.managers.location.LocationManager
import com.example.locationtrackingservice.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LocationTrackingStateMachineImpl(
    private val locationRepository: LocationRepository,
    private val locationManager: LocationManager
) :
    LocationTrackingStateMachine {
    private val _currentState = MutableLiveData<States>(States.IDLE)
    override val currentState: LiveData<States> get() = _currentState
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun transitionTo(newState: States) {
        handleStateTransition(newState)
    }

    override fun getLocationUpdates(): Flow<Location?> = locationManager.requestLocationUpdate()
    override fun removeLocationUpdates() {
        locationManager.removeLocationUpdate()
    }

    private suspend fun saveLocationToDatabase(location: Location) {
        val locationEntity = LocationEntity(location = "$location")
        locationRepository.insertLocation(locationEntity)
    }

    private fun handleStateTransition(newState: States) {
        when (newState) {
            States.IDLE -> {
                Log.d(LOG_TAG_STATE, "IDLE")
            }
            States.READY -> {
                Log.d(LOG_TAG_STATE, "READY")
            }
            States.RUNNING -> {
                Log.d(LOG_TAG_STATE, "RUNNING")
                getLocationUpdates()
                    .onEach {
                        if (it != null) {
                            saveLocationToDatabase(it)
                        }
                    }
                    .launchIn(scope)
            }
            States.PAUSE -> {
                Log.d(LOG_TAG_STATE, "PAUSE")
            }
            States.DONE -> {
                Log.d(LOG_TAG_STATE, "DONE")
                cancel()
            }
            States.ERROR -> {
                Log.e(LOG_TAG_STATE, "ERROR")
            }
        }
        _currentState.value = newState
    }

    private fun cancel() = scope.coroutineContext.cancelChildren()
}