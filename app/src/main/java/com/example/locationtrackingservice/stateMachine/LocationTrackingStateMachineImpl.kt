package com.example.locationtrackingservice.stateMachine

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.locationtrackingservice.LocationCallback
import com.example.locationtrackingservice.LocationManager
import com.example.locationtrackingservice.MapManager
import com.google.android.gms.maps.MapView


class LocationTrackingStateMachineImpl(
    private val mapManager: MapManager,
    private val locationManager: LocationManager,
    private val mapView: LiveData<MapView?>
) :
    LocationTrackingStateMachine, LocationCallback {
    private var currentState: States = States.IDLE

    init {
        locationManager.setLocationCallback(this)
    }

    override fun transitionTo(newState: States) {
        currentState = newState
        handleStateTransition(newState)
    }

    override fun onLastLocationReceived(location: Location) {
        mapManager.displayLocation(location, mapView.value)
    }

    private fun handleStateTransition(newState: States) {
        when (newState) {
            States.IDLE -> {
                Log.d(LOG_TAG, "IDLE")
            }
            States.READY -> {
                locationManager.getLastKnownLocation().value
                Log.d(LOG_TAG, "READY")
            }
            States.RUNNING -> {
                Log.d(LOG_TAG, "RUNNING")
            }
            States.PAUSE -> {
                Log.d(LOG_TAG, "PAUSE")
            }
            States.DONE -> {
                Log.d(LOG_TAG, "DONE")
            }
            States.ERROR -> {
                Log.e(LOG_TAG, "ERROR")
            }
        }
    }
}

const val LOG_TAG = "STATE"