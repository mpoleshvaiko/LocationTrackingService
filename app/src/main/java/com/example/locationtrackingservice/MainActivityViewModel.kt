package com.example.locationtrackingservice

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.locationtrackingservice.managers.permission.PermissionManager
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States

class MainActivityViewModel(
    application: Application,
    private val locationTrackingStateMachine: LocationTrackingStateMachine,
    private val permissionManager: PermissionManager
) : AndroidViewModel(application) {

    private var readyToTrackCalled = false

    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = permissionManager.onRequestPermissionResult(requestCode, permissions, grantResults)

    fun requestPermissions(activity: AppCompatActivity, callback: (Boolean) -> Unit) =
        permissionManager.requestPermissions(activity, callback)

    fun startLocationUpdates() = locationTrackingStateMachine.getLocationUpdates()
    fun stopLocationUpdates() = locationTrackingStateMachine.removeLocationUpdates()

    fun readyToTrack() {
        if (!readyToTrackCalled) {
            locationTrackingStateMachine.transitionTo(States.READY)
            readyToTrackCalled = true
        }
    }

    fun startTracking() {
        if (readyToTrackCalled) locationTrackingStateMachine.transitionTo(States.RUNNING)
        else Log.d(
            LOG_TAG_STATE, "THE APPLICATION IS NOT READY, GRANT PERMISSIONS"
        )
    }

    fun pauseTracking() = locationTrackingStateMachine.transitionTo(States.PAUSE)
    fun stopTracking() = locationTrackingStateMachine.transitionTo(States.DONE)
}