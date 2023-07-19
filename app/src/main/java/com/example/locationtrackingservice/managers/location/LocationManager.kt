package com.example.locationtrackingservice.managers.location

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationManager {
    fun getCurrentLocation(): LiveData<Location?>
    fun requestLocationUpdate()
}