package com.example.locationtrackingservice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationManager(private val context: Context) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback? = null

    fun setLocationCallback(callback: LocationCallback) {
        locationCallback = callback
    }

    fun getLastKnownLocation(): LiveData<Location?> {
        val locationLiveData = MutableLiveData<Location?>()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    locationCallback?.onLastLocationReceived(location)

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