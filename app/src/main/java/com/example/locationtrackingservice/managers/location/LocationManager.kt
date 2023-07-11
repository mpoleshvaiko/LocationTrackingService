package com.example.locationtrackingservice.managers.location

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationManager {
    fun getLastKnownLocation(): LiveData<Location?>
    fun setLocationCallback(callback: LocationCallback)
}