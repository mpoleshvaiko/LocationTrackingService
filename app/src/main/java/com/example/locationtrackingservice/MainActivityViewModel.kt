package com.example.locationtrackingservice

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.locationtrackingservice.managers.location.LocationManager
import com.example.locationtrackingservice.managers.permission.PermissionManager
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States

class MainActivityViewModel(
    application: Application,
    private val locationTrackingStateMachine: LocationTrackingStateMachine,
    private val permissionManager: PermissionManager,
    private val locationManager: LocationManager
) : AndroidViewModel(application) {

    private var readyToTrackCalled = false

    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = permissionManager.onRequestPermissionResult(requestCode, permissions, grantResults)

    fun requestPermissions(activity: AppCompatActivity, callback: (Boolean) -> Unit) =
        permissionManager.requestPermissions(activity, callback)

    fun startLocationUpdates() = locationManager.requestLocationUpdate()
    fun stopLocationUpdates() = locationManager.requestLocationUpdate()

    fun readyToTrack() {
        if (!readyToTrackCalled) {
            locationTrackingStateMachine.transitionTo(States.READY)
            readyToTrackCalled = true
        }
    }

    fun startTracking() = locationTrackingStateMachine.transitionTo(States.RUNNING)
    fun pauseTracking() = locationTrackingStateMachine.transitionTo(States.PAUSE)
    fun stopTracking() = locationTrackingStateMachine.transitionTo(States.DONE)
}