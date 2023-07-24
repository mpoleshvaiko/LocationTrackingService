package com.example.locationtrackingservice.managers.map

import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView

interface MapManager {
    fun displayLocation(location: Location, mapView: MapView?)
    fun onSaveMapState(map: GoogleMap, mapView: MapView)
    fun onRestoreMapState(map: GoogleMap, mapView: MapView)
}