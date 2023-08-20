package com.example.locationtrackingservice

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.managers.permission.PermissionManager
import com.example.locationtrackingservice.repository.LocationRepository
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States
import kotlinx.coroutines.launch

class MainActivityViewModel(
    application: Application,
    private val locationTrackingStateMachine: LocationTrackingStateMachine,
    private val permissionManager: PermissionManager,
    private val locationRepository: LocationRepository
) : AndroidViewModel(application) {

    private var readyToTrackCalled = false
    private val _collectedLocations = MutableLiveData<List<LocationEntity>>()
    val collectedLocations: LiveData<List<LocationEntity>>
        get() = _collectedLocations


    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = permissionManager.onRequestPermissionResult(requestCode, permissions, grantResults)

    fun requestPermissions(activity: AppCompatActivity, callback: (Boolean) -> Unit) =
        permissionManager.requestPermissions(activity, callback)

    fun isPermissionGranted() = permissionManager.permissionGranted()

    fun startLocationUpdates() = locationTrackingStateMachine.getLocationUpdates()
    fun stopLocationUpdates() = locationTrackingStateMachine.removeLocationUpdates()

    fun readyToTrack() {
        if (!readyToTrackCalled) {
            locationTrackingStateMachine.transitionTo(States.READY)
            readyToTrackCalled = true
        }
    }

    fun startTracking() {
        if (readyToTrackCalled) {
            viewModelScope.launch {
                locationRepository.clearLocations()
            }
            locationTrackingStateMachine.transitionTo(States.RUNNING)
        } else Log.d(
            LOG_TAG_STATE, "THE APPLICATION IS NOT READY, GRANT PERMISSIONS"
        )
    }

    fun stopTracking() {
        locationTrackingStateMachine.transitionTo(States.DONE)
        viewModelScope.launch {
            _collectedLocations.value = locationRepository.getLocations()
        }
    }
}