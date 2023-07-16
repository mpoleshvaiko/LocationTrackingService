package com.example.locationtrackingservice.stateMachine

import android.location.Location
import android.util.Log
import com.example.locationtrackingservice.managers.location.LocationCallback
import com.example.locationtrackingservice.managers.location.LocationManager
import com.example.locationtrackingservice.managers.map.MapManager
import com.google.android.gms.maps.MapView


class LocationTrackingStateMachineImpl(
    private val mapManager: MapManager,
    private val locationManager: LocationManager
) :
    LocationTrackingStateMachine, LocationCallback {
    private var currentState: States = States.IDLE
    private var currentMapView: MapView? = null

    init {
        locationManager.setLocationCallback(this)
    }

    override fun transitionTo(newState: States) {
        currentState = newState
        handleStateTransition(newState)
    }


    override fun onLastLocationReceived(location: Location) {
        if (currentMapView != null) mapManager.displayLocation(location, currentMapView)
        else Log.e("MapView", "MapView not intialized")
    }

    override fun setMapView(mapView: MapView) {
        currentMapView = mapView
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