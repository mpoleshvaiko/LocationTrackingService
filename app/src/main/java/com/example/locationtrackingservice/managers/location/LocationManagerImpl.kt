package com.example.locationtrackingservice.managers.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationManagerImpl(private val context: Context) : LocationManager {
    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var _locationCallback: LocationCallback? = null

    override fun setLocationCallback(callback: LocationCallback) {
        _locationCallback = callback
    }

    override fun getLastKnownLocation(): LiveData<Location?> {
        val locationLiveData = MutableLiveData<Location?>()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            _fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    _locationCallback?.onLastLocationReceived(location)

                    locationLiveData.value = location
                }
            }
        }
        return locationLiveData
    }
}

interface LocationCallback {
    fun onLastLocationReceived(location: Location)
}