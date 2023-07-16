package com.example.locationtrackingservice

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.locationtrackingservice.managers.permission.PermissionManager
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States
import com.google.android.gms.maps.MapView

class MainActivityViewModel(
    application: Application,
    private val locationTrackingStateMachine: LocationTrackingStateMachine,
    private val permissionManager: PermissionManager
) : AndroidViewModel(application) {

    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = permissionManager.onRequestPermissionResult(requestCode, permissions, grantResults)

    fun initializeMapView(mapView: MapView) = locationTrackingStateMachine.setMapView(mapView)
    fun readyToTrack(activity: AppCompatActivity) {
        permissionManager.requestPermissions(activity) { granted ->
            if (granted) locationTrackingStateMachine.transitionTo(States.READY)
            else Log.d("PERMISSIONS", "NOT GRANTED")
        }
    }

    fun startTracking() = locationTrackingStateMachine.transitionTo(States.RUNNING)
    fun pauseTracking() = locationTrackingStateMachine.transitionTo(States.PAUSE)
    fun stopTracking() = locationTrackingStateMachine.transitionTo(States.DONE)
}