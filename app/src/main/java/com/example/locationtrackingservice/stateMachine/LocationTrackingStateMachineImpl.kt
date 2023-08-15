package com.example.locationtrackingservice.stateMachine

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.locationtrackingservice.LOG_TAG_STATE
import com.example.locationtrackingservice.managers.location.LocationManager
import com.example.locationtrackingservice.service.LocationForegroundService
import kotlinx.coroutines.flow.Flow


class LocationTrackingStateMachineImpl(
    private val locationManager: LocationManager,
    private val applicationContext: Context
) :
    LocationTrackingStateMachine {
    private val _currentState = MutableLiveData<States>(States.IDLE)
    override val currentState: LiveData<States> get() = _currentState

    override fun transitionTo(newState: States) {
        handleStateTransition(newState)
    }

    override fun getLocationUpdates(): Flow<Location?> = locationManager.requestLocationUpdate()
    override fun removeLocationUpdates() {
        locationManager.removeLocationUpdate()
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
                LocationForegroundService.startService(
                    applicationContext,
                    "Foreground Service Running"
                )
            }
            States.PAUSE -> {
                Log.d(LOG_TAG_STATE, "PAUSE")
            }
            States.DONE -> {
                Log.d(LOG_TAG_STATE, "DONE")
                LocationForegroundService.stopService(applicationContext)
            }
            States.ERROR -> {
                Log.e(LOG_TAG_STATE, "ERROR")
            }
        }
        _currentState.value = newState
    }
}