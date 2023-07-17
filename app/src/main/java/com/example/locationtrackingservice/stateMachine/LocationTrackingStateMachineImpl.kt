package com.example.locationtrackingservice.stateMachine

import android.location.Location
import android.util.Log
import com.example.locationtrackingservice.LOG_TAG_MAP_VIEW
import com.example.locationtrackingservice.LOG_TAG_STATE
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
        else Log.e(LOG_TAG_MAP_VIEW, "MapView not initialized")
    }

    override fun setMapView(mapView: MapView) {
        currentMapView = mapView
    }

    private fun handleStateTransition(newState: States) {
        when (newState) {
            States.IDLE -> {
                Log.d(LOG_TAG_STATE, "IDLE")
            }
            States.READY -> {
                locationManager.getLastKnownLocation().value
                Log.d(LOG_TAG_STATE, "READY")
            }
            States.RUNNING -> {
                Log.d(LOG_TAG_STATE, "RUNNING")
            }
            States.PAUSE -> {
                Log.d(LOG_TAG_STATE, "PAUSE")
            }
            States.DONE -> {
                Log.d(LOG_TAG_STATE, "DONE")
            }
            States.ERROR -> {
                Log.e(LOG_TAG_STATE, "ERROR")
            }
        }
    }
}