package com.example.locationtrackingservice.managers.map

import android.location.Location
import com.google.android.gms.maps.MapView

interface MapManager {
    fun displayLocation(location: Location, mapView: MapView?)
}