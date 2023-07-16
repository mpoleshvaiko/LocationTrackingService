package com.example.locationtrackingservice

import android.app.Application
import androidx.lifecycle.*
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States
import com.google.android.gms.maps.MapView

class MainActivityViewModel(
    application: Application,
    private val locationTrackingStateMachine: LocationTrackingStateMachine
) : AndroidViewModel(application) {

    fun initializeMapView(mapView: MapView) = locationTrackingStateMachine.setMapView(mapView)
    fun readyToTrack() = locationTrackingStateMachine.transitionTo(States.READY)
    fun startTracking() = locationTrackingStateMachine.transitionTo(States.RUNNING)
    fun pauseTracking() = locationTrackingStateMachine.transitionTo(States.PAUSE)
    fun stopTracking() = locationTrackingStateMachine.transitionTo(States.DONE)
}