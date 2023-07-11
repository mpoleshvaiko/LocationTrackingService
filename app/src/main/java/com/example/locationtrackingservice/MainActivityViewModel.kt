package com.example.locationtrackingservice

import android.app.Application
import androidx.lifecycle.*
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachineImpl
import com.example.locationtrackingservice.stateMachine.States
import com.google.android.gms.maps.MapView

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val locationTrackingStateMachine: LocationTrackingStateMachine
    private val _mapView: MutableLiveData<MapView> = MutableLiveData()

    init {
        val mapManager = MapManager()
        val locationManager = LocationManager(application.applicationContext)

        locationTrackingStateMachine = LocationTrackingStateMachineImpl(
            mapManager,
            locationManager,
            _mapView
        )
    }

    fun initializeMapView(mapView: MapView) {
        _mapView.value = mapView
    }

//    fun readyToTrack() {
//        permissionManager.requestPermissions { granted ->
//            if (granted) {
//                locationTrackingStateMachine.transitionTo(States.READY)
//            } else {
//                Log.d("PERMISSIONS", "NOT GRANTED")
//            }
//        }
//    }

    fun readyToTrack() = locationTrackingStateMachine.transitionTo(States.READY)
    fun startTracking() = locationTrackingStateMachine.transitionTo(States.RUNNING)
    fun pauseTracking() = locationTrackingStateMachine.transitionTo(States.PAUSE)
    fun stopTracking() = locationTrackingStateMachine.transitionTo(States.DONE)
}